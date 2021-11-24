package com.tcdt.qlnvsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tcdt.qlnvsystem.table.UserGroup;



public interface UserGroupRepository extends CrudRepository<UserGroup, Long> {

	final String qr = "SELECT * FROM USER_GROUP WHERE (:groupCode is null or lower(GROUP_CODE) like lower(concat(concat('%', :groupCode),'%'))) AND (:groupName is null or lower(GROUP_NAME) like lower(concat(concat('%', :groupName),'%')))";
	final String qrCount = "SELECT count(1) FROM USER_GROUP WHERE (:groupCode is null or lower(GROUP_CODE) like lower(concat(concat('%', :groupCode),'%'))) AND (:groupName is null or lower(GROUP_NAME) like lower(concat(concat('%', :groupName),'%')))";
	
	@Query(value = qr, 
			countQuery = qrCount, nativeQuery = true)
	Page<UserGroup> selectParams(@Param("groupCode") String groupCode, @Param("groupName") String groupName, Pageable pageable);

	Iterable<UserGroup> findByStatus(String ttNhomHoatdong);
}
