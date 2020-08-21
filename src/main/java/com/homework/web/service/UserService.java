package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.User;

public interface UserService {
	User insert(User user);

	List<User> selectAll();

	User selectByUsername(String username);

	User selectByPhone(String phone);

	User selectByEmail(String email);
	
	List<User> selectByRole_id(Integer role_id);
	
	User selectById(Integer id);
}
