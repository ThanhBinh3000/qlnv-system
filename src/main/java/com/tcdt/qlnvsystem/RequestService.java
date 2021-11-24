package com.tcdt.qlnvsystem;

import javax.servlet.http.HttpServletRequest;

public interface RequestService {

	String getClientIp(HttpServletRequest request);

}