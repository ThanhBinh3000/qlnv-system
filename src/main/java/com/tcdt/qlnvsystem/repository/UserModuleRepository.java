package com.tcdt.qlnvsystem.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tcdt.qlnvsystem.table.UserModule;



@Repository
public interface UserModuleRepository extends CrudRepository<UserModule, Long> {

	@Query(value = "SELECT * FROM(SELECT * FROM USER_MODULE WHERE parent_id is null union all "
			+ "SELECT m.* FROM USER_INFO i, USER_GROUP_PERMISSION p, USER_MODULE m "
			+ "WHERE i.username = :username AND i.group_id = p.group_id AND p.module_id = m.id) ORDER BY PLACE asc", nativeQuery = true)
	Iterable<UserModule> findByUsername(String username);

	@Query(value = "SELECT * FROM USER_MODULE where parent_id is null ORDER BY id", nativeQuery = true)
	Iterable<UserModule> findAllOrderBy();
	
	@Query(value = "SELECT * FROM USER_MODULE WHERE (:name is null or lower(NAME) like lower(concat(concat('%', :name),'%'))) AND (:status is null or STATUS = :status) AND (:url is null or lower(URL) like lower(concat(concat('%', :url),'%'))) AND (:isShow is null or IS_SHOW = :isShow) AND (:data is null or lower(DATA) like lower(concat(concat('%', :data),'%'))) AND (:place is null or PLACE = to_number(:place)) AND (:parentId is null or PARENT_ID = to_number(:parentId)) start with parent_id is null connect by id = parent_id", countQuery = "SELECT count(1) FROM USER_MODULE WHERE (:name is null or lower(NAME) like lower(concat(concat('%', :name),'%'))) AND (:status is null or STATUS = :status) AND (:url is null or lower(URL) like lower(concat(concat('%', :url),'%'))) AND (:isShow is null or IS_SHOW = :isShow) AND (:data is null or lower(DATA) like lower(concat(concat('%', :data),'%'))) AND (:place is null or PLACE = to_number(:place)) AND (:parentId is null or PARENT_ID = to_number(:parentId)) start with parent_id is null connect by id = parent_id", nativeQuery = true)
	Iterable<UserModule> selectParams(String name, String status, String url, String isShow, String data, Long place, Long parentId);

	@Transactional()
	@Modifying
	@Query(value = "update USER_MODULE set STATUS = ?1 where id in (select u.id from USER_MODULE u start with u.parent_id = ?2 connect by prior u.id = u.parent_id)", nativeQuery = true)
	void updateStatusChild(String status, Long parentId);
}
