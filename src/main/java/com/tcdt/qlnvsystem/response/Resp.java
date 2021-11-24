package com.tcdt.qlnvsystem.response;

import lombok.Data;

@Data
public class Resp {
	Object data;
	int statusCode;//0: succ <>0: fail
	String msg;
	Object included;
}