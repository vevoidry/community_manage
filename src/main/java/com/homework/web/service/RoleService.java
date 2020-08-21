package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Role;

public interface RoleService {

	Role insert(Role role);

	List<Role> selectAll();

	Role selectByName(String name);
	
	Role selectById(Integer id);
}
