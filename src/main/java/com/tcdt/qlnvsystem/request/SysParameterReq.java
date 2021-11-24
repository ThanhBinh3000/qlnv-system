package com.tcdt.qlnvsystem.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysParameterReq extends BaseRequest{
	String param;
	String status;
	String paramId;
	Integer limit;
	Integer page;
}
