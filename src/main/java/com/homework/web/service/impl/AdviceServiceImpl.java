package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Advice;
import com.homework.web.repository.AdviceRepository;
import com.homework.web.service.AdviceService;

@Service
public class AdviceServiceImpl implements AdviceService {

	@Autowired
	AdviceRepository adviceRepository;

	@Override
	public Advice insert(Advice advice) {
		return adviceRepository.save(advice);
	}

	@Override
	public List<Advice> selectByPerson_idIs_handle(Integer person_id, Boolean is_handle) {
		return adviceRepository.selectByPerson_idIs_handle(person_id, is_handle);
	}

	@Override
	public List<Advice> selectByPerson_id(Integer person_id) {
		return adviceRepository.selectByPerson_id(person_id);
	}

	@Override
	public List<Advice> selectByIs_handle(Boolean is_handle) {
		return adviceRepository.selectByIs_handle(is_handle);
	}

	@Override
	public List<Advice> selectAll() {
		return adviceRepository.findAll();
	}

	@Override
	public Advice selectById(Integer id) {
		return adviceRepository.findById(id).get();
	}

	@Override
	public Advice update(Advice advice) {
		return adviceRepository.save(advice);
	}

}
