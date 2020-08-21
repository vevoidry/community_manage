package com.homework.web.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.web.pojo.Menu;
import com.homework.web.pojo.Permission;
import com.homework.web.pojo.Role;
import com.homework.web.pojo.Role_permission;
import com.homework.web.pojo.User;
import com.homework.web.service.impl.MenuServiceImpl;
import com.homework.web.service.impl.PermissionServiceImpl;
import com.homework.web.service.impl.RoleServiceImpl;
import com.homework.web.service.impl.Role_permissionServiceImpl;
import com.homework.web.service.impl.UserServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/user")
@Api(tags = "路径共同前缀：/user。主要针对用户表", description = "UserController")
public class UserController {
	@Autowired
	UserServiceImpl userServiceImpl;
	@Autowired
	RoleServiceImpl roleServiceImpl;
	@Autowired
	MenuServiceImpl menuServiceImpl;
	@Autowired
	Role_permissionServiceImpl role_permissionServiceImpl;
	@Autowired
	PermissionServiceImpl permissionServiceImpl;

	@GetMapping
	@ApiOperation("管理员/维修员登录后的主页")
	public String user(Model model) {
		// 获取业主信息
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		// 获取业主角色的权限
		List<Role_permission> role_permissionList = role_permissionServiceImpl.selectByRole_id(user.getRole_id());
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
	@ApiOperation("系统管理-用户管理：主页")
	public String main(Model model) {
		LinkedHashMap<User, Role> userRoleMap = new LinkedHashMap<User, Role>();
		// 获取所有用户
		List<User> userList = userServiceImpl.selectAll();
		Iterator<User> iterator = userList.iterator();
		// 获取每个用户的角色信息
		while (iterator.hasNext()) {
			User user = iterator.next();
			Role role = roleServiceImpl.selectById(user.getRole_id());
			userRoleMap.put(user, role);
		}
		model.addAttribute("userRoleMap", userRoleMap);
		return "menu/user::main";
	}

	@GetMapping("/insert")
	@ApiOperation("系统管理-用户管理：新增用户的弹出框")
	public String insert(Model model) {
		// 获取所有角色
		List<Role> roleList = roleServiceImpl.selectAll();
		model.addAttribute("roleList", roleList);
		return "menu/user::insert";
	}

	@PostMapping
	@ResponseBody
	@ApiOperation("系统管理-用户管理：进行一次新增用户的操作")
	public User post(User user) {
		if (user.getIs_using() == null) {
			user.setIs_using(false);
		} else {
			user.setIs_using(true);
		}
		if (user.getRole_id() == 0) {
			throw new RuntimeException("角色不可为空");
		}
		// 判断用户名是否已被使用
		if (userServiceImpl.selectByUsername(user.getUsername()) != null) {
			throw new RuntimeException("该用户名已被使用");
		}
		// 判断手机是否已被使用
		if (userServiceImpl.selectByPhone(user.getPhone()) != null) {
			throw new RuntimeException("该手机已被使用");
		}
		// 判断邮箱是否已被使用
		if (userServiceImpl.selectByEmail(user.getEmail()) != null) {
			throw new RuntimeException("该邮箱已被使用");
		}
		user.setInsert_date(new Date());
		return userServiceImpl.insert(user);
	}

}
