package com.tcdt.qlnvsystem.table;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_HISTORY")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_HISTORY_SEQ")
	@SequenceGenerator(sequenceName = "USER_HISTORY_SEQ", allocationSize = 1, name = "USER_HISTORY_SEQ")
	private Long id;
	Date timeaction;
	byte[] content;
	String ipaddress;
	String note;
	String username;
	String organization;
	String action;
}
