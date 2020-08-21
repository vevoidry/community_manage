package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Advice;

public interface AdviceService {

	Advice insert(Advice advice);

	List<Advice> selectByPerson_idIs_handle(Integer person_id, Boolean is_handle);

	List<Advice> selectByPerson_id(Integer person_id);

	List<Advice> selectByIs_handle(Boolean is_handle);

	List<Advice> selectAll();
	
	Advice selectById(Integer id);
	
	Advice update(Advice advice);

}
