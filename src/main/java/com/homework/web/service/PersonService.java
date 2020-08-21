package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Person;

public interface PersonService {

	List<Person> selectAll();

	Person insert(Person person);

	List<Person> selectByName(String name);

	List<Person> selectById_card_number(String id_card_number);

	List<Person> selectByPhone(String phone);

	List<Person> selectByNameId_card_number(String name, String id_card_number);

	List<Person> selectByNamePhone(String name, String phone);

	List<Person> selectById_card_numberPhone(String id_card_number, String phone);

	List<Person> selectByNameId_card_numberPhone(String name, String id_card_number, String phone);
	
	Person selectById(Integer id);
	
	Person update(Person person);
	
	List<Person> selectByLikePhoneOrId_card_number(String data);
}
