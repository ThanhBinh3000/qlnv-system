package com.tcdt.qlnvsystem.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.tcdt.qlnvsystem.table.RolesPermission;

@Repository
public interface RolesPermissionRepository extends CrudRepository<RolesPermission, Long> {
	@Query(value = "SELECT * FROM SYS_PERMISSION where parent_id is null ORDER BY id desc", nativeQuery = true)
	Iterable<RolesPermission> findAllOrderById();
}
