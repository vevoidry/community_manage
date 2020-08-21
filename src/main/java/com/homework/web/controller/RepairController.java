package com.homework.web.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.homework.web.pojo.Person;
import com.homework.web.pojo.Repair;
import com.homework.web.pojo.Role;
import com.homework.web.pojo.User;
import com.homework.web.pojo.vo.RepairVO;
import com.homework.web.service.impl.PersonServiceImpl;
import com.homework.web.service.impl.RepairServiceImpl;
import com.homework.web.service.impl.RoleServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/repair")
@Api(tags = "路径共同前缀：/repair。主要针对维修记录表", description = "RepairController")
public class RepairController {

	@Autowired
	RepairServiceImpl repairServiceImpl;
	@Autowired
	RoleServiceImpl roleServiceImpl;
	@Autowired
	UserServiceImpl userServiceImpl;
	@Autowired
	PersonServiceImpl personServiceImpl;

	@GetMapping("/person/main")
	@ApiOperation("管理服务-维修记录（业主）：主页")
	public String main() {
		return "menu/repair_person::main";
	}

	@GetMapping("/person/insert")
	@ApiOperation("管理服务-维修记录（业主）：新增维修记录弹出框")
	public String insert() {
		return "menu/repair_person::insert";
	}

	@GetMapping("/person/select_div")
	@ApiOperation("管理服务-维修记录（业主）：表格")
	public String select_div(Model model, String is_handle) {
		// 获取业主信息
		Person person = (Person) SecurityUtils.getSubject().getPrincipal();
		List<Repair> repairList = new ArrayList<Repair>();
		if (is_handle.equals("true")) {
			repairList = repairServiceImpl.selectByPerson_idIs_handle(person.getId(), true);
		} else if (is_handle.equals("false")) {
			repairList = repairServiceImpl.selectByPerson_idIs_handle(person.getId(), false);
		} else {
			repairList = repairServiceImpl.selectByPerson_id(person.getId());
		}
		ArrayList<RepairVO> repairList2 = new ArrayList<RepairVO>();
		for (Repair repair : repairList) {
			RepairVO repairVO = new RepairVO();
			BeanUtils.copyProperties(repair, repairVO);
			Person person2 = personServiceImpl.selectById(repair.getPerson_id());
			repairVO.setPerson_name(person2.getName());
			if (repair.getUser_id() == null) {
				repairVO.setUser_name("无");
			} else {
				User user = userServiceImpl.selectById(repair.getUser_id());
				repairVO.setUser_name(user.getNickname());
			}
			repairList2.add(repairVO);
		}
		model.addAttribute("repairList", repairList2);
		return "menu/repair_person::select_div";
	}

	@PostMapping("/person")
	@ResponseBody
	@ApiOperation("管理服务-维修记录（业主）：进行一次新增维修记录的操作")
	public Repair post(Repair repair) {
		// 获取业主信息
		Person person = (Person) SecurityUtils.getSubject().getPrincipal();
		repair.setIs_handle(false);
		repair.setStart_time(new Date());
		repair.setPerson_id(person.getId());
		return repairServiceImpl.insert(repair);
	}

	@GetMapping("/admin/main")
	@ApiOperation("管理服务-维修记录（管理员）：主页")
	public String admimain() {
		return "menu/repair_admin::main";
	}

	@GetMapping("/admin/select_div")
	@ApiOperation("管理服务-维修记录（管理员）：表格")
	public String adminselect_div(Model model, String is_handle, String has_worker) {
		List<Repair> repairList = new ArrayList<Repair>();
		if (is_handle.equals("true")) {
			if (has_worker.equals("true")) {
				repairList = repairServiceImpl.selectByIs_handleUser_idIsNotNull(true);
			} else if (has_worker.equals("false")) {
				repairList = repairServiceImpl.selectByIs_handleUser_idIsNull(true);
			} else {
				repairList = repairServiceImpl.selectByIs_handle(true);
			}
		} else if (is_handle.equals("false")) {
			if (has_worker.equals("true")) {
				repairList = repairServiceImpl.selectByIs_handleUser_idIsNotNull(false);
			} else if (has_worker.equals("false")) {
				repairList = repairServiceImpl.selectByIs_handleUser_idIsNull(false);
			} else {
				repairList = repairServiceImpl.selectByIs_handle(false);
			}
		} else {
			if (has_worker.equals("true")) {
				repairList = repairServiceImpl.selectByUser_idIsNotNull();
			} else if (has_worker.equals("false")) {
				repairList = repairServiceImpl.selectByUser_idIsNull();
			} else {
				repairList = repairServiceImpl.selectAll();
			}
		}
		ArrayList<RepairVO> repairList2 = new ArrayList<RepairVO>();
		for (Repair repair : repairList) {
			RepairVO repairVO = new RepairVO();
			BeanUtils.copyProperties(repair, repairVO);
			Person person2 = personServiceImpl.selectById(repair.getPerson_id());
			repairVO.setPerson_name(person2.getName());
			repairVO.setPerson_phone(person2.getPhone());
			if (repair.getUser_id() == null) {
				repairVO.setUser_name("无");
			} else {
				User user = userServiceImpl.selectById(repair.getUser_id());
				repairVO.setUser_name(user.getNickname());
			}
			repairList2.add(repairVO);
		}
		model.addAttribute("repairList", repairList2);
		return "menu/repair_admin::select_div";
	}

	@GetMapping("/admin/{id}")
	@ApiOperation("管理服务-维修记录（管理员）：指定维修员的弹出框")
	public String adminselect_div(Model model, @PathVariable Integer id) {
		// 获取所有维修员
		Role role = roleServiceImpl.selectByName("维修员");
		if (role == null) {
			throw new RuntimeException("请先添加维修员角色");
		}
		List<User> userList = userServiceImpl.selectByRole_id(role.getId());
		// 获取维修信息
		Repair repair = repairServiceImpl.selectById(id);
		model.addAttribute("repair", repair);
		model.addAttribute("userList", userList);
		return "menu/repair_admin::update";
	}

	@PostMapping("/admin")
	@ResponseBody
	@ApiOperation("管理服务-维修记录（管理员）：进行一次指定维修员的操作")
	public Repair posst(Integer id, Integer user_id) {
		if (user_id == null) {
			throw new RuntimeException("请指定维修员");
		}
		Repair repair = repairServiceImpl.selectById(id);
		if (repair.getIs_handle()) {
			throw new RuntimeException("已经维修完毕，无法更换维修员");
		}
		repair.setUser_id(user_id);
		return repairServiceImpl.update(repair);
	}

	@GetMapping("/worker/main")
	@ApiOperation("管理服务-维修记录（维修员）：主页")
	public String workermain() {
		return "menu/repair_worker::main";
	}

	@GetMapping("/worker/select_div")
	@ApiOperation("管理服务-维修记录（维修员）：表格")
	public String ss(Model model, String is_handle) {
		// 获取业主信息
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		List<Repair> repairList = new ArrayList<Repair>();
		if (is_handle.equals("true")) {
			repairList = repairServiceImpl.selectByUser_idIs_handle(user.getId(), true);
		} else if (is_handle.equals("false")) {
			repairList = repairServiceImpl.selectByUser_idIs_handle(user.getId(), false);
		} else {
			repairList = repairServiceImpl.selectByUser_id(user.getId());
		}
		ArrayList<RepairVO> repairList2 = new ArrayList<RepairVO>();
		for (Repair repair : repairList) {
			RepairVO repairVO = new RepairVO();
			BeanUtils.copyProperties(repair, repairVO);
			Person person2 = personServiceImpl.selectById(repair.getPerson_id());
			repairVO.setPerson_name(person2.getName());
			repairVO.setPerson_phone(person2.getPhone());
			if (repair.getUser_id() == null) {
				repairVO.setUser_name("无");
			} else {
				User user2 = userServiceImpl.selectById(repair.getUser_id());
				repairVO.setUser_name(user2.getNickname());
			}
			repairList2.add(repairVO);
		}
		model.addAttribute("repairList", repairList2);
		return "menu/repair_worker::select_div";
	}

	@GetMapping("/worker/{id:[0-9]+}")
	@ApiOperation("管理服务-维修记录（维修员）：获取一个进行维修的弹出框")
	public String workermn(@PathVariable Integer id, Model model) {
		Repair repair = repairServiceImpl.selectById(id);
		model.addAttribute("repair", repair);
		return "menu/repair_worker::update";
	}

	@PostMapping("/worker")
	@ResponseBody
	@ApiOperation("管理服务-维修记录（维修员）：进行一次维修操作")
	public Repair psosst(Integer id, Double price, String process, String result) {
		if (price < 0) {
			throw new RuntimeException("价格不可为负数");
		}
		Repair repair = repairServiceImpl.selectById(id);
		if (repair.getIs_handle()) {
			throw new RuntimeException("该维修已经处理过了");
		}
		repair.setPrice(price);
		repair.setProcess(process);
		repair.setResult(result);
		repair.setEnd_time(new Date());
		repair.setIs_handle(true);
		return repairServiceImpl.update(repair);
	}
}
