package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Person_project;

public interface Person_projectRepository extends JpaRepository<Person_project, Integer> {
	@Query(value = "select * from person_project where  project_id=:project_id", nativeQuery = true)
	List<Person_project> selectByProject_id(@Param("project_id") Integer project_id);

	@Query(value = "select * from person_project where  person_id=:person_id", nativeQuery = true)
	List<Person_project> selectByPerson_id(@Param("person_id") Integer person_id);

	@Query(value = "select * from person_project where  person_id=:person_id and is_handle=:is_handle", nativeQuery = true)
	List<Person_project> selectByPerson_idIs_handle(@Param("person_id") Integer person_id,
			@Param("is_handle") Boolean is_handle);

	@Query(value = "select * from person_project where   is_handle=:is_handle", nativeQuery = true)
	List<Person_project> selectByIs_handle(@Param("is_handle") Boolean is_handle);

}
