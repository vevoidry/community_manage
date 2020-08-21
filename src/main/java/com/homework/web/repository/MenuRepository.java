package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Menu;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
	@Query(value = "select * from menu where parent_id = :parent_id and name=:name", nativeQuery = true)
	Menu selectByParent_idName(@Param("parent_id") Integer parent_id, @Param("name") String name);

//	@Query(value = "select * from menu where parent_id = :parent_id", nativeQuery = true)
//	List<Menu> selectByParent_id(@Param("parent_id") Integer parent_id);
	
	@Query(value = "select * from menu where parent_id = :parent_id order by rank", nativeQuery = true)
	List<Menu> selectByParent_idRank(@Param("parent_id") Integer parent_id);
}
