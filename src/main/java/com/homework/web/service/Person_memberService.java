package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Person_member;

public interface Person_memberService {
	List<Person_member> selectByPerson_id(Integer person_id);

	Person_member insert(Person_member person_member);

	Person_member selectByPerson_idId_card_number(Integer person_id, String id_card_number);

	Person_member selectByPerson_idPhone(Integer person_id, String phone);
	
	Person_member selectById(Integer id);
	
	Person_member update(Person_member person_member);
	
	void deleteById(Integer id);
}
