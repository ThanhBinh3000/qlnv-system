package com.tcdt.qlnvsystem.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.tcdt.qlnvsystem.table.SysPermission;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SysPermissionRepository extends CrudRepository<SysPermission, Long> {
	@Query(value = "SELECT * FROM SYS_PERMISSION where parent_id is null ORDER BY id desc", nativeQuery = true)
	Iterable<SysPermission> findAllOrderById();
	
	@Query(value = "select sp.* from user_roles ur, SYS_PERMISSION sp, roles_permission rp where ur.USER_ID = (select id from user_info where USERNAME = :username)"
			+ "and sp.id = rp.PERMISSION_ID and sp.menu = '01' "
			+ "start with sp.parent_id is null "
			+ "connect by prior sp.id = sp.parent_id", nativeQuery = true)
	Iterable<SysPermission> findByUser(String username);

	@Query(value = "SELECT * FROM SYS_PERMISSION WHERE (:name is null or lower(NAME) like lower(concat(concat('%', :name),'%'))) AND (:status is null or STATUS = :status) AND (:url is null or lower(URL) like lower(concat(concat('%', :url),'%'))) AND (:isShow is null or IS_SHOW = :isShow) AND (:data is null or lower(DATA) like lower(concat(concat('%', :data),'%'))) AND (:place is null or PLACE = to_number(:place)) AND (:parentId is null or PARENT_ID = to_number(:parentId)) start with parent_id is null connect by id = parent_id", countQuery = "SELECT count(1) FROM SYS_PERMISSION WHERE (:name is null or lower(NAME) like lower(concat(concat('%', :name),'%'))) AND (:status is null or STATUS = :status) AND (:url is null or lower(URL) like lower(concat(concat('%', :url),'%'))) AND (:isShow is null or IS_SHOW = :isShow) AND (:data is null or lower(DATA) like lower(concat(concat('%', :data),'%'))) AND (:place is null or PLACE = to_number(:place)) AND (:parentId is null or PARENT_ID = to_number(:parentId)) start with parent_id is null connect by id = parent_id", nativeQuery = true)
	Iterable<SysPermission> selectParams(String name, String status, String url, String isShow, String data, Long place, Long parentId);

	@Transactional()
	@Modifying
	@Query(value = "update SYS_PERMISSION set STATUS = ?1 where id in (select u.id from SYS_PERMISSION u start with u.parent_id = ?2 connect by prior u.id = u.parent_id)", nativeQuery = true)
	void updateStatusChild(String status, Long parentId);
}
