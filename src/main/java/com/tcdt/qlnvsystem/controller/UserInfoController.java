package com.tcdt.qlnvsystem.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.tcdt.qlnvsystem.request.UserRolesReq;
import com.tcdt.qlnvsystem.service.UserService;
import com.tcdt.qlnvsystem.table.UserRoles;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tcdt.qlnvsystem.entities.UserInfoEntity;
import com.tcdt.qlnvsystem.enums.EnumResponse;
import com.tcdt.qlnvsystem.request.BaseRequest;
import com.tcdt.qlnvsystem.request.UserInfoReq;
import com.tcdt.qlnvsystem.request.UserSearchReq;
import com.tcdt.qlnvsystem.response.Resp;
import com.tcdt.qlnvsystem.table.UserInfo;
import com.tcdt.qlnvsystem.util.Contains;
import com.tcdt.qlnvsystem.util.ExportExcel;
import com.tcdt.qlnvsystem.util.PaginationSet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
@Api(tags = "Quản lý người sử dụng")
public class UserInfoController extends BaseController {

	@Autowired
	private UserService userService;

	@ApiOperation(value = "Lấy thông tin UserInfo", response = List.class)
	@PostMapping(value = "/userInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> selectUserInfo(@RequestBody BaseRequest str) {
		Resp resp = new Resp();
		try {
			if (org.apache.commons.lang3.StringUtils.isEmpty(str.getStr()))
				throw new Exception("Tên tài khoản không được để trống");
			UserInfo user = userService.selectUserInfo(str);
			if (user == null)
				throw new Exception("Tài khoản không tồn tại");
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
			resp.setData(user);
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@GetMapping(value = "/resetPassword", produces = MediaType.APPLICATION_JSON_VALUE)
	public String resetPassword(@RequestParam String password, @RequestParam String token) {
		Resp resp = new Resp();
		try {
			userService.resetPassword(password,token);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return new Gson().toJson(resp);
	}

	@GetMapping(value = "/changePassword", produces = MediaType.APPLICATION_JSON_VALUE)
	public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
			HttpServletRequest request) {
		Resp resp = new Resp();
		try {
			userService.changePassword(oldPassword,newPassword,request);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return new Gson().toJson(resp);
	}

	@ApiOperation(value = "Tạo mới UserInfo", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/create")
	public ResponseEntity<Resp> create(@Valid @RequestBody UserInfoReq req, HttpServletRequest request) {
		Resp resp = new Resp();
		try {
			resp.setData(userService.create(req,request));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật UserInfo", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/update")
	public ResponseEntity<Resp> modify(@RequestBody UserInfoReq req) {
		Resp resp = new Resp();
		try {
			resp.setData(userService.modify(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xóa UserInfo", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/delete")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('SYS_USER_DEL')")
	//TODO: ten chuc nang + hanh dong
	public ResponseEntity<Resp> delete(@RequestBody BaseRequest str) throws Exception {
		Resp resp = new Resp();
		try {
			userService.delete(str);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tra cứu danh sách User", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/findList")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> search(@RequestBody UserSearchReq req) {
		Resp resp = new Resp();
		try {
			resp.setData(userService.search(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Kích hoạt/Tạm dừng tài khoản nsd", response = List.class)
	@PostMapping(value = "/updateStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public String toggle(@RequestBody BaseRequest str) {
		Resp resp = new Resp();
		try {
			resp.setData(userService.toggle(str));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return new Gson().toJson(resp);
	}

	@ApiOperation(value = "Tra cứu danh sách kết xuất excel", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/exportExcel")
	@ResponseStatus(HttpStatus.OK)
	public void exportToExcel(@RequestBody UserSearchReq req, HttpServletResponse response) throws Exception {
		try {
			userService.exportToExcel(req,response);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Tra cứu danh sách kết xuất excel", e);
			final Map<String, Object> body = new HashMap<>();
			body.put("statusCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			body.put("msg", e.getMessage());

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");

			final ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), body);
		}
	}

	@ApiOperation(value = "Thêm quyền cho user", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/them-quyen")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> addRole(@RequestBody UserRolesReq req) throws Exception {
		Resp resp = new Resp();
		try {
			resp.setData(userService.addRole(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xóa quyền cho user", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/xoa-quyen")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> removeRole(@RequestBody UserRolesReq req) throws Exception {
		Resp resp = new Resp();
		try {
			userService.removeRole(req);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}
	@ApiOperation(value = "Kích hoạt/Tạm dừng tài quyền của user", response = List.class)
	@PostMapping(value = "/active-quyen", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> activeRole(@RequestBody UserRolesReq req) {

		Resp resp = new Resp();
		try {
			resp.setData(userService.activeRole(req));
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}