package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.House;

public interface HouseService {

	House insert(House house);

	List<House> selectAll();

	List<House> selectByCode(Integer code);

	List<House> selectByUnit_id(Integer unit_id);

	List<House> selectByUnit_idCode(Integer unit_id, Integer code);
	
	House selectById(Integer id);
	
	House update(House house);
	
	List<House> selectByPerson_id(Integer person_id);
	
	List<House> selectByPerson_idIsNull();

}
