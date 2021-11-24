package com.tcdt.qlnvsystem.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tcdt.qlnvsystem.table.UserInfo;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

	UserInfo findByUsername(String username);

	UserInfo findByToken(String token);

	@Transactional()
	@Modifying
	@Query(value = "UPDATE USER_INFO SET password = ?1, token='' WHERE token=?2", nativeQuery = true)
	void updatePassword(String password, String token);

	@Transactional()
	@Modifying
	@Query(value = "UPDATE USER_INFO SET password = ?1 WHERE username=?2", nativeQuery = true)
	void updatePasswordWhereUsername(String password, String username);

	@Query(value = "SELECT p.* from USER_INFO p where LOWER(p.USERNAME) = LOWER(?1)", nativeQuery = true)
	UserInfo findByUserIgnoreCase(@Param("username") String username);

	@Transactional()
	@Query(value = "SELECT count(*) FROM USER_INFO t where t.GROUPS_ARR = :nhomQuyen OR t.GROUPS_ARR like (SELECT CONCAT(CONCAT ('%',:nhomQuyen),',%') as nhom_quyen from dual)", nativeQuery = true)
	long countNhom(String nhomQuyen);

}
