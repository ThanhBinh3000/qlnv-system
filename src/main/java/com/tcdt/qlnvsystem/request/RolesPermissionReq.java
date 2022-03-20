package com.tcdt.qlnvsystem.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolesPermissionReq extends BaseRequest{
	long id;
	long roleId;
	long permissionId;
}
