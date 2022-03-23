package com.tcdt.qlnvsystem.service.feign;

import com.tcdt.qlnvsystem.request.BaseRequest;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "qlnv-category")
public interface CategoryServiceProxy {

	@GetMapping("/dmuc-donvi/chi-tiet/{ids}")
	@Headers({ "Accept: application/json; charset=utf-8", "Content-Type: application/x-www-form-urlencoded" })
	public ResponseEntity<String> getDetail(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@PathVariable("ids") String ids);

	@PostMapping("/dmuc-donvi/chi-tiet")
	@Headers({ "Accept: application/json; charset=utf-8", "Content-Type: application/x-www-form-urlencoded" })
	public ResponseEntity<String> getDetailByCode(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody BaseRequest objReq);

}
