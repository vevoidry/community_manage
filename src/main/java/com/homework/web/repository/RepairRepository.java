package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Repair;

public interface RepairRepository extends JpaRepository<Repair, Integer> {
	@Query(value = "select * from repair where person_id = :person_id and is_handle=:is_handle", nativeQuery = true)
	List<Repair> selectByPerson_idIs_handle(@Param("person_id") Integer person_id,
			@Param("is_handle") Boolean is_handle);

	@Query(value = "select * from repair where person_id = :person_id ", nativeQuery = true)
	List<Repair> selectByPerson_id(@Param("person_id") Integer person_id);

	@Query(value = "select * from repair where is_handle = :is_handle ", nativeQuery = true)
	List<Repair> selectByIs_handle(@Param("is_handle") Boolean is_handle);

	@Query(value = "select * from repair where is_handle = :is_handle and user_id is not null", nativeQuery = true)
	List<Repair> selectByIs_handleUser_idIsNotNull(@Param("is_handle") Boolean is_handle);

	@Query(value = "select * from repair where is_handle = :is_handle and user_id is null", nativeQuery = true)
	List<Repair> selectByIs_handleUser_idIsNull(@Param("is_handle") Boolean is_handle);

	@Query(value = "select * from repair where  user_id is not null", nativeQuery = true)
	List<Repair> selectByUser_idIsNotNull();

	@Query(value = "select * from repair where  user_id is null", nativeQuery = true)
	List<Repair> selectByUser_idIsNull();

	@Query(value = "select * from repair where user_id = :user_id and is_handle=:is_handle", nativeQuery = true)
	List<Repair> selectByUser_idIs_handle(@Param("user_id") Integer user_id, @Param("is_handle") Boolean is_handle);

	@Query(value = "select * from repair where user_id = :user_id ", nativeQuery = true)
	List<Repair> selectByUser_id(@Param("user_id") Integer user_id);
}
