package com.tcdt.qlnvsystem.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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

import com.google.gson.Gson;
import com.tcdt.qlnvsystem.repository.UserModuleRepository;
import com.tcdt.qlnvsystem.request.IdSearchReq;
import com.tcdt.qlnvsystem.request.UserModuleReq;
import com.tcdt.qlnvsystem.request.UserModuleSearchReq;
import com.tcdt.qlnvsystem.response.Resp;
import com.tcdt.qlnvsystem.table.UserModule;
import com.tcdt.qlnvsystem.util.Contains;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/module")
@Api(tags = "Chức năng người sử dụng")
public class UserModuleController {

	@Autowired
	private UserModuleRepository userModuleRepository;
	

	@PostMapping(value = "/userModule", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public String select(@RequestBody String username) {
		Resp resp = new Resp();
		try {
			Iterable<UserModule> data = this.userModuleRepository.findByUsername(username);
			resp.setData(data);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return new Gson().toJson(resp);
	}
	@ApiOperation(value = "Lấy danh sách chức năng hệ thống", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/findList")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> searchParams(@RequestBody UserModuleSearchReq req) {
		Resp resp = new Resp();
		try {
//			int page = PaginationSet.getPage(req.getPaggingReq().getPage());
//			int limit = PaginationSet.getLimit(req.getPaggingReq().getLimit());
//			Pageable pageable = PageRequest.of(page, limit);
			Iterable<UserModule> data = userModuleRepository.selectParams(req.getName(), req.getStatus(),req.getUrl(),req.getIsShow(),
					req.getData(),req.getPlace(),req.getParentId());
			resp.setData(data);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			e.printStackTrace();
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Tạo mới chức năng hệ thống", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/create")
	public ResponseEntity<Resp> create(@Valid @RequestBody UserModuleReq req) {
		Resp resp = new Resp();
		try {
			UserModule info = new UserModule();
			info.setName(req.getName());
			info.setUrl(req.getUrl());
			info.setStatus(req.getStatus());
			info.setPlace(req.getPlace());
			info.setData(req.getData());
			info.setIcon(req.getIcon());
			info.setIsShow(req.getIsShow());
			if(req.getParentId() != null) {
				Optional<UserModule> parent = this.userModuleRepository.findById(req.getParentId());
				if(parent.isPresent()) {
					info.setParent(parent.get());
				}
			}
			
			UserModule dataInfo = this.userModuleRepository.save(info);

			int data = 0;
			if (dataInfo.getId() > 0)
				data = 1;
			resp.setData(data);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			resp.setData(e.getLocalizedMessage());
			e.printStackTrace();
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
			Optional<UserModule> param = userModuleRepository.findById(req.getId());
			if (param == null)
				throw new UnsupportedOperationException("Không tìm thấy chức năng");
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setData(param);
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			resp.setData(e.getLocalizedMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật thông tin chức năng", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/update")
	public ResponseEntity<Resp> modify(@RequestBody UserModuleReq req) {
		Resp resp = new Resp();
		try {
			Optional<UserModule> info = this.userModuleRepository.findById(req.getId());
			if (info.isPresent())
				throw new UnsupportedOperationException("Không tìm thấy chức năng");

			info.get().setName(req.getName());
			info.get().setUrl(req.getUrl());
			info.get().setStatus(req.getStatus());
			info.get().setPlace(req.getPlace());
			info.get().setData(req.getData());
			info.get().setIcon(req.getIcon());
			info.get().setIsShow(req.getIsShow());
			if(req.getParentId() != null) {
				Optional<UserModule> parent = this.userModuleRepository.findById(req.getParentId());
				if(parent.isPresent()) {
					info.get().setParent(parent.get());
				}
			}else {
				info.get().setParent(null);
			}

			int data = 0;
			UserModule dataInfo = this.userModuleRepository.save(info.get());
			if (dataInfo.getId() > 0)
				data = 1;
			resp.setData(data);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			resp.setData(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return ResponseEntity.ok(resp);
	}
	@PostMapping(value = "/findAll", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> collect() {
		Resp resp = new Resp();
		try {
			Iterable<UserModule> data = this.userModuleRepository.findAllOrderBy();
			resp.setData(data);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
			e.printStackTrace();
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

			Optional<UserModule> userModule = userModuleRepository.findById(Long.valueOf(id.getId()));
			if (!userModule.isPresent())
				throw new Exception("Không tìm thấy chức năng");
			String status = userModule.get().getStatus();
			userModule.get().setStatus(status.equals(Contains.HOAT_DONG) ? Contains.NGUNG_HOAT_DONG : Contains.HOAT_DONG);
			userModuleRepository.save(userModule.get());
			userModuleRepository.updateStatusChild(status.equals(Contains.HOAT_DONG) ? Contains.NGUNG_HOAT_DONG : Contains.HOAT_DONG,userModule.get().getId());
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}