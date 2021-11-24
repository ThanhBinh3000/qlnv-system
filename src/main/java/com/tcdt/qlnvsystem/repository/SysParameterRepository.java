package com.tcdt.qlnvsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tcdt.qlnvsystem.table.SysParameter;

public interface SysParameterRepository extends CrudRepository<SysParameter, Long> {

	SysParameter findByTen(String ten);

	SysParameter findByMa(String ma);

	@Query(value = "SELECT * FROM SYS_PARAMETER WHERE (:param is null or lower(TEN) like lower(concat(concat('%', :param),'%'))) AND (:status is null or TRANG_THAI = :status) AND (:paramId is null or MA = :paramId)", countQuery = "SELECT count(1) FROM SYS_PARAMETER WHERE (:param is null or lower(TEN) like lower(concat(concat('%', :param),'%'))) AND (:status is null or TRANG_THAI = :status) AND (:paramId is null or MA = :paramId)", nativeQuery = true)
	Page<SysParameter> selectParams(String param, String status, String paramId, Pageable pageable);

}
