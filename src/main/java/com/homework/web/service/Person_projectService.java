package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Person_project;

public interface Person_projectService {
	List<Person_project> selectAll();

	Person_project insert(Person_project person_project);

	List<Person_project> selectByProject_id(Integer project_id);

	List<Person_project> selectByPerson_id(Integer person_id);

	List<Person_project> selectByPerson_idIs_handle(Integer person_id, Boolean is_handle);
	
	Person_project selectById(Integer id);
	
	Person_project update(Person_project person_project);
	
	List<Person_project> selectByIs_handle(Boolean is_handle);
}
