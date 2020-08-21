package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {
	@Query(value = "select * from person where name = :name", nativeQuery = true)
	List<Person> selectByName(@Param("name") String name);

	@Query(value = "select * from person where  id_card_number=:id_card_number ", nativeQuery = true)
	List<Person> selectById_card_number(@Param("id_card_number") String id_card_number);

	@Query(value = "select * from person where  phone=:phone", nativeQuery = true)
	List<Person> selectByPhone(@Param("phone") String phone);

	@Query(value = "select * from person where name = :name and id_card_number=:id_card_number ", nativeQuery = true)
	List<Person> selectByNameId_card_number(@Param("name") String name, @Param("id_card_number") String id_card_number);

	@Query(value = "select * from person where name = :name  and phone=:phone", nativeQuery = true)
	List<Person> selectByNamePhone(@Param("name") String name, @Param("phone") String phone);

	@Query(value = "select * from person where  id_card_number=:id_card_number and phone=:phone", nativeQuery = true)
	List<Person> selectById_card_numberPhone(@Param("id_card_number") String id_card_number,
			@Param("phone") String phone);

	@Query(value = "select * from person where name = :name and id_card_number=:id_card_number and phone=:phone", nativeQuery = true)
	List<Person> selectByNameId_card_numberPhone(@Param("name") String name,
			@Param("id_card_number") String id_card_number, @Param("phone") String phone);

	@Query(value = "select * from person where  phone like %:data% or id_card_number like %:data%", nativeQuery = true)
	List<Person> selectByLikePhoneOrId_card_number(@Param("data") String data);

}
