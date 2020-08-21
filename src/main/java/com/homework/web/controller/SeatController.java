package com.homework.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.web.pojo.Park;
import com.homework.web.pojo.Person;
import com.homework.web.pojo.Person_project;
import com.homework.web.pojo.Project;
import com.homework.web.pojo.Seat;
import com.homework.web.pojo.vo.SeatVO;
import com.homework.web.service.impl.ParkServiceImpl;
import com.homework.web.service.impl.PersonServiceImpl;
import com.homework.web.service.impl.Person_projectServiceImpl;
import com.homework.web.service.impl.ProjectServiceImpl;
import com.homework.web.service.impl.SeatServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/seat")
@Api(tags = "路径共同前缀：/seat。主要针对停车位表", description = "SeatController")
public class SeatController {
	@Autowired
	SeatServiceImpl seatServiceImpl;
	@Autowired
	ParkServiceImpl parkServiceImpl;
	@Autowired
	PersonServiceImpl personServiceImpl;
	@Autowired
	ProjectServiceImpl projectServiceImpl;
	@Autowired
	Person_projectServiceImpl person_projectServiceImpl;

	@GetMapping("/main")
	@ApiOperation("停车场管理-停车位管理：主页")
	public String main(Model model) {
		List<Park> parkList = parkServiceImpl.selectAll();
		model.addAttribute("parkList", parkList);
		return "menu/seat::main";
	}

	@GetMapping("/insert")
	@ApiOperation("停车场管理-停车位管理：新增楼栋的弹出框")
	public String insert(Model model) {
		List<Park> parkList = parkServiceImpl.selectAll();
		model.addAttribute("parkList", parkList);
		return "menu/seat::insert";
	}

	@GetMapping("/select_div")
	@ApiOperation("停车场管理-停车位管理：表格")
	public String select_div(Model model, String park_id, String code, String is_sell) {
		park_id = park_id.trim();
		code = code.trim();
		is_sell = is_sell.trim();
		List<Seat> seatList = new ArrayList<Seat>();
		if (is_sell.equals("true")) {
			if (park_id.equals("")) {
				if (code.equals("")) {
					seatList = seatServiceImpl.selectByPerson_idIsNotNull();
				} else {
					seatList = seatServiceImpl.selectByCodePerson_idIsNotNull(Integer.parseInt(code));
				}
			} else {
				if (code.equals("")) {
					seatList = seatServiceImpl.selectByPark_idPerson_idIsNotNull(Integer.parseInt(park_id));
				} else {
					seatList = seatServiceImpl.selectByPark_idCodePerson_idIsNotNull(Integer.parseInt(park_id),
							Integer.parseInt(code));
				}
			}
		} else if (is_sell.equals("false")) {
			if (park_id.equals("")) {
				if (code.equals("")) {
					seatList = seatServiceImpl.selectByPerson_idIsNull();
				} else {
					seatList = seatServiceImpl.selectByCodePerson_idIsNull(Integer.parseInt(code));
				}
			} else {
				if (code.equals("")) {
					seatList = seatServiceImpl.selectByPark_idPerson_idIsNull(Integer.parseInt(park_id));
				} else {
					seatList = seatServiceImpl.selectByPark_idCodePerson_idIsNull(Integer.parseInt(park_id),
							Integer.parseInt(code));
				}
			}
		} else {
			if (park_id.equals("")) {
				if (code.equals("")) {
					seatList = seatServiceImpl.selectAll();
				} else {
					seatList = seatServiceImpl.selectByCode(Integer.parseInt(code));
				}
			} else {
				if (code.equals("")) {
					seatList = seatServiceImpl.selectByPark_id(Integer.parseInt(park_id));
				} else {
					seatList = seatServiceImpl.selectByPark_idCode(Integer.parseInt(park_id), Integer.parseInt(code));
				}
			}
		}
		ArrayList<SeatVO> seatList2 = new ArrayList<SeatVO>();
		for (Seat seat : seatList) {
			SeatVO seatVO = new SeatVO();
			BeanUtils.copyProperties(seat, seatVO);
			List<Park> parkList = parkServiceImpl.selectById(seat.getPark_id());
			seatVO.setPark_code(parkList.get(0).getCode());
			if (seat.getPerson_id() != null) {
				Person person = personServiceImpl.selectById(seat.getPerson_id());
				seatVO.setPerson_name(person.getName());
				seatVO.setPerson_phone(person.getPhone());
			}
			seatList2.add(seatVO);
		}
		model.addAttribute("seatList", seatList2);
		return "menu/seat::select_div";
	}

	@PostMapping
	@ResponseBody
	@ApiOperation("停车场管理-停车位管理：进行一次新增停车位的操作")
	public Seat post(Seat seat) {
		if (seatServiceImpl.selectByPark_idCodeRegion(seat.getPark_id(), seat.getCode(), seat.getRegion())
				.size() != 0) {
			throw new RuntimeException("该停车场下已存在同分区同编号的车位");
		}
		return seatServiceImpl.insert(seat);
	}

	@GetMapping("/update/{id:[0-9]+}")
	@ApiOperation("停车场管理-停车位管理：获取修改一个停车位的弹出框")
	public String update(Model model, @PathVariable Integer id) {
		Seat seat = seatServiceImpl.selectById(id);
		if (seat.getPerson_id() != null) {
			Person seat_person = personServiceImpl.selectById(seat.getPerson_id());
			model.addAttribute("seat_person", seat_person);
		}
		List<Person> personList = personServiceImpl.selectAll();
		List<Park> parkList = parkServiceImpl.selectAll();
		List<Park> seat_parkList = parkServiceImpl.selectById(seat.getPark_id());
		model.addAttribute("seat", seat);
		model.addAttribute("personList", personList);
		model.addAttribute("parkList", parkList);
		model.addAttribute("seat_park", seat_parkList.get(0));
		return "menu/seat::update";
	}

	@SuppressWarnings("deprecation")
	@PostMapping("/update")
	@ResponseBody
	@ApiOperation("停车场管理-停车位管理：进行一次修改停车位的操作")
	public Seat updateSeat(Seat seat, Double months) {
		Seat seat2 = seatServiceImpl.selectById(seat.getId());
		if (!(seat.getPark_id().equals(seat2.getPark_id()) && seat.getCode().equals(seat2.getCode())
				&& seat.getRegion().equals(seat2.getRegion()))) {
			if (seatServiceImpl.selectByPark_idCodeRegion(seat.getPark_id(), seat.getCode(), seat.getRegion())
					.size() > 0) {
				throw new RuntimeException("同停车场下不可存在同分区同编号车位");
			}
		}
		seat2.setArea(seat.getArea());
		seat2.setCode(seat.getCode());
		seat2.setId(seat.getId());
		seat2.setPark_id(seat.getPark_id());
		seat2.setRegion(seat.getRegion());
		seat2.setRemark(seat.getRemark());
		if (seat2.getPerson_id() == null && seat.getPerson_id() != null) {
			seat2.setPerson_id(seat.getPerson_id());
			seat2.setType(seat.getType());
			if (seat2.getType() == 1) {
				List<Project> projectList = projectServiceImpl.selectByName("停车位出售");
				if (projectList.size() == 0) {
					throw new RuntimeException("请先添加一个名为停车位出售的收费项目");
				} else {
					Project project = projectList.get(0);
					Person_project person_project = new Person_project();
					person_project.setCreate_time(new Date());
					person_project.setIs_handle(false);
					person_project.setType(false);
					person_project.setType_id(seat2.getId());
					if (project.getMoney_type()) {
						person_project.setMoney(project.getMoney());
					} else {
						person_project.setMoney(project.getMoney() * seat2.getArea());
					}
					person_project.setPerson_id(seat2.getPerson_id());
					person_project.setProject_id(project.getId());
					person_projectServiceImpl.insert(person_project);
				}
			} else if (seat2.getType() == 2) {
				if (months == null) {
					throw new RuntimeException("出租月份不可为空");
				}
				int intValue = months.intValue();
				Date now = new Date();
				GregorianCalendar gregorianCalendar = new GregorianCalendar(1900 + now.getYear(), now.getMonth(),
						now.getDate());
				gregorianCalendar.add(GregorianCalendar.MONTH, intValue);
				Date time = gregorianCalendar.getTime();
				seat2.setEnd_date(time);
			}
		}
		return seatServiceImpl.update(seat2);
	}

}
