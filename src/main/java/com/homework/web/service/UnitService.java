package com.homework.web.service;

import java.util.List;

import com.homework.web.pojo.Unit;

public interface UnitService {
	List<Unit> selectAll();

	List<Unit> selectByCommunity_idCodeFloor(Integer community_id, Integer code, Integer floor);

	List<Unit> selectByCommunity_idCode(Integer community_id, Integer code);

	List<Unit> selectByCommunity_idFloor(Integer community_id, Integer floor);

	List<Unit> selectByCodeFloor(Integer code, Integer floor);

	List<Unit> selectByCommunity_id(Integer community_id);

	List<Unit> selectByCode(Integer code);

	List<Unit> selectByFloor(Integer floor);

	Unit insert(Unit unit);
	
	Unit selectById(Integer id);
	
	Unit update(Unit unit);
}
