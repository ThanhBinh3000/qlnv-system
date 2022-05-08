package com.tcdt.qlnvsystem.repository;

import com.tcdt.qlnvsystem.table.QlnvDanhMuc;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface DanhMucRepository extends CrudRepository<QlnvDanhMuc, Long> {

	Iterable<QlnvDanhMuc> findByTrangThai(String trangThai);

	@Transactional()
	@Modifying
	@Query(value = "UPDATE DM_DUNG_CHUNG SET GIA_TRI=:shgtNext WHERE ma = :ma", nativeQuery = true)
	void updateVal(String ma, Long shgtNext);

	QlnvDanhMuc findByMa(String ma);

}
