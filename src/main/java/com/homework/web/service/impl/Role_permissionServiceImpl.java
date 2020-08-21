package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Role_permission;
import com.homework.web.repository.Role_permissionRepository;
import com.homework.web.service.Role_permissionService;

@Service
public class Role_permissionServiceImpl implements Role_permissionService {

	@Autowired
	Role_permissionRepository role_permissionRepository;

	@Override
	public List<Role_permission> selectByRole_id(Integer role_id) {
		return role_permissionRepository.selectByRole_id(role_id);
	}

	@Override
	public void deleteByRole_id(Integer role_id) {
		role_permissionRepository.deleteByRole_id(role_id);
	}

	@Override
	public Role_permission insert(Role_permission role_permission) {
		return role_permissionRepository.save(role_permission);
	}

	@Override
	public Role_permission selectById(Integer id) {
		return role_permissionRepository.findById(id).get();
	}

}
