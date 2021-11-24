package com.tcdt.qlnvsystem.request;



import lombok.Data;

@Data
public class UserModuleSearchReq {
	String name;
	String status;
	String url;
	Long place;
	String isShow;
	String data;
	Long parentId;
	//PaggingReq paggingReq;
}
