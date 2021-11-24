package com.tcdt.qlnvsystem.util;

import java.util.Map;

public class Pagination {
	public static final int  DEFAULT_LIMIT = 20;
	public static final int  DEFAULT_PAGE = 0;
	
	public static int getPage(Map<String,String> allRequestParams) {
		if(allRequestParams.get("page") != null && !allRequestParams.get("page").trim().equals("")) {
			return Integer.valueOf(allRequestParams.get("page").toString()) - 1;
		}
		return DEFAULT_PAGE;
	}
	
	public static int getLimit(Map<String,String> allRequestParams) {
		if(allRequestParams.get("limit") != null  && !allRequestParams.get("limit").trim().equals("")) {
			return Integer.valueOf(allRequestParams.get("limit").toString());
		}
		return DEFAULT_LIMIT;
	}
}
