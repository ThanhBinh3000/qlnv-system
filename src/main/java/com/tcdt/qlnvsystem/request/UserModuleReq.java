package com.tcdt.qlnvsystem.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModuleReq extends BaseRequest{
	@ApiModelProperty(notes = "Bắt buộc set đối với update, delete")
	Long id;
	String name;
	String status;
	String icon;
	String url;
	Long place;
	String isShow;
	String data;
	Long parentId;
}
