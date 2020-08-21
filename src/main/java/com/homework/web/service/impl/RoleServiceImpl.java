package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Role;
import com.homework.web.repository.RoleRepository;
import com.homework.web.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;

	@Override
	public Role insert(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public List<Role> selectAll() {
		return roleRepository.findAll();
	}

	@Override
	public Role selectByName(String name) {
		return roleRepository.selectByName(name);
	}

	@Override
	public Role selectById(Integer id) {
		return roleRepository.findById(id).get();
	}

}
