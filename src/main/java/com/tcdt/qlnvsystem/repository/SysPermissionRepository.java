package com.tcdt.qlnvsystem.repository;

import com.tcdt.qlnvsystem.entities.SysPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.tcdt.qlnvsystem.table.SysPermission;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> {
	@Query(value = "SELECT * FROM SYS_PERMISSION where parent_id is null ORDER BY id desc", nativeQuery = true)
	Iterable<SysPermission> findAllOrderById();

	@Query(value = "select sp.* from user_roles ur, SYS_PERMISSION sp, roles_permission rp where ur.USER_ID = (select id from user_info where USERNAME = :username)"
			+ "and sp.id = rp.PERMISSION_ID and sp.menu = '01' "
			+ "start with sp.parent_id is null "
			+ "connect by prior sp.id = sp.parent_id", nativeQuery = true)
	Iterable<SysPermission> findByUser(String username);

	@Query("SELECT new com.tcdt.qlnvsystem.entities.SysPermissionEntity(sysPer.id,sysPer.name,sysPer.code,sysPer.url,sysPer.thuTu,sysPer.trangThai,sysPer.menu,sysPer.icon) FROM SysPermission sysPer where sysPer.parent is null ORDER BY sysPer.id desc")
	List<SysPermissionEntity> findAllParent();

	@Query("SELECT new com.tcdt.qlnvsystem.entities.SysPermissionEntity(sysPer.id,sysPer.name,sysPer.code,sysPer.url,sysPer.thuTu,sysPer.trangThai,sysPer.menu,sysPer.icon) FROM SysPermission sysPer where sysPer.parent.id = :parentId ORDER BY sysPer.id desc")
	List<SysPermissionEntity>findChildByParentId(Long parentId);

	@Query(value = "SELECT * FROM SYS_PERMISSION WHERE (:name is null or lower(NAME) like lower(concat(concat('%', :name),'%'))) AND (:status is null or STATUS = :status) AND (:url is null or lower(URL) like lower(concat(concat('%', :url),'%'))) AND (:isShow is null or IS_SHOW = :isShow) AND (:data is null or lower(DATA) like lower(concat(concat('%', :data),'%'))) AND (:place is null or PLACE = to_number(:place)) AND (:parentId is null or PARENT_ID = to_number(:parentId)) start with parent_id is null connect by id = parent_id", countQuery = "SELECT count(1) FROM SYS_PERMISSION WHERE (:name is null or lower(NAME) like lower(concat(concat('%', :name),'%'))) AND (:status is null or STATUS = :status) AND (:url is null or lower(URL) like lower(concat(concat('%', :url),'%'))) AND (:isShow is null or IS_SHOW = :isShow) AND (:data is null or lower(DATA) like lower(concat(concat('%', :data),'%'))) AND (:place is null or PLACE = to_number(:place)) AND (:parentId is null or PARENT_ID = to_number(:parentId)) start with parent_id is null connect by id = parent_id", nativeQuery = true)
	Iterable<SysPermission> selectParams(String name, String status, String url, String isShow, String data, Long place, Long parentId);

	@Transactional()
	@Modifying
	@Query(value = "update SYS_PERMISSION set STATUS = ?1 where id in (select u.id from SYS_PERMISSION u start with u.parent_id = ?2 connect by prior u.id = u.parent_id)", nativeQuery = true)
	void updateStatusChild(String status, Long parentId);
}
