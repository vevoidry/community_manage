package com.homework.web.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.web.pojo.Permission;
import com.homework.web.pojo.Role;
import com.homework.web.pojo.Role_permission;
import com.homework.web.service.impl.PermissionServiceImpl;
import com.homework.web.service.impl.RoleServiceImpl;
import com.homework.web.service.impl.Role_permissionServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/role")
@Api(tags = "路径共同前缀：/role。主要针对角色表", description = "RoleController")
public class RoleController {

	@Autowired
	RoleServiceImpl roleServiceImpl;
	@Autowired
	PermissionServiceImpl permissionServiceImpl;
	@Autowired
	Role_permissionServiceImpl role_permissionServiceImpl;

	@GetMapping("/main")
	@ApiOperation("系统管理-角色管理：主页")
	public String role(Model model) {
		LinkedHashMap<Role, List<Permission>> rolePermissionListMap = new LinkedHashMap<Role, List<Permission>>();
		// 获取所有角色
		List<Role> roleList = roleServiceImpl.selectAll();
		Iterator<Role> iterator = roleList.iterator();
		// 遍历每个角色获取每个角色的所有权限
		while (iterator.hasNext()) {
			ArrayList<Permission> permissionList = new ArrayList<Permission>();
			Role role = iterator.next();
			List<Role_permission> role_permissionList = role_permissionServiceImpl.selectByRole_id(role.getId());
			Iterator<Role_permission> iterator2 = role_permissionList.iterator();
			while (iterator2.hasNext()) {
				Role_permission role_permission = iterator2.next();
				Permission permission = permissionServiceImpl.selectById(role_permission.getPermission_id());
				permissionList.add(permission);
			}
			rolePermissionListMap.put(role, permissionList);
		}
		model.addAttribute("rolePermissionListMap", rolePermissionListMap);
		return "menu/role::main";
	}

	@GetMapping("/insert")
	@ApiOperation("系统管理-角色管理：新增角色的弹出框")
	public String role_insert(Model model) {
		return "menu/role::insert";
	}

	@PostMapping
	@ResponseBody
	@ApiOperation("系统管理-角色管理：进行一次新增角色的操作")
	public Role insert(Role role) {
		if (roleServiceImpl.selectByName(role.getName()) != null) {
			throw new RuntimeException("不可新增同名角色");
		}
		return roleServiceImpl.insert(role);
	}

	@GetMapping("/{id:[0-9]+}/permission")
	@ApiOperation("系统管理-角色管理：获取一个角色的权限操作的弹出框")
	public String role_permission(Model model, @PathVariable Integer id) {
		ArrayList<Permission> permissionList = new ArrayList<Permission>();
		// 取出该角色所拥有的所有权限
		Role role = roleServiceImpl.selectById(id);
		List<Role_permission> role_permissionList = role_permissionServiceImpl.selectByRole_id(role.getId());
		Iterator<Role_permission> iterator = role_permissionList.iterator();
		while (iterator.hasNext()) {
			Role_permission role_permission = iterator.next();
			Permission permission = permissionServiceImpl.selectById(role_permission.getPermission_id());
			permissionList.add(permission);
		}
		// 取出该角色所未拥有的所有权限
		ArrayList<Permission> permissionList2 = new ArrayList<Permission>();
		List<Permission> permissionList3 = permissionServiceImpl.selectAll();
		Iterator<Permission> iterator2 = permissionList3.iterator();
		outter: while (iterator2.hasNext()) {
			Permission permission = iterator2.next();
			Iterator<Permission> iterator3 = permissionList.iterator();
			while (iterator3.hasNext()) {
				Permission permission2 = iterator3.next();
				if (permission.getId() == permission2.getId()) {
					continue outter;
				}
			}
			permissionList2.add(permission);
		}
		model.addAttribute("id", id);
		model.addAttribute("permissionList", permissionList);
		model.addAttribute("permissionList2", permissionList2);
		return "menu/role::update_permission";
	}

}
