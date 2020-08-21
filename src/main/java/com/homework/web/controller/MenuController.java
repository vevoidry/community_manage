package com.homework.web.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.web.pojo.Menu;
import com.homework.web.pojo.vo.MenuVO;
import com.homework.web.service.impl.CommunityServiceImpl;
import com.homework.web.service.impl.MenuServiceImpl;
import com.homework.web.service.impl.PermissionServiceImpl;
import com.homework.web.service.impl.PersonServiceImpl;
import com.homework.web.service.impl.RoleServiceImpl;
import com.homework.web.service.impl.Role_permissionServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/menu")
@Api(tags = "路径共同前缀：/menu。主要针对任何菜单表", description = "MenuController")
public class MenuController {

	@Autowired
	MenuServiceImpl menuServiceImpl;
	@Autowired
	CommunityServiceImpl communityServiceImpl;
	@Autowired
	PersonServiceImpl personServiceImpl;
	@Autowired
	RoleServiceImpl roleServiceImpl;
	@Autowired
	Role_permissionServiceImpl role_permissionServiceImpl;
	@Autowired
	PermissionServiceImpl permissionServiceImpl;

	@GetMapping("/main")
	@ApiOperation("系统管理-菜单管理：主页")
	public String menu(Model model, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer size) {
		LinkedHashMap<MenuVO, List<MenuVO>> menuMenuListMap = new LinkedHashMap<MenuVO, List<MenuVO>>();
		// 获取所有父菜单
		List<Menu> menuList = menuServiceImpl.selectByParent_idRank(0);
		// 循环父菜单获取相应的子菜单
		for (Menu menu : menuList) {
			MenuVO menuVO2 = new MenuVO();
			BeanUtils.copyProperties(menu, menuVO2);
			menuVO2.setParent_name("无");
			menuVO2.setPage_name("无");
			ArrayList<MenuVO> menuList3 = new ArrayList<MenuVO>();
			List<Menu> menuList2 = menuServiceImpl.selectByParent_idRank(menu.getId());
			for (Menu menu2 : menuList2) {
				MenuVO menuVO = new MenuVO();
				BeanUtils.copyProperties(menu2, menuVO);
				menuVO.setParent_name(menu.getName());
				menuList3.add(menuVO);
			}
			menuMenuListMap.put(menuVO2, menuList3);
		}
//		// 分页
//		Set<MenuVO> keySet = menuMenuListMap.keySet();
//		int size2 = keySet.size();
//		int pageSize = (size2 - 1) / size + 1;
//		ArrayList<Integer> pageList = new ArrayList<Integer>();
//		for (int i = 1; i <= pageSize; i++) {
//			pageList.add(i);
//		}
//		LinkedHashMap<MenuVO, List<MenuVO>> menuMenuListMap2 = new LinkedHashMap<MenuVO, List<MenuVO>>();
//		Iterator<MenuVO> iterator = keySet.iterator();
//		int i = 1;
//		while (iterator.hasNext()) {
//			MenuVO next = iterator.next();
//			if (i > (page - 1) * size && i <= page * size) {
//				List<MenuVO> list = menuMenuListMap.get(next);
//				menuMenuListMap2.put(next, list);
//			}
//			i++;
//		}
		model.addAttribute("menuMenuListMap", menuMenuListMap);
		return "menu/menu::main";
	}

	@GetMapping("/insert1")
	@ApiOperation("系统管理-菜单管理：新增父菜单的弹出框")
	public String menu_insert1(Model model) {
		return "menu/menu::insert1";
	}

	@GetMapping("/insert2")
	@ApiOperation("系统管理-菜单管理：新增子菜单的弹出框")
	public String menu_insert2(Model model) {
		// 获取所有父菜单
		List<Menu> menuList = menuServiceImpl.selectByParent_idRank(0);
		model.addAttribute("menuList", menuList);
		return "menu/menu::insert2";
	}

	@PostMapping
	@ResponseBody
	@ApiOperation("系统管理-菜单管理：进行一个新增菜单的操作")
	public Menu post(Menu menu) {
		// 判断是否传入可用值，若无则设置其为false，否则设置为true
		if (menu.getIs_using() == null) {
			menu.setIs_using(false);
		} else {
			menu.setIs_using(true);
		}
		// 判断是否存在同父id且同名的菜单
		if (menuServiceImpl.selectByParent_idName(menu.getParent_id(), menu.getName()) != null) {
			throw new RuntimeException("不可添加同名的同类菜单");
		}
		return menuServiceImpl.insert(menu);
	}

	// 获取一个子菜单的主页面
	@GetMapping("/{id:[0-9]+}") // 正则表达式，匹配字符串
	@ApiOperation("点击任意子菜单实现局部刷新")
	public String get(@PathVariable Integer id, Model model) {
		Menu menu = menuServiceImpl.selectById(id);
		return "menu/" + menu.getPage_name() + "::index";
	}

}
