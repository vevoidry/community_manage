package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Role_permission;

public interface Role_permissionRepository extends JpaRepository<Role_permission, Integer> {
	@Query(value = "select * from role_permission where role_id = :role_id", nativeQuery = true)
	List<Role_permission> selectByRole_id(@Param("role_id") Integer role_id);

	@Modifying(clearAutomatically = true)
	@Query(value = "delete from role_permission where role_id=:role_id", nativeQuery = true)
	void deleteByRole_id(@Param("role_id") Integer role_id);

}
