package com.tcdt.qlnvsystem.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ROLES_PERMISSION")
@Data
public class RolesPermission {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLES_PERMISSION_SEQ")
	@SequenceGenerator(sequenceName = "ROLES_PERMISSION_SEQ", allocationSize = 1, name = "ROLES_PERMISSION_SEQ")
	private Long id;
	@Column(name = "role_id")
	private Long roleId;
	@Column(name = "PERMISSION_ID")
	private Long permissionId;
}
