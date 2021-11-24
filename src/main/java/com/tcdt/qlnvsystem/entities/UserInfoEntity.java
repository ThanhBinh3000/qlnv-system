package com.tcdt.qlnvsystem.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class UserInfoEntity {
	@Id
	Long id;
	String username;
	String fullName;
	String status;
	String dvql;
	String token;
	String sysType;
	Long groupId;
	Timestamp createTime;
	String createBy;
	String email;
	String idcard;
	Long chucvuId;
	String phoneNo;
	String title;
	String description;
	String region;
	String department;
	long notifyViewId;
	String groupsArr;
	String groupName;
	String dvqlName;
}