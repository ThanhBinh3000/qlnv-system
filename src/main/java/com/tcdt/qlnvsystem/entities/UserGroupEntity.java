package com.tcdt.qlnvsystem.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data
public class UserGroupEntity {
	@Id
	Long id;
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
