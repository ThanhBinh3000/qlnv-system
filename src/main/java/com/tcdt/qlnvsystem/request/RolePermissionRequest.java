package com.tcdt.qlnvsystem.request;

import java.util.List;

import lombok.Data;

@Data
public class RolePermissionRequest {
	String roleId;
	List<String> lstPermission;
}