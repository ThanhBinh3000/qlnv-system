package com.tcdt.qlnvsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class SysPermissionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	Long parent_id;

	public SysPermissionEntity(Long id, String name, String code, String url, Integer thuTu, String trangThai, String menu, String icon) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.url = url;
		this.thuTu = thuTu;
		this.trangThai = trangThai;
		this.menu = menu;
		this.icon = icon;
	}

	public SysPermissionEntity() {

	}
}
