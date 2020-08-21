package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Person_member;
import com.homework.web.repository.Person_memberRepository;
import com.homework.web.service.Person_memberService;

@Service
public class Person_memberServiceImpl implements Person_memberService {

	@Autowired
	Person_memberRepository person_memberRepository;

	@Override
	public List<Person_member> selectByPerson_id(Integer person_id) {
		return person_memberRepository.selectByPerson_id(person_id);
	}

	@Override
	public Person_member insert(Person_member person_member) {
		return person_memberRepository.save(person_member);
	}

	@Override
	public Person_member selectByPerson_idId_card_number(Integer person_id, String id_card_number) {
		return person_memberRepository.selectByPerson_idId_card_number(person_id, id_card_number);
	}

	@Override
	public Person_member selectByPerson_idPhone(Integer person_id, String phone) {
		return person_memberRepository.selectByPerson_idPhone(person_id, phone);
	}

	@Override
	public Person_member selectById(Integer id) {
		return person_memberRepository.findById(id).get();
	}

	@Override
	public Person_member update(Person_member person_member) {
		return person_memberRepository.save(person_member);
	}

	@Override
	public void deleteById(Integer id) {
		person_memberRepository.deleteById(id);
	}

}
