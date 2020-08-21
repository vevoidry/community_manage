package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Community;
import com.homework.web.repository.CommunityRepository;
import com.homework.web.service.CommunityService;

@Service
public class CommunityServiceImpl implements CommunityService {

	@Autowired
	CommunityRepository communityRepository;

	@Override
	public List<Community> selectAll() {
		return communityRepository.findAll();
	}

	@Override
	public Community insert(Community community) {
		return communityRepository.save(community);
	}

	@Override
	public Community selectById(Integer id) {
		return communityRepository.findById(id).get();
	}

	@Override
	public List<Community> selectByCode(Integer code) {
		return communityRepository.selectByCode(code);
	}

	@Override
	public List<Community> selectByIdCode(Integer id, Integer code) {
		return communityRepository.selectByIdCode(id, code);
	}

	@Override
	public Community update(Community community) {
		return communityRepository.save(community);
	}
}
