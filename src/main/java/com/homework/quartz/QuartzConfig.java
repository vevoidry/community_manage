package com.homework.quartz;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.homework.web.pojo.Person_project;
import com.homework.web.pojo.Project;
import com.homework.web.pojo.Seat;
import com.homework.web.service.impl.PersonServiceImpl;
import com.homework.web.service.impl.Person_projectServiceImpl;
import com.homework.web.service.impl.ProjectServiceImpl;
import com.homework.web.service.impl.SeatServiceImpl;

//定时执行某些任务
//一个任务对应一个定时器
@Configuration
public class QuartzConfig {

	@Autowired
	ProjectServiceImpl projectServiceImpl;
	@Autowired
	Person_projectServiceImpl person_projectServiceImpl;
	@Autowired
	SeatServiceImpl seatServiceImpl;
	@Autowired
	PersonServiceImpl personServiceImpl;

	// 任务：每隔24小时检测一次日期，若今天是1号，则创建一个车位费项目，并对所有出租的车位的车主收费一次
	@Bean
	public JobDetail monthFirstDayGetSeatMoneyJobDetail() {
		return JobBuilder.newJob(new QuartzJobBean() {
			@SuppressWarnings("deprecation")
			@Override
			protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
				Date date = new Date();
//				date.setMonth(6);
//				date.setDate(1);
				if (date.getDate() == 1) {
					int year = date.getYear();
					int month = date.getMonth();
					int date2 = date.getDate();
					// 根据时间生成项目名
					String name = (year + 1900) + "年" + (month + 1) + "月" + date2 + "日车位费";
					// 根据项目名从数据库查询项目
					List<Project> projectList = projectServiceImpl.selectByName(name);
					// 判断该项目是否存在
					if (projectList.size() == 0) {
						// 自动生成项目
						Project project = new Project();
						project.setName(name);
						Date date3 = new Date(year, month, date2);
						project.setStart_date(date3);
						GregorianCalendar gregorianCalendar = new GregorianCalendar(1900 + year, month, date2);
						gregorianCalendar.add(GregorianCalendar.MONTH, 1);
						Date date4 = gregorianCalendar.getTime();
						project.setEnd_date(date4);
						project.setMoney_type(false);
						project.setMoney(8.0);// 8元/平米
						project.setRemark(project.getName() + "（每月1号自动生成自动执行）");
						project = projectServiceImpl.insert(project);
						Project project2 = project;
						// 对出租类型的车位的车主进行收费
						List<Seat> seatList = seatServiceImpl.selectAll();
						Iterator<Seat> iterator = seatList.iterator();
						while (iterator.hasNext()) {
							Seat seat = iterator.next();
							if (seat.getType() == 2) {
								Integer person_id = seat.getPerson_id();
								Person_project person_project = new Person_project();
								person_project.setPerson_id(person_id);
								person_project.setCreate_time(new Date());
								person_project.setProject_id(project2.getId());
								person_project.setIs_handle(false);
								person_project.setType(false);
								person_project.setType_id(seat.getId());
								if (project2.getMoney_type()) {
									person_project.setMoney(project2.getMoney());
								} else {
									person_project.setMoney(seat.getArea() * project2.getMoney());
								}
								person_projectServiceImpl.insert(person_project);
							}
						}
					}
				}
			}
		}.getClass()).storeDurably().build();
	}

	// 定时器：24小时执行一次
	@Bean
	public Trigger monthFirstDayGetSeatMoneyTrigger() {
		return TriggerBuilder.newTrigger().forJob(monthFirstDayGetSeatMoneyJobDetail())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(60 * 24).repeatForever())
				.build();
	}

	// 任务：每天检测一次所有出租中的车位，若租期到期，则回收车位
	@Bean
	public JobDetail checkRentOvertimeJobDetail() {
		return JobBuilder.newJob(new QuartzJobBean() {
			@Override
			protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
				Date date = new Date();
				List<Seat> seatList = seatServiceImpl.selectAll();
				Iterator<Seat> iterator = seatList.iterator();
				while (iterator.hasNext()) {
					Seat seat = iterator.next();
					if (seat.getType() != null && seat.getType() == 2) {
						if (seat.getEnd_date().getTime() < date.getTime()) {
							Seat seat2 = new Seat();
							BeanUtils.copyProperties(seat, seat2);
//							System.out.println(seat2.getEnd_date());
							seat2.setType(null);
							seat2.setPerson_id(null);
							seat2.setEnd_date(null);
//							System.out.println(seat2.getId());
//							System.out.println(seat2.getEnd_date());
							seatServiceImpl.update(seat2);
						}
					}
				}
			}
		}.getClass()).storeDurably().build();
	}

	// 定时器：24小时执行一次
	@Bean
	public Trigger checkRentOvertimeTrigger() {
		return TriggerBuilder.newTrigger().forJob(checkRentOvertimeJobDetail())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(60 * 24).repeatForever())
				.build();
	}

}
