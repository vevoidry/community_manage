package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Person_member;

public interface Person_memberRepository extends JpaRepository<Person_member, Integer> {
	@Query(value = "select * from person_member where  person_id=:person_id", nativeQuery = true)
	List<Person_member> selectByPerson_id(@Param("person_id") Integer person_id);

	@Query(value = "select * from person_member where  person_id=:person_id and id_card_number=:id_card_number", nativeQuery = true)
	Person_member selectByPerson_idId_card_number(@Param("person_id") Integer person_id,
			@Param("id_card_number") String id_card_number);

	@Query(value = "select * from person_member where  person_id=:person_id and phone=:phone", nativeQuery = true)
	Person_member selectByPerson_idPhone(@Param("person_id") Integer person_id, @Param("phone") String phone);
}
