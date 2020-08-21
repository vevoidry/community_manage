package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Park;

public interface ParkRepository extends JpaRepository<Park, Integer> {
	@Query(value = "select * from park where id = :id", nativeQuery = true)
	List<Park> selectById(@Param("id") Integer id);

	@Query(value = "select * from park where  code=:code", nativeQuery = true)
	List<Park> selectByCode(@Param("code") Integer code);

	@Query(value = "select * from park where type=:type", nativeQuery = true)
	List<Park> selectByType(@Param("type") String type);

	@Query(value = "select * from park where id = :id and code=:code", nativeQuery = true)
	List<Park> selectByIdCode(@Param("id") Integer id, @Param("code") Integer code);

	@Query(value = "select * from park where id = :id and type=:type", nativeQuery = true)
	List<Park> selectByIdType(@Param("id") Integer id, @Param("type") String type);

	@Query(value = "select * from park where code = :code and type=:type", nativeQuery = true)
	List<Park> selectByCodeType(@Param("code") Integer code, @Param("type") String type);

	@Query(value = "select * from park where id = :id and code=:code and type=:type", nativeQuery = true)
	List<Park> selectByIdCodeType(@Param("id") Integer id, @Param("code") Integer code, @Param("type") String type);

}
