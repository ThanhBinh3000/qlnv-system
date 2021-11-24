package com.tcdt.qlnvsystem.request;

import lombok.Data;

@Data
public class UserSearchReq {
	String sysType;
	String username;
	String fullName;
	String status;
	String dvql;
	String groupId;
	String groupsArr;
	Integer limit;
	Integer page;
	
}
