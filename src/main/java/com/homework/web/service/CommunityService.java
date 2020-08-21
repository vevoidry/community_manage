package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Community;

public interface CommunityService {
	List<Community> selectAll();

	Community insert(Community community);

	Community selectById(Integer id);

	List<Community> selectByCode(Integer code);

	List<Community> selectByIdCode(Integer id, Integer code);
	
	Community update(Community community);
}
