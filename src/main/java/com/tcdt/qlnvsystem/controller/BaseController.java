package com.tcdt.qlnvsystem.controller;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvsystem.jwt.TokenAuthenticationService;
import com.tcdt.qlnvsystem.util.Contains;

public class BaseController {

	public String getStringParams(Map<String, String> allParams, String nameParam) {
		if (StringUtils.isEmpty(allParams.get(nameParam))) {
			return null;
		}
		return allParams.get(nameParam);
	}

	public Long getLongParams(Map<String, String> allParams, String nameParam) {
		if (StringUtils.isEmpty(allParams.get(nameParam))) {
			return null;
		}
		return Long.valueOf(allParams.get(nameParam));
	}

	public Integer getIntParams(Map<String, String> allParams, String nameParam) {
		if (StringUtils.isEmpty(allParams.get(nameParam))) {
			return null;
		}
		return Integer.valueOf(allParams.get(nameParam));
	}

	private static final List<String> EXTENSIONS = Arrays.asList(".doc", ".docx", ".xls", ".xlsx");

	public String getDvql(HttpServletRequest req) {
		Authentication authentication = TokenAuthenticationService.getAuthentication(req);
		return authentication.getDetails().toString();
	}

	public String getUserName(HttpServletRequest req) {
		Authentication authentication = TokenAuthenticationService.getAuthentication(req);
		return authentication.getName();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T mapToClass(Map data, Class cls) {
		try {
			Object obj = cls.getDeclaredConstructor().newInstance();
			for (Field f : cls.getDeclaredFields()) {
				f.setAccessible(true);
				if (data.get(f.getName()) != null) {
					try {
						f.set(obj, data.get(f.getName()));
					} catch (Exception e) {

					}
				}
			}
			return (T) cls.cast(obj);
		} catch (Exception e) {
		}

		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	<T> void updateMapToObject(Map<String, String> params, T source, Class cls) throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat(Contains.FORMAT_DATE_STR));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Object overrideObj = mapper.convertValue(params, cls);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.updateValue(source, overrideObj);
	}

	public <T> void updateObjectToObject(T source, T objectEdit) throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat(Contains.FORMAT_DATE_STR));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.updateValue(source, objectEdit);
	}

	public Date convertStringToDate(String format, String strDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(strDate);
		} catch (ParseException e) {
		}
		return null;
	}

	public Long convertStringToLong(String format, String strDate) {
		if (strDate == null)
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(strDate).getTime();
		} catch (ParseException e) {
		}
		return null;
	}

	public static boolean isValidFormat(String format, String value, Locale locale) {
		LocalDateTime ldt = null;
		DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);

		try {
			ldt = LocalDateTime.parse(value, fomatter);
			String result = ldt.format(fomatter);
			return result.equals(value);
		} catch (DateTimeParseException e) {
			try {
				LocalDate ld = LocalDate.parse(value, fomatter);
				String result = ld.format(fomatter);
				return result.equals(value);
			} catch (DateTimeParseException exp) {
				try {
					LocalTime lt = LocalTime.parse(value, fomatter);
					String result = lt.format(fomatter);
					return result.equals(value);
				} catch (DateTimeParseException e2) {
					// Debugging purposes
					// e2.printStackTrace();
				}
			}
		}

		return false;
	}

	public String getSort(Map<String, String> allParams) {
		if (allParams.get("sort") == null) {
			return "desc";
		}
		return allParams.get("sort");
	}

	boolean allowExtension(String fileName) {
		for (String ext : EXTENSIONS) {
			if (fileName.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}
}
