package com.tcdt.qlnvsystem.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tcdt.qlnvsystem.enums.EnumResponse;
import com.tcdt.qlnvsystem.repository.SysPermissionRepository;
import com.tcdt.qlnvsystem.request.SysPermissionRq;
import com.tcdt.qlnvsystem.table.SysPermission;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.tcdt.qlnvsystem.request.IdSearchReq;
import com.tcdt.qlnvsystem.response.Resp;
import com.tcdt.qlnvsystem.util.Contains;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Slf4j
@RestController
@RequestMapping("/quyen")
@Api(tags = "Chức năng người sử dụng")
public class SysPermissionController extends BaseController{

	@Autowired
	private SysPermissionRepository sysPermissionRepository;

	@ApiOperation(value = "Lấy danh sách chức năng theo user", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/ds-quyen-by-user")
	@ResponseStatus(HttpStatus.OK)
	public String select(@RequestBody String username) {
		Resp resp = new Resp();
		try {
			Iterable<SysPermission> data = this.sysPermissionRepository.findByUser(username);
			resp.setData(data);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return new Gson().toJson(resp);
	}
	@ApiOperation(value = "Lấy danh sách chức năng hệ thống", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/ds-quyen", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> selectAll() {
		Resp resp = new Resp();
		try {
			Iterable<SysPermission> data = sysPermissionRepository.findAllOrderById();
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

	@ApiOperation(value = "Tạo mới chức năng hệ thống", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/create")
	public ResponseEntity<Resp> create(@Valid @RequestBody SysPermissionRq req) {
		Resp resp = new Resp();
		try {
			SysPermission dataMap = new ModelMapper().map(req, SysPermission.class);
			if(req.getParentId() != null) {
				Optional<SysPermission> parent = this.sysPermissionRepository.findById(req.getParentId());
				if(parent.isPresent()) {
					dataMap.setParent(parent.get());
				}
			}

			SysPermission dataInfo = this.sysPermissionRepository.save(dataMap);
			resp.setData(dataInfo);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy thông tin chức năng", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/find")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> select(@Valid @RequestBody IdSearchReq req) {
		Resp resp = new Resp();
		try {
			if (req.getId() == null)
				throw new UnsupportedOperationException("Không tìm thấy chức năng");
			Optional<SysPermission> param = sysPermissionRepository.findById(req.getId());
			if (param == null)
				throw new UnsupportedOperationException("Không tìm thấy chức năng");
			resp.setData(param);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật thông tin chức năng", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/update")
	public ResponseEntity<Resp> modify(@RequestBody SysPermissionRq req) {
		Resp resp = new Resp();
		try {
			Optional<SysPermission> info = this.sysPermissionRepository.findById(req.getId());
			if (info.isPresent())
				throw new UnsupportedOperationException("Không tìm thấy chức năng");
			SysPermission dataMap = new ModelMapper().map(req, SysPermission.class);
			updateObjectToObject(info.get(), dataMap);

			if(req.getParentId() != null) {
				Optional<SysPermission> parent = this.sysPermissionRepository.findById(req.getParentId());
				if(parent.isPresent()) {
					info.get().setParent(parent.get());
				}
			}else {
				info.get().setParent(null);
			}

			SysPermission dataInfo = this.sysPermissionRepository.save(dataMap);
			resp.setData(dataInfo);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	@ApiOperation(value = "Kích hoạt/ Tạm dừng chức năng", response = List.class)
	@PostMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resp> updateStatus(@RequestBody IdSearchReq id) {
		Resp resp = new Resp();
		try {
			if (StringUtils.isEmpty(id.getId()))
				throw new Exception("Không tìm thấy chức năng");

			Optional<SysPermission> userModule = sysPermissionRepository.findById(Long.valueOf(id.getId()));
			if (!userModule.isPresent())
				throw new Exception("Không tìm thấy chức năng");
			String status = userModule.get().getTrangThai();
			userModule.get().setTrangThai(status.equals(Contains.HOAT_DONG) ? Contains.NGUNG_HOAT_DONG : Contains.HOAT_DONG);
			sysPermissionRepository.save(userModule.get());
			//sysPermissionRepository.updateStatusChild(status.equals(Contains.HOAT_DONG) ? Contains.NGUNG_HOAT_DONG : Contains.HOAT_DONG,userModule.get().getId());
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