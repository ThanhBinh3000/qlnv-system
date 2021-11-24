package com.tcdt.qlnvsystem.table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "USER_AUTHORIZATION")
@Data
public class UserAuthorization {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_AUTHORIZATION_SEQ")
	@SequenceGenerator(sequenceName = "USER_AUTHORIZATION_SEQ", allocationSize = 1, name = "USER_AUTHORIZATION_SEQ")
	private Long id;
	Long userId;
	Long groupId;
}
