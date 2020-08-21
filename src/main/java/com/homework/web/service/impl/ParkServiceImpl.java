package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Park;
import com.homework.web.repository.ParkRepository;
import com.homework.web.service.ParkService;

@Service
public class ParkServiceImpl implements ParkService {

	@Autowired
	ParkRepository parkRepository;

	@Override
	public List<Park> selectAll() {
		return parkRepository.findAll();
	}

	@Override
	public Park insert(Park park) {
		return parkRepository.save(park);
	}

	@Override
	public List<Park> selectById(Integer id) {
		return parkRepository.selectById(id);
	}

	@Override
	public List<Park> selectByCode(Integer code) {
		return parkRepository.selectByCode(code);
	}

	@Override
	public List<Park> selectByType(String type) {
		return parkRepository.selectByType(type);
	}

	@Override
	public List<Park> selectByIdCode(Integer id, Integer code) {
		return parkRepository.selectByIdCode(id, code);
	}

	@Override
	public List<Park> selectByIdType(Integer id, String type) {
		return parkRepository.selectByIdType(id, type);
	}

	@Override
	public List<Park> selectByCodeType(Integer code, String type) {
		return parkRepository.selectByCodeType(code, type);
	}

	@Override
	public List<Park> selectByIdCodeType(Integer id, Integer code, String type) {
		return parkRepository.selectByIdCodeType(id, code, type);
	}

	@Override
	public Park update(Park park) {
		return parkRepository.save(park);
	}

}
