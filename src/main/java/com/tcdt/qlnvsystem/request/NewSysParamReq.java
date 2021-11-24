package com.tcdt.qlnvsystem.request;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewSysParamReq extends BaseRequest {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	Long id;
	@NotNull(message = "Không được để trống")
	String paramId;
	@NotNull(message = "Không được để trống")
	String paramName;
	@NotNull(message = "Không được để trống")
	String paramValue;
	String status;
	String description;
}
