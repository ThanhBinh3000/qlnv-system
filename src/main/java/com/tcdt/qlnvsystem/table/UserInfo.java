package com.tcdt.qlnvsystem.table;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.*;

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

	@ManyToMany(cascade = {
			CascadeType.PERSIST,
			CascadeType.MERGE
	})
	@JoinTable(name = "user_roles",
			joinColumns =@JoinColumn(name = "userId"),
			inverseJoinColumns = @JoinColumn(name = "roleId"
			))
	private Set<Roles> roles;
}
