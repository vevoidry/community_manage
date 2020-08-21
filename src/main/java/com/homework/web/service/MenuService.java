package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Menu;

public interface MenuService {
	Menu insert(Menu menu);

	Menu selectByParent_idName(Integer parent_id, String name);

	List<Menu> selectAll();

	Menu selectById(Integer id);

//	List<Menu> selectByParent_id(Integer parent_id);
	
	List<Menu> selectByParent_idRank(Integer parent_id);
}
