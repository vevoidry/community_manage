package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Menu;
import com.homework.web.repository.MenuRepository;
import com.homework.web.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	MenuRepository menuRepository;

	@Override
	public Menu insert(Menu menu) {
		return menuRepository.save(menu);
	}

	@Override
	public Menu selectByParent_idName(Integer parent_id, String name) {
		return menuRepository.selectByParent_idName(parent_id, name);
	}

	@Override
	public List<Menu> selectAll() {
		return menuRepository.findAll();
	}

	@Override
	public Menu selectById(Integer id) {
		return menuRepository.findById(id).get();
	}

//	@Override
//	public List<Menu> selectByParent_id(Integer parent_id) {
//		return menuRepository.selectByParent_id(parent_id);
//	}

	@Override
	public List<Menu> selectByParent_idRank(Integer parent_id) {
		return menuRepository.selectByParent_idRank(parent_id);
	}

}
