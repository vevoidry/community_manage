package com.homework.web.controller;

import java.util.LinkedHashMap;
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

import com.homework.web.pojo.Menu;
import com.homework.web.pojo.Permission;
import com.homework.web.pojo.vo.MenuVO;
import com.homework.web.service.impl.MenuServiceImpl;
import com.homework.web.service.impl.PermissionServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/permission")
@Api(tags = "路径共同前缀：/permission。主要针对权限表", description = "PermissionController")
public class PermissionController {
	@Autowired
	PermissionServiceImpl permissionServiceImpl;
	@Autowired
	MenuServiceImpl menuServiceImpl;

	@GetMapping("/main")
	@ApiOperation("系统管理-权限管理：主页")
	public String permission(Model model) {
		LinkedHashMap<MenuVO, List<Permission>> menuPermissionListMap = new LinkedHashMap<MenuVO, List<Permission>>();
		// 获取所有菜单
		List<Menu> menuList = menuServiceImpl.selectAll();
		// 遍历每一个菜单，获取每个菜单的权限
		for (Menu menu : menuList) {
			MenuVO menuVO = new MenuVO();
			BeanUtils.copyProperties(menu, menuVO);
			if (menu.getParent_id() == 0) {
				menuVO.setParent_name("无");
				menuVO.setPage_name("无");
			} else {
				Menu menu2 = menuServiceImpl.selectById(menu.getParent_id());
				menuVO.setParent_name(menu2.getName());
			}
			List<Permission> permissionList = permissionServiceImpl.selectByMenu_id(menu.getId());
			menuPermissionListMap.put(menuVO, permissionList);
		}
		model.addAttribute("menuPermissionListMap", menuPermissionListMap);
		return "menu/permission::main";
	}

	@GetMapping("/{id:[0-9]+}/insert")
	@ApiOperation("系统管理-权限管理：获取新增权限的弹出框")
	public String permission_insert(@PathVariable Integer id, Model model) {
		Menu menu = menuServiceImpl.selectById(id);
		model.addAttribute("menu", menu);
		return "menu/permission::insert";
	}

	@PostMapping
	@ResponseBody
	@ApiOperation("系统管理-权限管理：进行一次新增权限的操作")
	public Permission insert(Permission permission) {
		if (permissionServiceImpl.selectByMenu_idName(permission.getMenu_id(), permission.getName()) != null) {
			throw new RuntimeException("该菜单已存在同名权限");
		}
		return permissionServiceImpl.insert(permission);
	}
}
