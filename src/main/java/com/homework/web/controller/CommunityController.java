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

import com.homework.web.pojo.Community;
import com.homework.web.service.impl.CommunityServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/community")
@Api(tags = "路径共同前缀：/community。主要针对楼栋表", description = "CommunityController")
public class CommunityController {

	@Autowired
	CommunityServiceImpl communityServiceImpl;

	@GetMapping("/main")
	@ApiOperation("档案管理-楼栋管理：主页")
	public String main() {
		return "menu/community::main";
	}

	@GetMapping("/insert")
	@ApiOperation("档案管理-楼栋管理：新增楼栋的弹出框")
	public String insert() {
		return "menu/community::insert";
	}

	@GetMapping("/select")
	@ApiOperation("档案管理-楼栋管理：表格")
	public String select_div(Model model, String id, String code) {
		// 获取参数并去除两侧空格
		id = id.trim();
		code = code.trim();
		List<Community> communityList = new ArrayList<Community>();
		if (id.equals("")) {
			if (code.equals("")) {
				communityList = communityServiceImpl.selectAll();
			} else {
				communityList = communityServiceImpl.selectByCode(Integer.parseInt(code));
			}
		} else {
			if (code.equals("")) {
				Community community = communityServiceImpl.selectById(Integer.parseInt(id));
				communityList.add(community);
			} else {
				communityList = communityServiceImpl.selectByIdCode(Integer.parseInt(id), Integer.parseInt(code));
			}
		}
		model.addAttribute("communityList", communityList);
		return "menu/community::select";
	}

	@PostMapping
	@ResponseBody
	@ApiOperation("档案管理-楼栋管理：进行新增楼栋的操作")
	public Community post(Community community) {
		if (community.getCode() <= 0) {
			throw new RuntimeException("楼栋编号必须大于0");
		}
		if (community.getRemark().equals("")) {
			throw new RuntimeException("楼栋备注不可为空");
		}
		if (communityServiceImpl.selectByCode(community.getCode()).size() != 0) {
			throw new RuntimeException("楼栋编号不可重复");
		}
		return communityServiceImpl.insert(community);
	}

	@GetMapping("/update/{id:[0-9]+}")
	@ApiOperation("档案管理-楼栋管理：获取更新楼栋的弹出框")
	public String update(Model model, @PathVariable Integer id) {
		Community community = communityServiceImpl.selectById(id);
		model.addAttribute("community", community);
		return "menu/community::update";
	}

	@PostMapping("/update")
	@ResponseBody
	@ApiOperation("档案管理-楼栋管理：进行更新楼栋的操作")
	public Community updateCommunity(Community community) {
		Community community2 = communityServiceImpl.selectById(community.getId());
		if (!community.getCode().equals(community2.getCode())) {
			if (communityServiceImpl.selectByCode(community.getCode()).size() > 0) {
				throw new RuntimeException("已存在同编号楼栋");
			}
		}
		return communityServiceImpl.update(community);
	}

}
