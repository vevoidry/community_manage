package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
//	@Query(value = "select * from project where type = :type ", nativeQuery = true)
//	List<Project> selectByType(@Param("type") String type);

	@Query(value = "select * from project where  name like %:name%", nativeQuery = true)
	List<Project> selectByName(@Param("name") String name);

//	@Query(value = "select * from project where type = :type and name=:name", nativeQuery = true)
//	List<Project> selectByTypeName(@Param("type") String type, @Param("name") String name);
}
