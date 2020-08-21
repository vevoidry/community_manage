package com.homework.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	@Query(value = "select * from role where name=:name", nativeQuery = true)
	Role selectByName(@Param("name") String name);
}
