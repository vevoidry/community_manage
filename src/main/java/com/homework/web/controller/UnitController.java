package com.homework.web.controller;

import java.util.ArrayList;
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

import com.homework.web.pojo.Community;
import com.homework.web.pojo.Unit;
import com.homework.web.pojo.vo.UnitVO;
import com.homework.web.service.impl.CommunityServiceImpl;
import com.homework.web.service.impl.UnitServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/unit")
@Api(tags = "路径共同前缀：/unit。主要针对单元表", description = "UnitController")
public class UnitController {
	@Autowired
	UnitServiceImpl unitServiceImpl;
	@Autowired
	CommunityServiceImpl communityServiceImpl;

	@GetMapping("/main")
	@ApiOperation("档案管理-单元管理：主页")
	public String main(Model model) {
		List<Community> communityList = communityServiceImpl.selectAll();
		model.addAttribute("communityList", communityList);
		return "menu/unit::main";
	}

	@GetMapping("/select")
	@ApiOperation("档案管理-单元管理：表格")
	public String select(Model model, String community_id, String code, String floor) {
		List<Unit> unitList = new ArrayList<Unit>();
		if (community_id.equals("")) {
			if (code.equals("")) {
				if (floor.equals("")) {
					unitList = unitServiceImpl.selectAll();
				} else {
					unitList = unitServiceImpl.selectByFloor(Integer.parseInt(floor));
				}
			} else {
				if (floor.equals("")) {
					unitList = unitServiceImpl.selectByCode(Integer.parseInt(code));
				} else {
					unitList = unitServiceImpl.selectByCodeFloor(Integer.parseInt(code), Integer.parseInt(floor));
				}
			}
		} else {
			if (code.equals("")) {
				if (floor.equals("")) {
					unitList = unitServiceImpl.selectByCommunity_id(Integer.parseInt(community_id));
				} else {
					unitList = unitServiceImpl.selectByCommunity_idFloor(Integer.parseInt(community_id),
							Integer.parseInt(floor));
				}
			} else {
				if (floor.equals("")) {
					unitList = unitServiceImpl.selectByCommunity_idCode(Integer.parseInt(community_id),
							Integer.parseInt(code));
				} else {
					unitList = unitServiceImpl.selectByCommunity_idCodeFloor(Integer.parseInt(community_id),
							Integer.parseInt(code), Integer.parseInt(floor));
				}
			}
		}
		ArrayList<UnitVO> unitList2 = new ArrayList<UnitVO>();
		for (Unit unit : unitList) {
			UnitVO unitVO = new UnitVO();
			BeanUtils.copyProperties(unit, unitVO);
			Community community = communityServiceImpl.selectById(unit.getCommunity_id());
			unitVO.setCommunity_code(community.getCode());
			unitList2.add(unitVO);
		}
		model.addAttribute("unitList", unitList2);
		return "menu/unit::select_div";
	}

	@GetMapping("/insert")
	@ApiOperation("档案管理-单元管理：新增单元的弹出框")
	public String insert(Model model) {
		List<Community> communityList = communityServiceImpl.selectAll();
		model.addAttribute("communityList", communityList);
		return "menu/unit::insert";
	}

	@PostMapping
	@ResponseBody
	@ApiOperation("档案管理-单元管理：进行一次新增单元的操作")
	public Unit post(Unit unit) {
		if (unit.getHas_elevator() == null) {
			unit.setHas_elevator(false);
		} else {
			unit.setHas_elevator(true);
		}
		if (unitServiceImpl.selectByCommunity_idCode(unit.getCommunity_id(), unit.getCode()).size() != 0) {
			throw new RuntimeException("该楼栋已存在该编号单元");
		}
		return unitServiceImpl.insert(unit);
	}

	@GetMapping("/update/{id:[0-9]+}")
	@ApiOperation("档案管理-单元管理：更新一个单元的弹出框")
	public String update(Model model, @PathVariable Integer id) {
		Unit unit = unitServiceImpl.selectById(id);
		List<Community> communityList = communityServiceImpl.selectAll();
		model.addAttribute("unit", unit);
		model.addAttribute("communityList", communityList);
		return "menu/unit::update";
	}

	@PostMapping("/update")
	@ResponseBody
	@ApiOperation("档案管理-单元管理：进行一次更新单元的操作")
	public Unit updateUnit(Unit unit) {
		if (unit.getHas_elevator() == null) {
			unit.setHas_elevator(false);
		} else {
			unit.setHas_elevator(true);
		}
		// 同楼栋下不可存在同编号单元
		Unit unit2 = unitServiceImpl.selectById(unit.getId());
		if (!(unit.getCommunity_id().equals(unit2.getCommunity_id()) && unit.getCode().equals(unit2.getCode()))) {
			if (unitServiceImpl.selectByCommunity_idCode(unit.getCommunity_id(), unit.getCode()).size() > 0) {
				throw new RuntimeException("同楼栋下不可存在同编号的单元");
			}
		}
		return unitServiceImpl.update(unit);
	}

}
