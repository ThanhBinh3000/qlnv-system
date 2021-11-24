package com.tcdt.qlnvsystem.table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "USER_GROUP_PERMISSION")
@Data
public class UserGroupPermission {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_GROUP_PERMISSION_SEQ")
	@SequenceGenerator(sequenceName = "USER_GROUP_PERMISSION_SEQ", allocationSize = 1, name = "USER_GROUP_PERMISSION_SEQ")
	private Long id;

	long groupId;
	long moduleId;
}
