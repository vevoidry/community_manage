package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
	@Query(value = "select * from permission where menu_id = :menu_id", nativeQuery = true)
	List<Permission> selectByMenu_id(@Param("menu_id") Integer menu_id);

	@Query(value = "select * from permission where menu_id = :menu_id and name=:name", nativeQuery = true)
	Permission selectByMenu_idName(@Param("menu_id") Integer menu_id, @Param("name") String name);
}
