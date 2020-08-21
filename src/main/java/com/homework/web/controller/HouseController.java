package com.homework.web.controller;

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

import com.homework.web.pojo.Community;
import com.homework.web.pojo.House;
import com.homework.web.pojo.Person;
import com.homework.web.pojo.Project;
import com.homework.web.pojo.Unit;
import com.homework.web.pojo.vo.HouseVO;
import com.homework.web.service.impl.CommunityServiceImpl;
import com.homework.web.service.impl.HouseServiceImpl;
import com.homework.web.service.impl.PersonServiceImpl;
import com.homework.web.service.impl.ProjectServiceImpl;
import com.homework.web.service.impl.UnitServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/house")
@Api(tags = "路径共同前缀：/house。主要针对房屋表", description = "HouseController")
public class HouseController {
	@Autowired
	HouseServiceImpl houseServiceImpl;
	@Autowired
	UnitServiceImpl unitServiceImpl;
	@Autowired
	CommunityServiceImpl communityServiceImpl;
	@Autowired
	PersonServiceImpl personServiceImpl;
	@Autowired
	ProjectServiceImpl projectServiceImpl;

	@GetMapping("/main")
	@ApiOperation("档案管理-房屋管理：主页")
	public String main(Model model) {
		List<Community> communityList = communityServiceImpl.selectAll();
		model.addAttribute("communityList", communityList);
		return "menu/house::main";
	}

	@GetMapping("/select_unit/{id:[0-9]+}")//正则表达式，用于字符串匹配
	@ApiOperation("档案管理-房屋管理：根据楼栋id刷新单元的单选框")
	public String select_unit(Model model, @PathVariable Integer id) {
		List<Unit> unitList = unitServiceImpl.selectByCommunity_id(id);
		model.addAttribute("unitList", unitList);
		return "menu/house::select_unit";
	}

	@GetMapping("/insert")
	@ApiOperation("档案管理-房屋管理：新增房屋的弹出框")
	public String insert(Model model) {
		List<Community> communityList = communityServiceImpl.selectAll();
		model.addAttribute("communityList", communityList);
		return "menu/house::insert";
	}

	@GetMapping("/select_div")
	@ApiOperation("档案管理-房屋管理：表格")
	public String select_div(Model model, String community_id, String unit_id, String code) {
		List<House> houseList = new ArrayList<House>();
		if (community_id.equals("")) {
			if (unit_id.equals("")) {
				if (code.equals("")) {
					houseList = houseServiceImpl.selectAll();
				} else {
					houseList = houseServiceImpl.selectByCode(Integer.parseInt(code));
				}
			} else {
				if (code.equals("")) {
					houseList = houseServiceImpl.selectByUnit_id(Integer.parseInt(unit_id));
				} else {
					houseList = houseServiceImpl.selectByUnit_idCode(Integer.parseInt(unit_id), Integer.parseInt(code));
				}
			}
		} else {
			if (unit_id.equals("")) {
				List<Unit> unitList = unitServiceImpl.selectByCommunity_id(Integer.parseInt(community_id));
				for (Unit unit : unitList) {
					if (code.equals("")) {
						List<House> houseList2 = houseServiceImpl.selectByUnit_id(unit.getId());
						houseList.addAll(houseList2);
					} else {
						List<House> houseList2 = houseServiceImpl.selectByUnit_idCode(unit.getId(),
								Integer.parseInt(code));
						houseList.addAll(houseList2);
					}
				}
			} else {
				if (code.equals("")) {
					houseList = houseServiceImpl.selectByUnit_id(Integer.parseInt(unit_id));
				} else {
					houseList = houseServiceImpl.selectByUnit_idCode(Integer.parseInt(unit_id), Integer.parseInt(code));
				}
			}
		}
		ArrayList<HouseVO> houseList2 = new ArrayList<HouseVO>();
		for (House house : houseList) {
			HouseVO houseVO = new HouseVO();
			BeanUtils.copyProperties(house, houseVO);// 复制同名属性
			if (house.getPerson_id() != null) {
				Person person = personServiceImpl.selectById(house.getPerson_id());
				houseVO.setPerson_name(person.getName());
			}
			Unit unit = unitServiceImpl.selectById(house.getUnit_id());
			houseVO.setUnit_code(unit.getCode());
			Community community = communityServiceImpl.selectById(unit.getCommunity_id());
			houseVO.setCommunity_code(community.getCode());
			houseList2.add(houseVO);
		}
		model.addAttribute("houseList", houseList2);
		return "menu/house::select_div";
	}

	@PostMapping
	@ResponseBody
	@ApiOperation("档案管理-房屋管理：进行新增房屋的操作")
	public House post(House house) {
		if (house.getUnit_id() == null || house.getUnit_id() == 0) {
			throw new RuntimeException("请选择一个单元");
		}
		if (house.getCode() == null) {
			throw new RuntimeException("请输入房屋编号");
		}
		if (house.getFloor() == null) {
			throw new RuntimeException("请输入房屋楼层");
		}
		if (house.getArea() == null) {
			throw new RuntimeException("请输入房屋面积");
		}
		if (house.getPrice() == null) {
			throw new RuntimeException("请输入房屋单价");
		}
		house.setRemark(house.getRemark().trim());
		if (house.getRemark().equals("")) {
			throw new RuntimeException("请输入房屋备注");
		}
		if (houseServiceImpl.selectByUnit_idCode(house.getUnit_id(), house.getCode()).size() != 0) {
			throw new RuntimeException("该单元已存在该编号的房屋");
		}
		house.setStatus("空闲");
		return houseServiceImpl.insert(house);
	}

	@GetMapping("/update/{id:[0-9]+}")
	@ApiOperation("档案管理-房屋管理：获取更新房屋的弹出框")
	public String update(Model model, @PathVariable Integer id) {
		House house = houseServiceImpl.selectById(id);
		List<Person> personList = personServiceImpl.selectAll();
		List<Community> communityList = communityServiceImpl.selectAll();
		model.addAttribute("house", house);
		model.addAttribute("personList", personList);
		model.addAttribute("communityList", communityList);
		return "menu/house::update";
	}

	@SuppressWarnings("deprecation")
	@PostMapping("/update")
	@ResponseBody
	@ApiOperation("档案管理-房屋管理：进行更新房屋的操作")
	public House updateHouse(House house) {
		if (house.getUnit_id() == null) {
			throw new RuntimeException("请选择单元");
		}
		House house2 = houseServiceImpl.selectById(house.getId());
		if (!(house.getUnit_id().equals(house2.getUnit_id()) && house.getCode().equals(house2.getCode()))) {
			if (houseServiceImpl.selectByUnit_idCode(house.getUnit_id(), house.getCode()).size() > 0) {
				throw new RuntimeException("同单元下不可存在同编号的房屋");
			}
		}
		if (!house.getPerson_id().equals(house2.getPerson_id())) {
			if (house.getPerson_id() == 0) {
				house2.setPerson_id(null);
				house2.setStart_time(null);
				house2.setEnd_time(null);
				house2.setStatus("空闲");
			} else {
				house2.setPerson_id(house.getPerson_id());
				Date date = new Date();
				house2.setStart_time(date);
				Date date2 = new Date();
				date2.setYear(date2.getYear() + 70);
				house2.setEnd_time(date2);
				house2.setStatus("已出售");
			}
		}
		house2.setArea(house.getArea());
		house2.setCode(house.getCode());
		house2.setFloor(house.getFloor());
		house2.setPrice(house.getPrice());
		house2.setRemark(house.getRemark());
		house2.setType_hall(house.getType_hall());
		house2.setType_room(house.getType_room());
		house2.setUnit_id(house.getUnit_id());
		return houseServiceImpl.update(house2);
	}

	@GetMapping("/{house_id:[0-9]+}/water_fee")
	@ApiOperation("档案管理-房屋管理：获取收水费的弹出框")
	public String water_fee(Model model, @PathVariable Integer house_id) {
		List<Project> projectList = projectServiceImpl.selectAll();
		model.addAttribute("house_id", house_id);
		model.addAttribute("projectList", projectList);
		return "menu/house::water_fee_div";
	}
}
