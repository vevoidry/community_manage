package com.homework.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.web.pojo.Park;
import com.homework.web.service.impl.ParkServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/park")
@Api(tags = "路径共同前缀：/park。主要针对停车场表", description = "ParkController")
public class ParkController {

	@Autowired
	ParkServiceImpl parkServiceImpl;

	@GetMapping("/main")
	@ApiOperation("停车场管理-停车场管理：主页")
	public String main() {
		return "menu/park::main";
	}

	@GetMapping("/insert")
	@ApiOperation("停车场管理-停车场管理：新增停车场的弹出框")
	public String insert() {
		return "menu/park::insert";
	}

	@GetMapping("/select_div")
	@ApiOperation("停车场管理-停车场管理：表格")
	public String select(Model model, String id, String code, String type) {
		id = id.trim();
		code = code.trim();
		type = type.trim();
		List<Park> parkList = new ArrayList<Park>();
//		if (id.equals("")) {
		if (code.equals("") && type.equals("")) {
			parkList = parkServiceImpl.selectAll();// 1空且2空
		} else if (code.equals("") && !type.equals("")) {
			parkList = parkServiceImpl.selectByType(type);// 1空且2不空
		} else if (!code.equals("") && type.equals("")) {
			parkList = parkServiceImpl.selectByCode(Integer.parseInt(code));//1不空且2空
		} else if (!code.equals("") && !type.equals("")) {
			parkList = parkServiceImpl.selectByCodeType(Integer.parseInt(code), type);//1不空且2不空
		}

//		} else {
//			if (code.equals("")) {
//				if (type.equals("")) {
//					parkList = parkServiceImpl.selectById(Integer.parseInt(id));
//				} else {
//					parkList = parkServiceImpl.selectByIdType(Integer.parseInt(id), type);
//				}
//			} else {
//				if (type.equals("")) {
//					parkList = parkServiceImpl.selectByIdCode(Integer.parseInt(id), Integer.parseInt(code));
//				} else {
//					parkList = parkServiceImpl.selectByIdCodeType(Integer.parseInt(id), Integer.parseInt(code), type);
//				}
//			}
//		}
		model.addAttribute("parkList", parkList);
		return "menu/park::select_div";
	}

	@PostMapping
	@ResponseBody
	@ApiOperation("停车场管理-停车场管理：进行一次新增停车场的操作")
	public Park post(Park park) {
		if (parkServiceImpl.selectByCodeType(park.getCode(), park.getType()).size() != 0) {
			throw new RuntimeException("已存在同类型同编号的停车场");
		}
		return parkServiceImpl.insert(park);
	}

	@GetMapping("/update/{id:[0-9]+}")
	@ApiOperation("停车场管理-停车场管理：获取更新停车场的弹出框")
	public String update(@PathVariable Integer id, Model model) {
		List<Park> parkList = parkServiceImpl.selectById(id);
		model.addAttribute("park", parkList.get(0));
		return "menu/park::update";
	}

	@PostMapping("/update")
	@ResponseBody
	@ApiOperation("停车场管理-停车场管理：进行一次更新停车场的操作")
	public Park updatePark(Park park) {
		List<Park> parkList = parkServiceImpl.selectById(park.getId());
		Park park2 = parkList.get(0);
		if (!(park.getType().equals(park2.getType()) && park.getCode().equals(park2.getCode()))) {
			if (parkServiceImpl.selectByCodeType(park.getCode(), park.getType()).size() > 0) {
				throw new RuntimeException("同类型停车场不可存在同编号停车场");
			}
		}
		return parkServiceImpl.update(park);
	}

}
