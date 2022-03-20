package com.tcdt.qlnvsystem.table;

import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.tcdt.qlnvsystem.entities.SysPermissionEntity;
import lombok.Data;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "ROLES")
@Data
public class Roles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 250)
	private String name;
	@Column(length = 50)
	private String code;
	@Column(length = 2)
	private String status;

	@Transient
	private List<RolesPermission> rolesPermission;
}