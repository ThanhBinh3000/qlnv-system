package com.tcdt.qlnvsystem.table;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "QLNV_DM")
@Data
public class QlnvDanhMuc implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_DM_COMMON_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_DM_COMMON_SEQ", allocationSize = 1, name = "QLNV_DM_COMMON_SEQ")
	private Long id;

	String ma;
	String maCha;
	String giaTri;
	String ghiChu;
	String trangThai;
	String nguoiTao;
	Date ngayTao;
	String nguoiSua;
	Date ngaySua;
	String loai;

}
