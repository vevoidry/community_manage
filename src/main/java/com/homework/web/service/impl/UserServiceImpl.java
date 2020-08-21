package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.User;
import com.homework.web.repository.UserRepository;
import com.homework.web.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	public User insert(User user) {
		return userRepository.save(user);
	}

	@Override
	public List<User> selectAll() {
		return userRepository.findAll();
	}

	@Override
	public User selectByUsername(String username) {
		return userRepository.selectByUsername(username);
	}

	@Override
	public User selectByPhone(String phone) {
		return userRepository.selectByPhone(phone);
	}

	@Override
	public User selectByEmail(String email) {
		return userRepository.selectByEmail(email);
	}

	@Override
	public List<User> selectByRole_id(Integer role_id) {
		return userRepository.selectByRole_id(role_id);
	}

	@Override
	public User selectById(Integer id) {
		return userRepository.findById(id).get();
	}

}
