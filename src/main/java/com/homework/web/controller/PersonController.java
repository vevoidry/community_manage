package com.homework.web.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
import com.homework.web.pojo.Menu;
import com.homework.web.pojo.Permission;
import com.homework.web.pojo.Person;
import com.homework.web.pojo.Role;
import com.homework.web.pojo.Role_permission;
import com.homework.web.pojo.Unit;
import com.homework.web.pojo.vo.HouseVO;
import com.homework.web.service.impl.CommunityServiceImpl;
import com.homework.web.service.impl.HouseServiceImpl;
import com.homework.web.service.impl.MenuServiceImpl;
import com.homework.web.service.impl.PermissionServiceImpl;
import com.homework.web.service.impl.PersonServiceImpl;
import com.homework.web.service.impl.RoleServiceImpl;
import com.homework.web.service.impl.Role_permissionServiceImpl;
import com.homework.web.service.impl.UnitServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/person")
@Api(tags = "路径共同前缀：/person。主要针对业主表", description = "PersonController")
public class PersonController {
	@Autowired
	PersonServiceImpl personServiceImpl;
	@Autowired
	MenuServiceImpl menuServiceImpl;
	@Autowired
	RoleServiceImpl roleServiceImpl;
	@Autowired
	Role_permissionServiceImpl role_permissionServiceImpl;
	@Autowired
	PermissionServiceImpl permissionServiceImpl;
	@Autowired
	HouseServiceImpl houseServiceImpl;
	@Autowired
	UnitServiceImpl unitServiceImpl;
	@Autowired
	CommunityServiceImpl communityServiceImpl;

	@GetMapping
	@ApiOperation("业主登录后的的主页")
	public String person(Model model) {
		// 获取业主信息
		Subject subject = SecurityUtils.getSubject();
		Person person = (Person) subject.getPrincipal();
		// 获取业主角色的权限
		List<Role_permission> role_permissionList = role_permissionServiceImpl.selectByRole_id(person.getRole_id());
		// 获取有权限的所有菜单
		ArrayList<Integer> menu_idList = new ArrayList<Integer>();
		outter: for (Role_permission role_permission : role_permissionList) {
			Permission permission = permissionServiceImpl.selectById(role_permission.getPermission_id());
			Integer menu_id = permission.getMenu_id();
			for (Integer integer : menu_idList) {
				if (integer == menu_id) {
					continue outter;
				}
			}
			menu_idList.add(menu_id);
		}
		// 将菜单分为父菜单和子菜单
		ArrayList<Menu> parentMenuList = new ArrayList<Menu>();
		ArrayList<Menu> childrenMenuList = new ArrayList<Menu>();
		for (Integer id : menu_idList) {
			Menu menu = menuServiceImpl.selectById(id);
			if (menu.getParent_id() == 0) {
				parentMenuList.add(menu);
			} else {
				childrenMenuList.add(menu);
			}
		}
		// 父菜单排序
		parentMenuList.sort(new Comparator<Menu>() {
			@Override
			public int compare(Menu o1, Menu o2) {
				return o1.getRank() - o2.getRank();
			}
		});
		// 将有权限的子菜单放入有权限的父菜单中
		LinkedHashMap<Menu, List<Menu>> menuMenuListMap = new LinkedHashMap<Menu, List<Menu>>();
		Iterator<Menu> iterator = parentMenuList.iterator();
		while (iterator.hasNext()) {
			Menu menu = iterator.next();
			ArrayList<Menu> childMenu = new ArrayList<Menu>();
			for (Menu menu2 : childrenMenuList) {
				if (menu2.getParent_id() == menu.getId()) {
					childMenu.add(menu2);
				}
			}
			// 菜单排序
			childMenu.sort(new Comparator<Menu>() {
				@Override
				public int compare(Menu o1, Menu o2) {
					return o1.getRank() - o2.getRank();
				}
			});
			menuMenuListMap.put(menu, childMenu);
		}
		model.addAttribute("menuMenuListMap", menuMenuListMap);
		return "index";
	}

	@GetMapping("/main")
	@ApiOperation("档案管理-业主管理：主页")
	public String main() {
		return "menu/person::main";
	}

	@GetMapping("/insert")
	@ApiOperation("档案管理-业主管理：新增业主弹出框")
	public String insert() {
		return "menu/person::insert";
	}

	@GetMapping("/select_div")
	@ApiOperation("档案管理-业主管理：表格")
	public String select_div(Model model, String name, String id_card_number, String phone) {
		name = name.trim();
		id_card_number = id_card_number.trim();
		phone = phone.trim();
		List<Person> personList = new ArrayList<Person>();
		if (name.equals("")) {
			if (id_card_number.equals("")) {
				if (phone.equals("")) {
					personList = personServiceImpl.selectAll();
				} else {
					personList = personServiceImpl.selectByPhone(phone);
				}
			} else {
				if (phone.equals("")) {
					personList = personServiceImpl.selectById_card_number(id_card_number);
				} else {
					personList = personServiceImpl.selectById_card_numberPhone(id_card_number, phone);
				}
			}
		} else {
			if (id_card_number.equals("")) {
				if (phone.equals("")) {
					personList = personServiceImpl.selectByName(name);
				} else {
					personList = personServiceImpl.selectByNamePhone(name, phone);
				}
			} else {
				if (phone.equals("")) {
					personList = personServiceImpl.selectByNameId_card_number(name, id_card_number);
				} else {
					personList = personServiceImpl.selectByNameId_card_numberPhone(name, id_card_number, phone);
				}
			}
		}
		model.addAttribute("personList", personList);
		return "menu/person::select_div";
	}

	@PostMapping
	@ResponseBody
	@ApiOperation("档案管理-业主管理：进行一次新增业主的操作")
	public Person post(Person person) {
		Role role = roleServiceImpl.selectByName("业主");
		if (role == null) {
			throw new RuntimeException("请先添加业主角色");
		}
		person.setRole_id(role.getId());
		if (personServiceImpl.selectById_card_number(person.getId_card_number()).size() != 0) {
			throw new RuntimeException("使用该身份证的业主已存在");
		}
		if (personServiceImpl.selectByPhone(person.getPhone()).size() != 0) {
			throw new RuntimeException("使用该手机号的业主已存在");
		}
		if (person.getImage().equals("")) {
			throw new RuntimeException("头像不可为空");
		}
		return personServiceImpl.insert(person);
	}

	@GetMapping("/update/{id:[0-9]+}")
	@ApiOperation("档案管理-业主管理：获取更新业主的弹出框")
	public String update(Model model, @PathVariable Integer id) {
		Person person = personServiceImpl.selectById(id);
		model.addAttribute("person", person);
		return "menu/person::update";
	}

	@PostMapping("/update")
	@ResponseBody
	@ApiOperation("档案管理-业主管理：进行一次更新业主操作")
	public Person updatePerson(Person person) {
		Person person2 = personServiceImpl.selectById(person.getId());
		if (!person.getId_card_number().equals(person2.getId_card_number())) {
			if (personServiceImpl.selectById_card_number(person.getId_card_number()).size() > 0) {
				throw new RuntimeException("已存在同身份证业主");
			}
		}
		if (!person.getPhone().equals(person2.getPhone())) {
			if (personServiceImpl.selectByPhone(person.getPhone()).size() > 0) {
				throw new RuntimeException("已存在同手机号业主");
			}
		}
		person.setRole_id(person2.getRole_id());
		person.setImage(person2.getImage());
		return personServiceImpl.update(person);
	}

	@GetMapping("/update/{id:[0-9]+}/house")
	@ApiOperation("档案管理-业主管理：获取绑定/解绑房屋的弹出框")
	public String updateHouse(Model model, @PathVariable Integer id) {
		Person person = personServiceImpl.selectById(id);
		// 获取用户拥有的所有房屋
		List<House> houseList = houseServiceImpl.selectByPerson_id(person.getId());
		ArrayList<HouseVO> houseList2 = new ArrayList<HouseVO>();
		for (House house : houseList) {
			HouseVO houseVO = new HouseVO();
			BeanUtils.copyProperties(house, houseVO);// 复制同名属性
			if (house.getPerson_id() != null) {
				houseVO.setPerson_name(personServiceImpl.selectById(house.getPerson_id()).getName());
			}
			Unit unit = unitServiceImpl.selectById(house.getUnit_id());
			houseVO.setUnit_code(unit.getCode());
			Community community = communityServiceImpl.selectById(unit.getCommunity_id());
			houseVO.setCommunity_code(community.getCode());
			houseList2.add(houseVO);
		}
		// 获取所有无主房屋
		List<House> houseList3 = houseServiceImpl.selectByPerson_idIsNull();
		ArrayList<HouseVO> houseList4 = new ArrayList<HouseVO>();
		for (House house : houseList3) {
			HouseVO houseVO = new HouseVO();
			BeanUtils.copyProperties(house, houseVO);// 复制同名属性
			if (house.getPerson_id() != null) {
				houseVO.setPerson_name(personServiceImpl.selectById(house.getPerson_id()).getName());
			}
			Unit unit = unitServiceImpl.selectById(house.getUnit_id());
			houseVO.setUnit_code(unit.getCode());
			Community community = communityServiceImpl.selectById(unit.getCommunity_id());
			houseVO.setCommunity_code(community.getCode());
			houseList4.add(houseVO);
		}
		model.addAttribute("person", person);
		model.addAttribute("houseList2", houseList2);
		model.addAttribute("houseList4", houseList4);
		return "menu/person::update_house";
	}

	@SuppressWarnings("deprecation")
	@PostMapping("/{person_id:[0-9]+}/house/{house_id:[0-9]+}/bind")
	@ResponseBody
	@ApiOperation("档案管理-业主管理：绑定一个房屋")
	public House bind(@PathVariable Integer person_id, @PathVariable Integer house_id) {
		House house = houseServiceImpl.selectById(house_id);
		if (house.getPerson_id() != null) {
			throw new RuntimeException("该房屋已经有业主了，不可被绑定");
		}
		house.setPerson_id(person_id);
		Date date = new Date();
		house.setStart_time(date);
		Date date2 = new Date();
		date2.setYear(date2.getYear() + 70);
		house.setEnd_time(date2);
		house.setStatus("已出售");
		return houseServiceImpl.update(house);
	}

	@PostMapping("/house/{house_id:[0-9]+}/unbind")
	@ResponseBody
	@ApiOperation("档案管理-业主管理：与一个房屋解绑")
	public House unbind(@PathVariable Integer house_id) {
		House house = houseServiceImpl.selectById(house_id);
		if (house.getPerson_id() == null) {
			throw new RuntimeException("该房屋还没有业主，不可解绑");
		}
		house.setPerson_id(null);
		house.setStart_time(null);
		house.setEnd_time(null);
		house.setStatus("空闲");
		return houseServiceImpl.update(house);
	}

	@GetMapping("/phoneOrIDNumber")
	public String phoneOrIDNumber(String data, Model model) {
		List<Person> personList = personServiceImpl.selectByLikePhoneOrId_card_number(data);
		model.addAttribute("personList2", personList);
		System.out.println(personList.size());
		return "menu/seat::select_search";
	}

}
