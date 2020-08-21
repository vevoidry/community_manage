package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Person;
import com.homework.web.repository.PersonRepository;
import com.homework.web.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	PersonRepository personRepository;

	@Override
	public List<Person> selectAll() {
		return personRepository.findAll();
	}

	@Override
	public Person insert(Person person) {
		return personRepository.save(person);
	}

	@Override
	public List<Person> selectByName(String name) {
		return personRepository.selectByName(name);
	}

	@Override
	public List<Person> selectById_card_number(String id_card_number) {
		return personRepository.selectById_card_number(id_card_number);
	}

	@Override
	public List<Person> selectByPhone(String phone) {
		return personRepository.selectByPhone(phone);
	}

	@Override
	public List<Person> selectByNameId_card_number(String name, String id_card_number) {
		return personRepository.selectByNameId_card_number(name, id_card_number);
	}

	@Override
	public List<Person> selectByNamePhone(String name, String phone) {
		return personRepository.selectByNamePhone(name, phone);
	}

	@Override
	public List<Person> selectById_card_numberPhone(String id_card_number, String phone) {
		return personRepository.selectById_card_numberPhone(id_card_number, phone);
	}

	@Override
	public List<Person> selectByNameId_card_numberPhone(String name, String id_card_number, String phone) {
		return personRepository.selectByNameId_card_numberPhone(name, id_card_number, phone);
	}

	@Override
	public Person selectById(Integer id) {
		return personRepository.findById(id).get();
	}

	@Override
	public Person update(Person person) {
		return personRepository.save(person);
	}

	@Override
	public List<Person> selectByLikePhoneOrId_card_number(String data) {
		return personRepository.selectByLikePhoneOrId_card_number(data);
	}

}
