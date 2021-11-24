package com.tcdt.qlnvsystem.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryRequest extends BaseRequest {
	String username;
	String tuNgay;
	String denNgay;
	Integer limit;
	Integer page;
}
