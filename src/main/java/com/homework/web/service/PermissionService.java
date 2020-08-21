package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Permission;

public interface PermissionService {
	Permission insert(Permission permission);

	List<Permission> selectAll();

	List<Permission> selectByMenu_id(Integer menu_id);

	Permission selectByMenu_idName(Integer menu_id, String name);

	Permission selectById(Integer id);
}
