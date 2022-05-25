package com.tcdt.qlnvsystem.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoReq{
	@ApiModelProperty(notes = "Bắt buộc set đối với update, delete")
	Long id;
	@NotNull(message = "Không được để trống")
	@Size(min = 6, max = 16, message = "username phải lớn hơn 6 ký tự và ít hơn 16 ký tự")
	String username;
	@NotNull(message = "Không được để trống")
	@Size(min = 8, max = 16, message = "password phải lớn hơn 8 ký tự và ít hơn 16 ký tự")
	String password;
	@NotNull(message = "Không được để trống")
	@Size(min = 8, max = 50, message = "fullName phải lớn hơn 8 ký tự và ít hơn 50 ký tự")
	String fullName;
	String status;
	String dvql;
	String token;
	@NotNull(message = "Không được để trống")
	@Size(max = 5, message = "sysType phải ít hơn 5 ký tự")
	String sysType;
	Long groupId;
	@Email
	String email;
	Long chucvuId;
	String phoneNo;
	String groupsArr;
}
