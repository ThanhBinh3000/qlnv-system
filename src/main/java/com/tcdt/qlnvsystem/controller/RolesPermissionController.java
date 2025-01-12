package com.tcdt.qlnvsystem.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.tcdt.qlnvsystem.request.RolesPermissionReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tcdt.qlnvsystem.entities.RolesPermissionEntity;
import com.tcdt.qlnvsystem.enums.EnumResponse;
import com.tcdt.qlnvsystem.repository.RolesPermissionEntityRepository;
import com.tcdt.qlnvsystem.repository.SysPermissionRepository;
import com.tcdt.qlnvsystem.repository.RolesRepository;
import com.tcdt.qlnvsystem.request.IdSearchReq;
import com.tcdt.qlnvsystem.response.Resp;
import com.tcdt.qlnvsystem.table.Roles;
import com.tcdt.qlnvsystem.table.SysPermission;
import com.tcdt.qlnvsystem.util.Contains;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
@Slf4j
@Api(tags = "Quản lý vai trò người sử dụng")
public class RolesPermissionController extends BaseController {
	@Autowired
	private RolesRepository rolesRepository;

	@Autowired
	private SysPermissionRepository rolesPermissionRepository;
	
	@Autowired
	private RolesPermissionEntityRepository rolesPermissionEntityRepository;


	@ApiOperation(value = "Danh sách vai trò hệ thống", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/findAll", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> findAllRole() {
		Resp resp = new Resp();
		try {
			Iterable<Roles> data = this.rolesRepository.findAll();
			resp.setData(data);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	
	
	@PostMapping(value = "/findByUser", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Danh sách quyền hệ thống theo user", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> findByUser(HttpServletRequest request) {
		Resp resp = new Resp();
		try {
			Iterable<RolesPermissionEntity> data = this.rolesPermissionEntityRepository.findByUser(getUserName(request));
			resp.setData(data);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}
