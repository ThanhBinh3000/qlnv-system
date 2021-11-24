package com.tcdt.qlnvsystem.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGroupReq extends BaseRequest {
	@ApiModelProperty(notes = "Bắt buộc set đối với update, delete")
	long id;
	@NotNull(message = "Không được để trống")
	@Size(max = 200, message = "Mã nhóm không được vượt quá 200 ký tự")
	String groupCode;
	@NotNull(message = "Không được để trống")
	@Size(max = 200, message = "Tên nhóm không được vượt quá 200 ký tự")
	String groupName;
	@Size(max = 500, message = "Mô tả nhóm không được vượt quá 200 ký tự")
	String description;
	String status;
	String data;
	String dataFt;
	List<UserGroupPermissionReq> groupPermissionsReq;

}
