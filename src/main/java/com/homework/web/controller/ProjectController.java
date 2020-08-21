package com.homework.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.homework.utils.DateTimeUtils;
import com.homework.web.pojo.Person_project;
import com.homework.web.pojo.Project;
import com.homework.web.pojo.vo.ProjectVO;
import com.homework.web.service.impl.Person_projectServiceImpl;
import com.homework.web.service.impl.ProjectServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/project")
@Api(tags = "路径共同前缀：/project。主要针对收费项目表", description = "ProjectController")
public class ProjectController {
	@Autowired
	ProjectServiceImpl projectServiceImpl;
	@Autowired
	Person_projectServiceImpl person_projectServiceImpl;

	@GetMapping("/main")
	@ApiOperation("财务管理-收费项目：主页")
	public String main(Model model) {
		return "menu/project::main";
	}

	@GetMapping("/insert")
	@ApiOperation("财务管理-收费项目：新增收费项目的弹出框")
	public String insert(Model model) {
		return "menu/project::insert";
	}

	@GetMapping("/select_div")
	@ApiOperation("财务管理-收费项目：表格")
	public String select_div(Model model, String name) {
		List<Project> projectList = new ArrayList<Project>();
		if (name.equals("")) {
			projectList = projectServiceImpl.selectAll();
		} else {
			projectList = projectServiceImpl.selectByName(name);
		}
		// 所有项目应收的钱和已收的钱
		Double money_sum_all = 0.0;
		Double money_sum_ok = 0.0;
		ArrayList<ProjectVO> projectList2 = new ArrayList<ProjectVO>();
		for (Project project : projectList) {
			ProjectVO projectVO = new ProjectVO();
			BeanUtils.copyProperties(project, projectVO);
			//
			List<Person_project> person_projectList = person_projectServiceImpl.selectByProject_id(project.getId());
			// 一个项目应收的钱和已收的钱
			Double project_money_sum_all = 0.0;
			Double project_money_sum_ok = 0.0;
			for (Person_project person_project : person_projectList) {
				project_money_sum_all += person_project.getMoney();
				if (person_project.getIs_handle()) {
					project_money_sum_ok += person_project.getMoney();
				}
			}
			money_sum_all += project_money_sum_all;
			money_sum_ok += project_money_sum_ok;
			projectVO.setMoney_sum_all(project_money_sum_all);
			projectVO.setMoney_sum_ok(project_money_sum_ok);
			projectList2.add(projectVO);
		}
		model.addAttribute("projectList", projectList2);
		model.addAttribute("money_sum_all", money_sum_all);
		model.addAttribute("money_sum_ok", money_sum_ok);
		return "menu/project::select_div";
	}

	@PostMapping
	@ResponseBody
	@ApiOperation("财务管理-收费项目：进行一次新增收费项目的操作")
	public Project post(Project project, String start_dateString, String end_dateString) throws Exception {
		start_dateString = start_dateString.replaceAll("T", " ");
		end_dateString = end_dateString.replaceAll("T", " ");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date start_date = simpleDateFormat.parse(start_dateString);
		Date end_date = simpleDateFormat.parse(end_dateString);
		if (start_date.getTime() >= end_date.getTime()) {
			throw new RuntimeException("缴费截止时间必须比缴费开始时间晚");
		}
		project.setStart_date(start_date);
		project.setEnd_date(end_date);
		return projectServiceImpl.insert(project);
	}

	@GetMapping("/update/{id:[0-9]+}")
	@ApiOperation("获取更新收费项目的弹出框")
	public String update(Model model, @PathVariable Integer id) {
		Project project = projectServiceImpl.selectById(id);
		ProjectVO projectVO = new ProjectVO();
		BeanUtils.copyProperties(project, projectVO);
		projectVO.setStart_date_string(DateTimeUtils.format(projectVO.getStart_date()));
		projectVO.setEnd_date_string(DateTimeUtils.format(projectVO.getEnd_date()));
		model.addAttribute("project", projectVO);
		return "menu/project::update";
	}

	@PostMapping("/update")
	@ResponseBody
	@ApiOperation("财务管理-收费项目：进行一次收费项目的更新操作")
	public Project updateProject(Project project, String start_dateString, String end_dateString) throws Exception {
		start_dateString = start_dateString.replaceAll("T", " ");
		end_dateString = end_dateString.replaceAll("T", " ");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date start_date = simpleDateFormat.parse(start_dateString);
		Date end_date = simpleDateFormat.parse(end_dateString);
		project.setStart_date(start_date);
		project.setEnd_date(end_date);
		return projectServiceImpl.update(project);
	}

}
