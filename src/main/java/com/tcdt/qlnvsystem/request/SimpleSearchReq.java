package com.tcdt.qlnvsystem.request;

import lombok.Data;

@Data
public class SimpleSearchReq {
	
	Integer limit;
	Integer page;
	String name;
	String code;
}
