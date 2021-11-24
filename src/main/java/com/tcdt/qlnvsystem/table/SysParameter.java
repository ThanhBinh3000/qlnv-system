package com.tcdt.qlnvsystem.table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "SYS_PARAMETER")
@Data
public class SysParameter {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SYS_PARAMETER_SEQ")
	@SequenceGenerator(sequenceName = "SYS_PARAMETER_SEQ", allocationSize = 1, name = "SYS_PARAMETER_SEQ")
	private Long id;
	String ma;
	String ten;
	String giaTri;
	String loai;
	String trangThai;
	String moTa;
}
