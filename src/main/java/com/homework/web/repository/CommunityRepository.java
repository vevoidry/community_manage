package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Community;

public interface CommunityRepository extends JpaRepository<Community, Integer> {

	@Query(value = "select * from community where  code=:code", nativeQuery = true)
	List<Community> selectByCode(@Param("code") Integer code);

	@Query(value = "select * from community where id = :id and code=:code", nativeQuery = true)
	List<Community> selectByIdCode(@Param("id") Integer id, @Param("code") Integer code);
}
