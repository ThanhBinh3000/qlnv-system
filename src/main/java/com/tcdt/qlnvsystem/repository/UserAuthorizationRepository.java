package com.tcdt.qlnvsystem.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import com.tcdt.qlnvsystem.table.UserAuthorization;

public interface UserAuthorizationRepository extends CrudRepository<UserAuthorization, Long> {

	@Transactional
	void deleteByGroupId(Long id);

	long countByGroupId(Long groupId);
}
