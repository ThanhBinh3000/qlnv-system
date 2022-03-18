package com.tcdt.qlnvsystem.request;

import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class SysPermissionRq {
	private Long id;
	@Column(length = 250)
	private String name;
	@Column(length = 50)
	private String code;
	@Column(length = 255)
	private String url;
	@Column
	private Integer thuTu;
	@Column(length = 2)
	private String trangThai;
	@Column(length = 2)
	private String menu;
	@Column(length = 50)
	private String icon;
	private Long parentId;
}