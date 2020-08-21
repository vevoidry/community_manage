package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Person_project;
import com.homework.web.repository.Person_projectRepository;
import com.homework.web.service.Person_projectService;

@Service
public class Person_projectServiceImpl implements Person_projectService {

	@Autowired
	Person_projectRepository person_projectRepository;

	@Override
	public List<Person_project> selectAll() {
		return person_projectRepository.findAll();
	}

	@Override
	public Person_project insert(Person_project person_project) {
		return person_projectRepository.save(person_project);
	}

	@Override
	public List<Person_project> selectByProject_id(Integer project_id) {
		return person_projectRepository.selectByProject_id(project_id);
	}

	@Override
	public List<Person_project> selectByPerson_id(Integer person_id) {
		return person_projectRepository.selectByPerson_id(person_id);
	}

	@Override
	public List<Person_project> selectByPerson_idIs_handle(Integer person_id, Boolean is_handle) {
		return person_projectRepository.selectByPerson_idIs_handle(person_id, is_handle);
	}

	@Override
	public Person_project selectById(Integer id) {
		return person_projectRepository.findById(id).get();
	}

	@Override
	public Person_project update(Person_project person_project) {
		return person_projectRepository.save(person_project);
	}

	@Override
	public List<Person_project> selectByIs_handle(Boolean is_handle) {
		return person_projectRepository.selectByIs_handle(is_handle);
	}

}
