package com.tcdt.qlnvsystem.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tcdt.qlnvsystem.table.PermissionRoles;

@Repository
public interface PermissionRolesRepository extends CrudRepository<PermissionRoles, Long> {
	@Transactional
	void deleteByRoleId(Long id);
}
