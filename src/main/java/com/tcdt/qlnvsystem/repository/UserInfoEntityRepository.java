package com.tcdt.qlnvsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tcdt.qlnvsystem.entities.UserInfoEntity;



public interface UserInfoEntityRepository extends CrudRepository<UserInfoEntity, Long> {

	String qr="select u.id,u.username,u.full_name,u.status,u.sys_type,to_char(u.create_time,'dd/MM/yyyy') create_time,u.create_by,u.email,u.phone_no,dv.ten_dvi dvql_name from user_info u left join qlnv_dm_donvi dv on u.dvql = dv.id  where (:userName is null or lower(t.USERNAME) like lower(concat(concat('%', :userName),'%'))) AND (:fullName is null or lower(t.FULL_NAME) like lower(concat(concat('%', :fullName),'%'))) AND (:trangThai is null or u.STATUS = :trangThai) AND (:sysType is null or u.SYS_TYPE = :sysType) AND (:dvql is null or u.DVQL = to_number(:dvql)) ";
	String qrCount="select count(u.*) from user_info u left join qlnv_dm_donvi dv on u.dvql = dv.id where (:userName is null or lower(t.USERNAME) like lower(concat(concat('%', :userName),'%'))) AND (:fullName is null or lower(t.FULL_NAME) like lower(concat(concat('%', :fullName),'%'))) AND (:trangThai is null or u.STATUS = :trangThai) AND (:sysType is null or u.SYS_TYPE = :sysType) AND (:dvql is null or u.DVQL = to_number(:dvql)) ";
	@Query(value = qr,countQuery = qrCount, nativeQuery = true)

	List<UserInfoEntity> selectParams(String userName, String fullName,String sysType, String dvql, String trangThai);
}
