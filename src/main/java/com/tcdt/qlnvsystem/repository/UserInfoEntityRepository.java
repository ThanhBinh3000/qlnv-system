package com.tcdt.qlnvsystem.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tcdt.qlnvsystem.entities.UserInfoEntity;



public interface UserInfoEntityRepository extends CrudRepository<UserInfoEntity, Long> {

	@Query(value = "SELECT t.*, (select get_ten_nhom(t.groups_arr) from dual) as group_name, (select ten_dvi from qlnv_dm_donvi where id = t.dvql) as dvql_name FROM USER_INFO t WHERE (:userName is null or lower(t.USERNAME) like lower(concat(concat('%', :userName),'%'))) "
			+ " AND (:fullName is null or lower(t.FULL_NAME) like lower(concat(concat('%', :fullName),'%'))) AND (:trangThai is null or t.STATUS = :trangThai) AND (:sysType is null or t.SYS_TYPE = :sysType) AND (:maDv is null or t.DVQL = to_number(:maDv)) AND (:nhomQuyen is null or (t.GROUPS_ARR = :nhomQuyen OR ("
			+ " select count(*) from user_group g where g.id in ("
			+ " SELECT regexp_substr(t.groups_arr ,'[^,]+', 1, level) as id FROM dual CONNECT BY regexp_substr(t.groups_arr  , '[^,]+', 1, level) IS NOT NULL "
			+ ")" + "and g.id in ("
			+ "  SELECT regexp_substr(:nhomQuyen ,'[^,]+', 1, level) as id FROM dual CONNECT BY regexp_substr(:nhomQuyen, '[^,]+', 1, level) IS NOT NULL "
			+ ")"
			+ ") > 0)) ORDER BY t.ID DESC", countQuery = "SELECT count(1) FROM USER_INFO t WHERE (:userName is null or lower(t.USERNAME) like lower(concat(concat('%', :userName),'%'))) "
					+ " AND (:fullName is null or lower(t.FULL_NAME) like lower(concat(concat('%', :fullName),'%'))) AND (:trangThai is null or t.STATUS = :trangThai) AND (:sysType is null or t.SYS_TYPE = :sysType) AND (:maDv is null or t.DVQL = to_number(:maDv)) AND (:nhomQuyen is null or (t.GROUPS_ARR = :nhomQuyen OR ("
					+ " select count(*) from user_group g where g.id in ("
					+ " SELECT regexp_substr(t.groups_arr ,'[^,]+', 1, level) as id FROM dual CONNECT BY regexp_substr(t.groups_arr  , '[^,]+', 1, level) IS NOT NULL"
					+ ")" + "and g.id in ("
					+ "  SELECT regexp_substr(:nhomQuyen ,'[^,]+', 1, level) as id FROM dual CONNECT BY regexp_substr(:nhomQuyen, '[^,]+', 1, level) IS NOT NULL "
					+ ")" + ") > 0))", nativeQuery = true)
	Page<UserInfoEntity> selectParams(String userName, String fullName,String sysType, String maDv, String nhomQuyen, String trangThai,
			Pageable pageable);

	@Query(value = "SELECT t.*, c.group_name as group_name FROM USER_INFO t,user_group c WHERE t.group_id = c.id AND LOWER(t.username) = LOWER(:userName)", nativeQuery = true)
	UserInfoEntity findByUsername(String userName);

	long countByGroupId(Long groupId);

	@Query(value = "SELECT t.*, (select get_ten_nhom(t.groups_arr) from dual) as group_name, (select ten_dvi from qlnv_dm_donvi where id = t.dvql) as dvql_name FROM USER_INFO t WHERE (:userName is null or lower(t.USERNAME) like lower(concat(concat('%', :userName),'%'))) "
			+ " AND (:fullName is null or lower(t.FULL_NAME) like lower(concat(concat('%', :fullName),'%'))) AND (:trangThai is null or t.STATUS = :trangThai) AND (:sysType is null or t.SYS_TYPE = :sysType) AND (:maDv is null or t.DVQL = to_number(:maDv)) AND (:nhomQuyen is null or (t.GROUPS_ARR = :nhomQuyen OR ("
			+ " select count(*) from user_group g where g.id in ("
			+ " SELECT regexp_substr(t.groups_arr ,'[^,]+', 1, level) as id FROM dual CONNECT BY regexp_substr(t.groups_arr  , '[^,]+', 1, level) IS NOT NULL "
			+ ")" + "and g.id in ("
			+ "  SELECT regexp_substr(:nhomQuyen ,'[^,]+', 1, level) as id FROM dual CONNECT BY regexp_substr(:nhomQuyen, '[^,]+', 1, level) IS NOT NULL "
			+ ")" + ") > 0)) ORDER BY t.ID DESC", nativeQuery = true)
	List<UserInfoEntity> selectParams(String userName, String fullName,String sysType, String maDv, String nhomQuyen, String trangThai);
}
