package com.tcdt.qlnvsystem.table;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Persister;

@Entity
@Table(name = "SYS_PERMISSION")
@Data
public class SysPermission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SYS_PERMISSION_SEQ")
	@SequenceGenerator(sequenceName = "SYS_PERMISSION_SEQ", allocationSize = 1, name = "SYS_PERMISSION_SEQ")
	private Long id;
    @Transient
    private Long key;
    @Transient
    private String title;
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
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "parent_id", referencedColumnName = "id")
	//@JsonIgnoreProperties(value = {"child"})
	private SysPermission parent;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_id", referencedColumnName = "id")
	//@Fetch(value = FetchMode.SUBSELECT)
	//@JsonIgnoreProperties(value = {"parent"})
	private List<SysPermission> children;

	public Long getKey() {
		return id;
	}
	public String getTitle() {
		return name;
	}
}
