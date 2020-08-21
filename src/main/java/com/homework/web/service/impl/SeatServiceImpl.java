package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Seat;
import com.homework.web.repository.SeatRepository;
import com.homework.web.service.SeatService;

@Service
public class SeatServiceImpl implements SeatService {

	@Autowired
	SeatRepository seatRepository;

	@Override
	public Seat insert(Seat seat) {
		return seatRepository.save(seat);
	}

	@Override
	public List<Seat> selectAll() {
		return seatRepository.findAll();
	}

	@Override
	public List<Seat> selectByPerson_idIsNotNull() {
		return seatRepository.selectByPerson_idIsNotNull();
	}

	@Override
	public List<Seat> selectByPark_idPerson_idIsNotNull(Integer park_id) {
		return seatRepository.selectByPark_idPerson_idIsNotNull(park_id);
	}

	@Override
	public List<Seat> selectByCodePerson_idIsNotNull(Integer code) {
		return seatRepository.selectByCodePerson_idIsNotNull(code);
	}

	@Override
	public List<Seat> selectByPark_idCodePerson_idIsNotNull(Integer park_id, Integer code) {
		return seatRepository.selectByPark_idCodePerson_idIsNotNull(park_id, code);
	}

	@Override
	public List<Seat> selectByPerson_idIsNull() {
		return seatRepository.selectByPerson_idIsNull();
	}

	@Override
	public List<Seat> selectByPark_idPerson_idIsNull(Integer park_id) {
		return seatRepository.selectByPark_idPerson_idIsNull(park_id);
	}

	@Override
	public List<Seat> selectByCodePerson_idIsNull(Integer code) {
		return seatRepository.selectByCodePerson_idIsNull(code);
	}

	@Override
	public List<Seat> selectByPark_idCodePerson_idIsNull(Integer park_id, Integer code) {
		return seatRepository.selectByPark_idCodePerson_idIsNull(park_id, code);
	}

	@Override
	public Seat selectById(Integer id) {
		return seatRepository.findById(id).get();
	}

	@Override
	public Seat update(Seat seat) {
		return seatRepository.save(seat);
	}

	@Override
	public List<Seat> selectByPark_idCode(Integer park_id, Integer code) {
		return seatRepository.selectByPark_idCode(park_id, code);
	}

	@Override
	public List<Seat> selectByPark_idCodeRegion(Integer park_id, Integer code, String region) {
		return seatRepository.selectByPark_idCodeRegion(park_id, code, region);
	}

	@Override
	public List<Seat> selectByCode(Integer code) {
		return seatRepository.selectByCode(code);
	}

	@Override
	public List<Seat> selectByPark_id(Integer park_id) {
		return seatRepository.selectByPark_id(park_id);
	}

}
