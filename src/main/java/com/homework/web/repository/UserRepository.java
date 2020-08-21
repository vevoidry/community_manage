package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.User;

//继承JpaRepository接口，指定要操作的表和该表的主键类型
//spring会自动为该接口生成一个实现类，并为该实现类自动生成一个对象，并且将该对象交给spring管理
//该对象自带了很多通用的方法
public interface UserRepository extends JpaRepository<User, Integer> {
	// nativeQuery = true说明是sql
	//@Query是查询
	@Query(value = "select * from user where username = :username", nativeQuery = true)
	User selectByUsername(@Param("username") String username);

	@Query(value = "select * from user where phone = :phone", nativeQuery = true)
	User selectByPhone(@Param("phone") String phone);

	@Query(value = "select * from user where email = :email", nativeQuery = true)
	User selectByEmail(@Param("email") String email);

	@Query(value = "select * from user where role_id = :role_id", nativeQuery = true)
	List<User> selectByRole_id(@Param("role_id") Integer role_id);
}
