package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.House;
import com.homework.web.repository.HouseRepository;
import com.homework.web.service.HouseService;

@Service
public class HouseServiceImpl implements HouseService {

	@Autowired
	HouseRepository houseRepository;

	@Override
	public House insert(House house) {
		return houseRepository.save(house);
	}

	@Override
	public List<House> selectAll() {
		return houseRepository.findAll();
	}

	@Override
	public List<House> selectByCode(Integer code) {
		return houseRepository.selectByCode(code);
	}

	@Override
	public List<House> selectByUnit_id(Integer unit_id) {
		return houseRepository.selectByUnit_id(unit_id);
	}

	@Override
	public List<House> selectByUnit_idCode(Integer unit_id, Integer code) {
		return houseRepository.selectByUnit_idCode(unit_id, code);
	}

	@Override
	public House selectById(Integer id) {
		return houseRepository.findById(id).get();
	}

	@Override
	public House update(House house) {
		return houseRepository.save(house);
	}

	@Override
	public List<House> selectByPerson_id(Integer person_id) {
		return houseRepository.selectByPerson_id(person_id);
	}

	@Override
	public List<House> selectByPerson_idIsNull() {
		return houseRepository.selectByPerson_idIsNull();
	}

}
