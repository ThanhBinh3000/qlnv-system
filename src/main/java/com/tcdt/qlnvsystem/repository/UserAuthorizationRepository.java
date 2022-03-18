package com.tcdt.qlnvsystem.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

public interface UserAuthorizationRepository extends CrudRepository<UserAuthorization, Long> {

	@Transactional
	void deleteByGroupId(Long id);

	long countByGroupId(Long groupId);
}
