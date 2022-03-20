package com.tcdt.qlnvsystem.table;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "USER_ROLES")
@Data
public class UserRoles implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	Long userId;
	String roleId;
	String status;
}
