package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.House;

public interface HouseRepository extends JpaRepository<House, Integer> {
	@Query(value = "select * from house where  code=:code", nativeQuery = true)
	List<House> selectByCode(@Param("code") Integer code);

	@Query(value = "select * from house where  unit_id=:unit_id", nativeQuery = true)
	List<House> selectByUnit_id(@Param("unit_id") Integer unit_id);

	@Query(value = "select * from house where  unit_id=:unit_id and  code=:code", nativeQuery = true)
	List<House> selectByUnit_idCode(@Param("unit_id") Integer unit_id, @Param("code") Integer code);

	@Query(value = "select * from house where  person_id=:person_id", nativeQuery = true)
	List<House> selectByPerson_id(@Param("person_id") Integer person_id);

	@Query(value = "select * from house where  person_id is null", nativeQuery = true)
	List<House> selectByPerson_idIsNull();

}
