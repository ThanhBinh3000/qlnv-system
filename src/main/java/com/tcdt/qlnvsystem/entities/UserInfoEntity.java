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
	String sysType;
	String createTime;
	String createBy;
	String email;
	String phoneNo;
	String dvqlName;
}