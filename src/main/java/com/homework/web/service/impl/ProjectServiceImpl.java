package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Project;
import com.homework.web.repository.ProjectRepository;
import com.homework.web.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ProjectRepository projectRepository;

	@Override
	public Project insert(Project project) {
		return projectRepository.save(project);
	}

	@Override
	public List<Project> selectAll() {
		return projectRepository.findAll();
	}

//	@Override
//	public List<Project> selectByType(String type) {
//		return projectRepository.selectByType(type);
//	}

	@Override
	public List<Project> selectByName(String name) {
		return projectRepository.selectByName(name);
	}

//	@Override
//	public List<Project> selectByTypeName(String type, String name) {
//		return projectRepository.selectByTypeName(type, name);
//	}

	@Override
	public Project selectById(Integer id) {
		return projectRepository.findById(id).get();
	}

	@Override
	public Project update(Project project) {
		return projectRepository.save(project);
	}

}
