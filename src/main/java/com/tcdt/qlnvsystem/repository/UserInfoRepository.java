package com.tcdt.qlnvsystem.repository;

import com.tcdt.qlnvsystem.entities.UserInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	String qr="select u.* from user_info u left join user_roles r on  u.id = r.user_id where (:roleId is null or r.role_id = to_number(:roleId)) and (:userName is null or lower(t.USERNAME) like lower(concat(concat('%', :userName),'%'))) AND (:fullName is null or lower(t.FULL_NAME) like lower(concat(concat('%', :fullName),'%'))) AND (:trangThai is null or u.STATUS = :trangThai) AND (:sysType is null or u.SYS_TYPE = :sysType) AND (:dvql is null or u.DVQL = to_number(:dvql)) ";
	String qrCount="select count(u.*) from user_info u left join user_roles r on  u.id = r.user_id where (:roleId is null or r.role_id = to_number(:roleId)) and (:userName is null or lower(t.USERNAME) like lower(concat(concat('%', :userName),'%'))) AND (:fullName is null or lower(t.FULL_NAME) like lower(concat(concat('%', :fullName),'%'))) AND (:trangThai is null or u.STATUS = :trangThai) AND (:sysType is null or u.SYS_TYPE = :sysType) AND (:dvql is null or u.DVQL = to_number(:dvql)) ";
	@Query(value = qr,countQuery = qrCount, nativeQuery = true)
	Page<UserInfoEntity> selectParams(String roleId,String userName, String fullName, String sysType, String dvql, String trangThai,
									  Pageable pageable);
	@Transactional()
	@Query(value = "SELECT count(*) FROM USER_ROLES t where t.role_id = :roleId", nativeQuery = true)
	long countByRoleId(Long roleId);
}
