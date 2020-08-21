package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Permission;
import com.homework.web.repository.PermissionRepository;
import com.homework.web.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	PermissionRepository permissionRepository;

	@Override
	public Permission insert(Permission permission) {
		return permissionRepository.save(permission);
	}

	@Override
	public List<Permission> selectAll() {
		return permissionRepository.findAll();
	}

	@Override
	public List<Permission> selectByMenu_id(Integer menu_id) {
		return permissionRepository.selectByMenu_id(menu_id);
	}

	@Override
	public Permission selectByMenu_idName(Integer menu_id, String name) {
		return permissionRepository.selectByMenu_idName(menu_id, name);
	}

	@Override
	public Permission selectById(Integer id) {
		return permissionRepository.findById(id).get();
	}

}
