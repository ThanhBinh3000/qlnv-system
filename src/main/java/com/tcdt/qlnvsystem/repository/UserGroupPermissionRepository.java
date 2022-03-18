package com.tcdt.qlnvsystem.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

public interface UserGroupPermissionRepository extends CrudRepository<UserGroupPermission, Long> {

	@Transactional
	void deleteByGroupId(Long id);

	List<UserGroupPermission> findByGroupId(Long id);

}
