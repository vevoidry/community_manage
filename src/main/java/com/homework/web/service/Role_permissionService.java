package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Role_permission;

public interface Role_permissionService {
	List<Role_permission> selectByRole_id(Integer role_id);

	void deleteByRole_id(Integer role_id);

	Role_permission insert(Role_permission role_permission);
	
	Role_permission selectById(Integer id);
}
