package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Repair;
import com.homework.web.repository.RepairRepository;
import com.homework.web.service.RepairService;
@Service
public class RepairServiceImpl implements RepairService {

	@Autowired
	RepairRepository	repairRepository;
	
	@Override
	public List<Repair> selectByPerson_idIs_handle(Integer person_id, Boolean is_handle) {
		return repairRepository.selectByPerson_idIs_handle(person_id, is_handle);
	}

	@Override
	public List<Repair> selectByPerson_id(Integer person_id) {
		return repairRepository.selectByPerson_id(person_id);
	}

	@Override
	public Repair insert(Repair repair) {
		return repairRepository.save(repair);
	}

	@Override
	public List<Repair> selectByIs_handle(Boolean is_handle) {
		return repairRepository.selectByIs_handle(is_handle);
	}

	@Override
	public List<Repair> selectByIs_handleUser_idIsNotNull(Boolean is_handle) {
		return repairRepository.selectByIs_handleUser_idIsNotNull(is_handle);
	}

	@Override
	public List<Repair> selectByIs_handleUser_idIsNull(Boolean is_handle) {
		return repairRepository.selectByIs_handleUser_idIsNull(is_handle);
	}

	@Override
	public List<Repair> selectAll() {
		return repairRepository.findAll();
	}

	@Override
	public List<Repair> selectByUser_idIsNotNull() {
		return repairRepository.selectByUser_idIsNotNull();
	}

	@Override
	public List<Repair> selectByUser_idIsNull() {
		return repairRepository.selectByUser_idIsNull();
	}

	@Override
	public Repair selectById(Integer id) {
		return repairRepository.findById(id).get();
	}

	@Override
	public Repair update(Repair repair) {
		return repairRepository.save(repair);
	}

	@Override
	public List<Repair> selectByUser_idIs_handle(Integer user_id, Boolean is_handle) {
		return repairRepository.selectByUser_idIs_handle(user_id, is_handle);
	}

	@Override
	public List<Repair> selectByUser_id(Integer user_id) {
		return repairRepository.selectByUser_id(user_id);
	}

}
