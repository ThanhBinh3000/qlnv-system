package com.tcdt.qlnvsystem.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.tcdt.qlnvsystem.enums.EnumResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.tcdt.qlnvsystem.repository.SysParameterRepository;
import com.tcdt.qlnvsystem.request.IdSearchReq;
import com.tcdt.qlnvsystem.request.NewSysParamReq;
import com.tcdt.qlnvsystem.request.SysParameterReq;
import com.tcdt.qlnvsystem.response.Resp;
import com.tcdt.qlnvsystem.table.SysParameter;
import com.tcdt.qlnvsystem.util.Contains;
import com.tcdt.qlnvsystem.util.PaginationSet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sysparam")
@Api(tags = "Tham số hệ thống")
public class SysParameterController {

	@Autowired
	private SysParameterRepository sysParameterRepository;

	@ApiOperation(value = "Lấy danh sách tham số hệ thống", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/findList")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> searchParams(@RequestBody SysParameterReq req) {
		Resp resp = new Resp();
		try {
			System.out.println(new Gson().toJson(req));
			int page = PaginationSet.getPage(req.getPage());
			int limit = PaginationSet.getLimit(req.getLimit());
			Pageable pageable = PageRequest.of(page, limit);
			Page<SysParameter> data = sysParameterRepository.selectParams(req.getParam(), req.getStatus(),
					req.getParamId(), pageable);
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

	@ApiOperation(value = "Tạo mới tham số hệ thống", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/create")
	public ResponseEntity<Resp> create(@Valid @RequestBody NewSysParamReq req) {
		Resp resp = new Resp();
		try {
			if (this.sysParameterRepository.findByMa(req.getParamId()) != null)
				throw new UnsupportedOperationException("Mã tham số đã tồn tại");

			if (this.sysParameterRepository.findByTen(req.getParamName()) != null)
				throw new UnsupportedOperationException("Tên tham số đã tồn tại");

			SysParameter info = new SysParameter();
			info.setMa(req.getParamId());
			info.setTen(req.getParamName());
			info.setGiaTri(req.getParamValue());
			info.setTrangThai(req.getStatus());
			info.setMoTa(req.getDescription());
			SysParameter dataInfo = this.sysParameterRepository.save(info);

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

	@ApiOperation(value = "Lấy thông tin tham số", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/find")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> select(@Valid @RequestBody IdSearchReq req) {
		Resp resp = new Resp();
		try {
			if (req.getId() == null)
				throw new UnsupportedOperationException("Không tìm thấy tham số");
			Optional<SysParameter> param = sysParameterRepository.findById(req.getId());
			if (param == null)
				throw new UnsupportedOperationException("Không tìm thấy tham số");
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

	@ApiOperation(value = "Cập nhật thông tin tham số", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/update")
	public ResponseEntity<Resp> modify(@RequestBody NewSysParamReq req) {
		Resp resp = new Resp();
		try {
			Optional<SysParameter> param = this.sysParameterRepository.findById(req.getId());
			if (!param.isPresent())
				throw new UnsupportedOperationException("Không tìm thấy tham số");

			param.get().setMa(req.getParamId());
			param.get().setTen(req.getParamName());
			param.get().setGiaTri(req.getParamValue());
			param.get().setTrangThai(req.getStatus());
			param.get().setMoTa(req.getDescription());

			int data = 0;
			SysParameter dataInfo = this.sysParameterRepository.save(param.get());
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

	@ApiOperation(value = "Kích hoạt/ Tạm dừng tham số", response = List.class)
	@PostMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resp> updateStatus(@Valid @RequestBody IdSearchReq req) {
		Resp resp = new Resp();
		try {
			if (StringUtils.isEmpty(req.getId()))
				throw new Exception("Không tìm thấy tham số");

			Optional<SysParameter> sysParam = sysParameterRepository.findById(Long.valueOf(req.getId()));
			if (!sysParam.isPresent())
				throw new Exception("Không tìm thấy tham số cần sửa");
			sysParam.get()
					.setTrangThai(sysParam.get().getTrangThai().equals(Contains.HOAT_DONG) ? Contains.NGUNG_HOAT_DONG
							: Contains.HOAT_DONG);
			sysParameterRepository.save(sysParam.get());
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
	@ApiOperation(value = "Xóa Tham số", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/delete")
	@ResponseStatus(HttpStatus.OK)
	/*@PreAuthorize("hasRole('SYS_USER_DEL')")*/
	//TODO: ten chuc nang + hanh dong
	public ResponseEntity<Resp> delete(@RequestBody IdSearchReq req) throws Exception {
		Resp resp = new Resp();
		try {
			Optional<SysParameter> sysParam = sysParameterRepository.findById(Long.valueOf(req.getId()));
			if (!sysParam.isPresent())
				throw new Exception("Không tìm thấy tham số cần sửa");
			sysParameterRepository.delete(sysParam.get());
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}
