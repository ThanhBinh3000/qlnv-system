package com.tcdt.qlnvsystem.util;

import java.text.SimpleDateFormat;

public class DateFormat {
	public static String dateStr(String dateStr) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat dateFormatReturn = new SimpleDateFormat("yyyyMMdd");
			
			return dateFormatReturn.format(dateFormat.parse(dateStr));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
}
