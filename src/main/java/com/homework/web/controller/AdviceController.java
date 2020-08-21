package com.homework.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.web.pojo.Advice;
import com.homework.web.pojo.Person;
import com.homework.web.pojo.vo.AdviceVO;
import com.homework.web.service.impl.AdviceServiceImpl;
import com.homework.web.service.impl.PersonServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/advice")
@Api(tags = "路径共同前缀：/advice。主要针对建议意见表", description = "AdviceController")
public class AdviceController {

	@Autowired
	AdviceServiceImpl adviceServiceImpl;
	@Autowired
	PersonServiceImpl personServiceImpl;

	@GetMapping("/main")
	@ApiOperation("管理服务-投诉建议（业主）：主页")
	public String main() {
		return "menu/advice::main";
	}

	@GetMapping("/insert")
	@ApiOperation("管理服务-投诉建议（业主）：新增投诉建议的弹出框")
	public String insert() {
		return "menu/advice::insert";
	}

	@GetMapping("/select_div")
	@ApiOperation("管理服务-投诉建议（业主）：表格")
	public String select_div(Model model, String is_handle) {
		System.out.println(is_handle);
		// 获取业主信息
		Person person = (Person) SecurityUtils.getSubject().getPrincipal();
		List<Advice> adviceList = new ArrayList<Advice>();
		if (is_handle.equals("true")) {
			adviceList = adviceServiceImpl.selectByPerson_idIs_handle(person.getId(), true);
		} else if (is_handle.equals("false")) {
			adviceList = adviceServiceImpl.selectByPerson_idIs_handle(person.getId(), false);
		} else {
			adviceList = adviceServiceImpl.selectByPerson_id(person.getId());
		}
		ArrayList<AdviceVO> adviceList2 = new ArrayList<AdviceVO>();
		for (Advice advice : adviceList) {
			AdviceVO adviceVO = new AdviceVO();
			BeanUtils.copyProperties(advice, adviceVO);
			Person person2 = personServiceImpl.selectById(advice.getPerson_id());
			adviceVO.setPerson_name(person2.getName());
//			adviceVO.setStart_time_string(DateTimeUtils.format(advice.getStart_time()));
//			adviceVO.setEnd_time_string(DateTimeUtils.format(advice.getEnd_time()));
			adviceList2.add(adviceVO);
		}
		model.addAttribute("adviceList", adviceList2);
		return "menu/advice::select_div";
	}

	// --------------------------

	@PostMapping
	@ResponseBody
	@ApiOperation("管理服务-投诉建议（业主）：进行新增操作")
	public Advice post(Advice advice) {
		// 获取业主信息
		Person person = (Person) SecurityUtils.getSubject().getPrincipal();
		advice.setStart_time(new Date());
		advice.setIs_handle(false);
		advice.setPerson_id(person.getId());
		System.out.println(advice);
		return adviceServiceImpl.insert(advice);
	}

	@GetMapping("/admin/main")
	@ApiOperation("管理服务-投诉建议（管理员）：主页")
	public String admin() {
		return "menu/advice_admin::main";
	}

	@PostMapping("/admin/{id:[0-9]+}/handle")
	@ResponseBody
	@ApiOperation("管理服务-投诉建议（管理员）：进行处理操作")
	public HashMap<String, String> handle(@PathVariable Integer id) {
		Advice advice = adviceServiceImpl.selectById(id);
		if (advice.getIs_handle()) {
			throw new RuntimeException("该投诉/建议已处理过");
		} else {
			advice.setIs_handle(true);
			advice.setEnd_time(new Date());
			adviceServiceImpl.update(advice);
		}
		return new HashMap<String, String>();
	}

	@GetMapping("/admin/select_div")
	@ApiOperation("管理服务-投诉建议（管理员）：表格")
	public String admin_select_div(Model model, String is_handle) {
		System.out.println(is_handle);
		List<Advice> adviceList = new ArrayList<Advice>();
		if (is_handle.equals("true")) {
			adviceList = adviceServiceImpl.selectByIs_handle(true);
		} else if (is_handle.equals("false")) {
			adviceList = adviceServiceImpl.selectByIs_handle(false);
		} else {
			adviceList = adviceServiceImpl.selectAll();
		}
		ArrayList<AdviceVO> adviceList2 = new ArrayList<AdviceVO>();
		for (Advice advice : adviceList) {
			AdviceVO adviceVO = new AdviceVO();
			BeanUtils.copyProperties(advice, adviceVO);
			Person person2 = personServiceImpl.selectById(advice.getPerson_id());
			adviceVO.setPerson_name(person2.getName());
			adviceList2.add(adviceVO);
		}
		model.addAttribute("adviceList", adviceList2);
		return "menu/advice_admin::select_div";
	}
}
