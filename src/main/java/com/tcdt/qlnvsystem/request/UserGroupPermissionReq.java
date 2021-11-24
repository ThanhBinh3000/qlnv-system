package com.tcdt.qlnvsystem.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGroupPermissionReq extends BaseRequest{
	long id;
	long groupId;
	long moduleId;
}
