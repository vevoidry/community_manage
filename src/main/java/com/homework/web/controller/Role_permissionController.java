package com.homework.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.web.pojo.Role_permission;
import com.homework.web.service.impl.Role_permissionServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/role_permission")
@Api(tags = "路径共同前缀：/role_permission。主要针对角色-权限关联表", description = "Role_permissionController")
public class Role_permissionController {
	@Autowired
	Role_permissionServiceImpl role_permissionServiceImpl;

	@PostMapping
	@ResponseBody
	@Transactional
	@ApiOperation("进行角色-权限关联操作")
	public List<Role_permission> insert(Integer role_id, String[] permissions) {
		ArrayList<Role_permission> role_permissionList = new ArrayList<Role_permission>();
		Integer[] permissions2 = new Integer[permissions.length];
		for (int i = 0; i < permissions.length; i++) {
			permissions2[i] = Integer.parseInt(permissions[i]);
		}
		// 删除该角色的所有权限
		role_permissionServiceImpl.deleteByRole_id(role_id);
		// 为该角色添加现有权限
		for (int i = 0; i < permissions2.length; i++) {
			Role_permission role_permission = new Role_permission();
			role_permission.setRole_id(role_id);
			role_permission.setPermission_id(permissions2[i]);
			Role_permission role_permission2 = role_permissionServiceImpl.insert(role_permission);
			role_permissionList.add(role_permission2);
		}
		return role_permissionList;
	}
}
