package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Repair;

public interface RepairService {
	List<Repair> selectByPerson_idIs_handle(Integer person_id, Boolean is_handle);

	List<Repair> selectByPerson_id(Integer person_id);

	Repair insert(Repair repair);

	List<Repair> selectByIs_handle(Boolean is_handle);

	List<Repair> selectByIs_handleUser_idIsNotNull(Boolean is_handle);

	List<Repair> selectByIs_handleUser_idIsNull(Boolean is_handle);

	List<Repair> selectAll();

	List<Repair> selectByUser_idIsNotNull();

	List<Repair> selectByUser_idIsNull();

	Repair selectById(Integer id);

	Repair update(Repair repair);

	List<Repair> selectByUser_idIs_handle(Integer user_id, Boolean is_handle);

	List<Repair> selectByUser_id(Integer user_id);
}
