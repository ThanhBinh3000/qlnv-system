package com.tcdt.qlnvsystem.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tcdt.qlnvsystem.entities.RolesPermissionEntity;

@Repository
public interface RolesPermissionEntityRepository extends CrudRepository<RolesPermissionEntity, Long> {

	
	@Query(value = "select sp.* from user_roles ur, SYS_PERMISSION sp, roles_permission rp where ur.USER_ID = (select id from user_info where USERNAME = :username)"
			+ "and sp.id = rp.PERMISSION_ID and sp.menu = '01' "
			+ "start with sp.parent_id is null "
			+ "connect by prior sp.id = sp.parent_id", nativeQuery = true)
	Iterable<RolesPermissionEntity> findByUser(String username);
}
