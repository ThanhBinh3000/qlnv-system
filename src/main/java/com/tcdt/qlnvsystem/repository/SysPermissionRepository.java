package com.tcdt.qlnvsystem.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.tcdt.qlnvsystem.table.SysPermission;

@Repository
public interface SysPermissionRepository extends CrudRepository<SysPermission, Long> {
	@Query(value = "SELECT * FROM SYS_PERMISSION where parent_id is null ORDER BY id desc", nativeQuery = true)
	Iterable<SysPermission> findAllOrderById();
	
	@Query(value = "select sp.* from user_roles ur, SYS_PERMISSION sp, roles_permission rp where ur.USER_ID = (select id from user_info where USERNAME = :username)"
			+ "and sp.id = rp.PERMISSION_ID and sp.menu = '01' "
			+ "start with sp.parent_id is null "
			+ "connect by prior sp.id = sp.parent_id", nativeQuery = true)
	Iterable<SysPermission> findByUser(String username);
}
