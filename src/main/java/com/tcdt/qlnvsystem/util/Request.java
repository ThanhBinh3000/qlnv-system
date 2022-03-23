package com.tcdt.qlnvsystem.util;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;

public class Request {
	public static String get(UriComponentsBuilder builder, String authorization) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", authorization);
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

		ResponseEntity<String> response;

		response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);

		return response.getBody();
	}

	public static String post(String json, String authorization, String url) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", authorization);

		HttpEntity<String> entity = new HttpEntity<String>(json, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		return response.getBody();
	}

	public static String get(UriComponentsBuilder builder) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

		ResponseEntity<String> response;

		response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);

		return response.getBody();
	}

	public static String post(Map<String, Object> map, String authorization, String url) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", authorization);

		HttpEntity<Object> entity = new HttpEntity<Object>(map, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		return response.getBody();
	}

	public static String put(Map<String, Object> map, String authorization, String url) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", authorization);

		HttpEntity<Object> entity = new HttpEntity<Object>(map, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

		return response.getBody();
	}

	public static String put(String json, String authorization, String url) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", authorization);

		HttpEntity<String> entity = new HttpEntity<String>(json, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

		return response.getBody();
	}

	public static String delete(UriComponentsBuilder builder, String authorization) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", authorization);
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

		ResponseEntity<String> response;

		response = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, entity, String.class);

		return response.getBody();
	}

	public static String put(UriComponentsBuilder builder, String authorization) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", authorization);
		HttpEntity<Object> entity = new HttpEntity<Object>(headers);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

		ResponseEntity<String> response;

		response = restTemplate.exchange(builder.toUriString(), HttpMethod.PUT, entity, String.class);

		return response.getBody();
	}

	public static <T> T getList(String json, Type type, String... attr) {
		String str = "";
		try {
			for (int i = 0; i < attr.length; i++) {
				JSONObject jsonObject = new JSONObject(json);
				json = jsonObject.get(attr[i]).toString();

				if (i == (attr.length - 1)) {
					str = jsonObject.get(attr[i]).toString();
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new Gson().fromJson(str, type);
	}

	public static String getAttrFromJson(String json, String... attr) {
		try {
			for (int i = 0; i < attr.length; i++) {
				JSONObject jsonObject = new JSONObject(json);
				json = jsonObject.get(attr[i]).toString();

				if (i == (attr.length - 1)) {
					return jsonObject.get(attr[i]).toString();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;
	}

	public static int getIntFromJson(String json, String... attr) {
		return Integer.valueOf(getAttrFromJson(json, attr));
	}

	public static long getLongFromJson(String json, String... attr) {
		return Long.valueOf(getAttrFromJson(json, attr));
	}

	public static String getContent(String json) {
		return getAttrFromJson(json, "data", "content");
	}

	public static int getTotal(String json) {
		return getIntFromJson(json, "data", "total");
	}

	public static int getStatus(String json) {
		return getIntFromJson(json, "statusCode");
	}
}
