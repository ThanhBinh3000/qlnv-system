package com.tcdt.qlnvsystem.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolesReq extends BaseRequest {
	@ApiModelProperty(notes = "Bắt buộc set đối với update, delete")
	long id;
	@NotNull(message = "Không được để trống")
	@Size(max = 200, message = "Mã nhóm không được vượt quá 200 ký tự")
	String code;
	@NotNull(message = "Không được để trống")
	@Size(max = 200, message = "Tên nhóm không được vượt quá 200 ký tự")
	String name;
	String status;
	List<RolesPermissionReq> rolePermissionsReq;

}
