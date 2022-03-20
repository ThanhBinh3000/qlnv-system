package com.tcdt.qlnvsystem.repository;

import com.tcdt.qlnvsystem.entities.SysPermissionEntity;
import com.tcdt.qlnvsystem.table.SysPermission;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SysPermissionEntityRepository extends CrudRepository<SysPermissionEntity, Long> {
	@Query(value = "SELECT * FROM SYS_PERMISSION where parent_id is null ORDER BY id desc", nativeQuery = true)
	Iterable<SysPermissionEntity> findAllOrderById();

}
