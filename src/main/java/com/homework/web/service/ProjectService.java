package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Project;

public interface ProjectService {

	Project insert(Project project);

	List<Project> selectAll();

//	List<Project> selectByType(String type);

	List<Project> selectByName(String name);

//	List<Project> selectByTypeName(String type, String name);
	
	Project selectById(Integer id);
	
	Project update(Project project);

}
