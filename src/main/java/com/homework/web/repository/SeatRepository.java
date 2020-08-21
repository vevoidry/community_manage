package com.homework.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.homework.web.pojo.Seat;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
	@Query(value = "select * from seat where  person_id is not null", nativeQuery = true)
	List<Seat> selectByPerson_idIsNotNull();

	@Query(value = "select * from seat where park_id = :park_id  and person_id is not null", nativeQuery = true)
	List<Seat> selectByPark_idPerson_idIsNotNull(@Param("park_id") Integer park_id);

	@Query(value = "select * from seat where  code=:code and person_id is not null", nativeQuery = true)
	List<Seat> selectByCodePerson_idIsNotNull(@Param("code") Integer code);

	@Query(value = "select * from seat where park_id = :park_id and code=:code and person_id is not null", nativeQuery = true)
	List<Seat> selectByPark_idCodePerson_idIsNotNull(@Param("park_id") Integer park_id, @Param("code") Integer code);

	@Query(value = "select * from seat where  person_id is null", nativeQuery = true)
	List<Seat> selectByPerson_idIsNull();

	@Query(value = "select * from seat where park_id = :park_id  and person_id is null", nativeQuery = true)
	List<Seat> selectByPark_idPerson_idIsNull(@Param("park_id") Integer park_id);

	@Query(value = "select * from seat where  code=:code and person_id is null", nativeQuery = true)
	List<Seat> selectByCodePerson_idIsNull(@Param("code") Integer code);

	@Query(value = "select * from seat where park_id = :park_id and code=:code and person_id is null", nativeQuery = true)
	List<Seat> selectByPark_idCodePerson_idIsNull(@Param("park_id") Integer park_id, @Param("code") Integer code);

	@Query(value = "select * from seat where park_id = :park_id and code=:code", nativeQuery = true)
	List<Seat> selectByPark_idCode(@Param("park_id") Integer park_id, @Param("code") Integer code);

	@Query(value = "select * from seat where park_id = :park_id and code=:code and region=:region", nativeQuery = true)
	List<Seat> selectByPark_idCodeRegion(@Param("park_id") Integer park_id, @Param("code") Integer code,
			@Param("region") String region);

	@Query(value = "select * from seat where  code=:code", nativeQuery = true)
	List<Seat> selectByCode(@Param("code") Integer code);

	@Query(value = "select * from seat where park_id = :park_id", nativeQuery = true)
	List<Seat> selectByPark_id(@Param("park_id") Integer park_id);

}
