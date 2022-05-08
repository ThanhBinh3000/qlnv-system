package com.tcdt.qlnvsystem.table.catalog;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "DM_DONVI")
@Data
public class QlnvDmDonvi implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QLNV_DM_COMMON_SEQ")
	@SequenceGenerator(sequenceName = "QLNV_DM_COMMON_SEQ", allocationSize = 1, name = "QLNV_DM_COMMON_SEQ")
	private Long id;
	String maDvi;
//	String maDviCha;
	String tenDvi;
	String maHchinh;
	String maTinh;
	String maQuan;
	String maPhuong;
	String diaChi;
	String capDvi;
	String kieuDvi;
	String loaiDvi;
	String ghiChu;
	String trangThai;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "maDviCha", referencedColumnName = "maDvi")
	private QlnvDmDonvi parent;

}
