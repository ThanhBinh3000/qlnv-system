package com.tcdt.qlnvsystem.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class UserRolesReq extends BaseRequest {
	@NotNull(message = "userId Không được để trống")
	long userId;
	@NotNull(message = "roleId Không được để trống")
	long roleId;
	String status;
}
