package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Unit;

public interface UnitRepository extends JpaRepository<Unit, Integer> {
	@Query(value = "select * from unit where community_id = :community_id and code=:code and floor=:floor", nativeQuery = true)
	List<Unit> selectByCommunity_idCodeFloor(@Param("community_id") Integer community_id, @Param("code") Integer code,
                                             @Param("floor") Integer floor);

	@Query(value = "select * from unit where community_id = :community_id and code=:code", nativeQuery = true)
	List<Unit> selectByCommunity_idCode(@Param("community_id") Integer community_id, @Param("code") Integer code);

	@Query(value = "select * from unit where community_id = :community_id  and floor=:floor", nativeQuery = true)
	List<Unit> selectByCommunity_idFloor(@Param("community_id") Integer community_id, @Param("floor") Integer floor);

	@Query(value = "select * from unit where code=:code and floor=:floor", nativeQuery = true)
	List<Unit> selectByCodeFloor(@Param("code") Integer code, @Param("floor") Integer floor);

	@Query(value = "select * from unit where community_id = :community_id ", nativeQuery = true)
	List<Unit> selectByCommunity_id(@Param("community_id") Integer community_id);

	@Query(value = "select * from unit where  code=:code ", nativeQuery = true)
	List<Unit> selectByCode(@Param("code") Integer code);

	@Query(value = "select * from unit where  floor=:floor", nativeQuery = true)
	List<Unit> selectByFloor(@Param("floor") Integer floor);

}
