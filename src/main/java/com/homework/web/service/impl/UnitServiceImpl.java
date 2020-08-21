package com.homework.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.web.pojo.Unit;
import com.homework.web.repository.UnitRepository;
import com.homework.web.service.UnitService;

@Service
public class UnitServiceImpl implements UnitService {

	@Autowired
	UnitRepository unitRepository;

	@Override
	public List<Unit> selectAll() {
		return unitRepository.findAll();
	}

	@Override
	public List<Unit> selectByCommunity_idCodeFloor(Integer community_id, Integer code, Integer floor) {
		return unitRepository.selectByCommunity_idCodeFloor(community_id, code, floor);
	}

	@Override
	public List<Unit> selectByCommunity_idCode(Integer community_id, Integer code) {
		return unitRepository.selectByCommunity_idCode(community_id, code);
	}

	@Override
	public List<Unit> selectByCommunity_idFloor(Integer community_id, Integer floor) {
		return unitRepository.selectByCommunity_idFloor(community_id, floor);
	}

	@Override
	public List<Unit> selectByCodeFloor(Integer code, Integer floor) {
		return unitRepository.selectByCodeFloor(code, floor);
	}

	@Override
	public List<Unit> selectByCommunity_id(Integer community_id) {
		return unitRepository.selectByCommunity_id(community_id);
	}

	@Override
	public List<Unit> selectByCode(Integer code) {
		return unitRepository.selectByCode(code);
	}

	@Override
	public List<Unit> selectByFloor(Integer floor) {
		return unitRepository.selectByFloor(floor);
	}

	@Override
	public Unit insert(Unit unit) {
		return unitRepository.save(unit);
	}

	@Override
	public Unit selectById(Integer id) {
		return unitRepository.findById(id).get();
	}

	@Override
	public Unit update(Unit unit) {
		return unitRepository.save(unit);
	}

}
