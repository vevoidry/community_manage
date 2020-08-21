package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Advice;

public interface AdviceRepository extends JpaRepository<Advice, Integer> {
	@Query(value = "select * from advice where person_id = :person_id and is_handle=:is_handle", nativeQuery = true)
	List<Advice> selectByPerson_idIs_handle(@Param("person_id") Integer person_id,
			@Param("is_handle") Boolean is_handle);

	@Query(value = "select * from advice where person_id = :person_id ", nativeQuery = true)
	List<Advice> selectByPerson_id(@Param("person_id") Integer person_id);

	@Query(value = "select * from advice where  is_handle=:is_handle", nativeQuery = true)
	List<Advice> selectByIs_handle(@Param("is_handle") Boolean is_handle);
}
