package com.tcdt.qlnvsystem.table;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "USER_INFO")
@Data
public class UserInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_INFO_SEQ")
	@SequenceGenerator(sequenceName = "USER_INFO_SEQ", allocationSize = 1, name = "USER_INFO_SEQ")
	Long id;
	String username;
	String password;
	String fullName;
	String status;
	Long dvql;
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
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles;
}
