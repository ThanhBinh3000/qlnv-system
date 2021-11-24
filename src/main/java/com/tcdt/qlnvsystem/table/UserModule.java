package com.tcdt.qlnvsystem.table;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.Data;

@Entity
@Table(name = "USER_MODULE")
@Data
public class UserModule {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_MODULE_SEQ")
	@SequenceGenerator(sequenceName = "USER_MODULE_SEQ", allocationSize = 1, name = "USER_MODULE_SEQ")
	private Long id;

	String name;
	String status;
	String url;
	long place;
	String icon;
	String isShow;
	String data;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name="parent_id", referencedColumnName="id")
    private UserModule parent;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="parent_id", referencedColumnName="id")
    private List<UserModule> child;
}
