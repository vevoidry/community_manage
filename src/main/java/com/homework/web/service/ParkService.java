package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Park;

public interface ParkService {

	List<Park> selectAll();

	Park insert(Park park);

	List<Park> selectById(Integer id);

	List<Park> selectByCode(Integer code);

	List<Park> selectByType(String type);

	List<Park> selectByIdCode(Integer id, Integer code);

	List<Park> selectByIdType(Integer id, String type);

	List<Park> selectByCodeType(Integer code, String type);

	List<Park> selectByIdCodeType(Integer id, Integer code, String type);
	
	Park update(Park park);

}
