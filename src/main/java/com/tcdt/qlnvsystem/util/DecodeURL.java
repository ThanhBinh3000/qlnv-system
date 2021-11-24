package com.tcdt.qlnvsystem.util;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.StringUtils;

public class DecodeURL {

	public static String decode(Map<String, String> params, String field) throws Exception {
		return StringUtils.isEmpty(params.get(field)) ? null : URLDecoder.decode(params.get(field), "UTF-8");
	}

	public static Map<String, String> decodeMap(Map<String, String> params) throws Exception {
		Map<String, String> map = new HashMap<>();
		for (Entry<String, String> e : params.entrySet()) {
			map.put(e.getKey(), URLDecoder.decode(e.getValue(), "UTF-8"));
		}
		return map;
	}
}