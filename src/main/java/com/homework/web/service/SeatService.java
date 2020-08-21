package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Seat;

public interface SeatService {
	Seat insert(Seat seat);

	List<Seat> selectAll();

	List<Seat> selectByPerson_idIsNotNull();

	List<Seat> selectByPark_idPerson_idIsNotNull(Integer park_id);

	List<Seat> selectByCodePerson_idIsNotNull(Integer code);

	List<Seat> selectByPark_idCodePerson_idIsNotNull(Integer park_id, Integer code);

	List<Seat> selectByPerson_idIsNull();

	List<Seat> selectByPark_idPerson_idIsNull(Integer park_id);

	List<Seat> selectByCodePerson_idIsNull(Integer code);

	List<Seat> selectByPark_idCodePerson_idIsNull(Integer park_id, Integer code);

	Seat selectById(Integer id);

	Seat update(Seat seat);

	List<Seat> selectByPark_idCode(Integer park_id, Integer code);

	List<Seat> selectByPark_idCodeRegion(Integer park_id, Integer code, String region);

	List<Seat> selectByCode(Integer code);

	List<Seat> selectByPark_id(Integer park_id);
}
