package com.tcdt.qlnvsystem.table;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name = "USER_GROUP")
@Data
public class UserGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_GROUP_SEQ")
	@SequenceGenerator(sequenceName = "USER_GROUP_SEQ", allocationSize = 1, name = "USER_GROUP_SEQ")
	private Long id;
	String groupCode;
	String groupName;
	String description;
	String status;
	Date createTime;
	String createBy;
	String data;
	String dataFt;
	@Transient
	List<UserGroupPermission> groupPermissionsReq;
}
