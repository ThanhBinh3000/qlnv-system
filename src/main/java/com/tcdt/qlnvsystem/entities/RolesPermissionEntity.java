package com.tcdt.qlnvsystem.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class RolesPermissionEntity {
	@Id
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
	private Long parent_id;
}
