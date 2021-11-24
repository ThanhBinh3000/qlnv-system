package com.tcdt.qlnvsystem.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvsystem.repository.UserHistoryRepository;
import com.tcdt.qlnvsystem.request.HistoryRequest;
import com.tcdt.qlnvsystem.request.IdSearchReq;
import com.tcdt.qlnvsystem.response.Resp;
import com.tcdt.qlnvsystem.table.UserHistory;
import com.tcdt.qlnvsystem.util.Contains;
import com.tcdt.qlnvsystem.util.ExportExcel;
import com.tcdt.qlnvsystem.util.PaginationSet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
@Api(tags = "Lịch sử hệ thống")
@Slf4j
public class UserHistoryController {

	@Autowired
	private UserHistoryRepository userHistoryRepository;

	@ApiOperation(value = "Lấy lịch sử hệ thống", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/findList")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> searchHistory(@RequestBody HistoryRequest req) {
		Resp resp = new Resp();
		try {
			int page = PaginationSet.getPage(req.getPage());
			int limit = PaginationSet.getLimit(req.getLimit());
			Pageable pageable = PageRequest.of(page, limit);

			Page<UserHistory> data = userHistoryRepository.selectParams(req.getUsername(), req.getTuNgay(),
					req.getDenNgay(), pageable);
			resp.setData(data);
			resp.setStatusCode(Contains.RESP_SUCC);
			resp.setMsg("Thành công");
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Lấy thông tin chi tiết lịch sử hệ thống", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/find")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('MARKER')")
	public ResponseEntity<Resp> select(@Valid @RequestBody IdSearchReq req) {
		Resp resp = new Resp();
		try {
			if (req.getId() == null)
				throw new UnsupportedOperationException("Không tìm thấy lịch sử");
			Optional<UserHistory> param = userHistoryRepository.findById(req.getId());
			if (param == null)
				throw new UnsupportedOperationException("Không tìm thấy lịch sử");
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

	@ApiOperation(value = "Tra cứu danh sách kết xuất excel", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping("/exportExcel")
	@ResponseStatus(HttpStatus.OK)
	public void exportToExcel(@RequestBody HistoryRequest req, HttpServletResponse response) throws Exception {
		try {
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String currentDateTime = dateFormatter.format(new Date());

			String filename = "history_" + currentDateTime + ".xlsx";

			String title = "Danh sách lịch sử hệ thống";
			String[] rowsName = new String[] { "STT", "Thời gian", "Tên đăng nhập", "Địa chỉ IP", "Mô tả" };
			List<UserHistory> users = userHistoryRepository.selectParams(req.getUsername(), req.getTuNgay(),
					req.getDenNgay());
			List<Object[]> dataList = new ArrayList<Object[]>();
			Object[] objs = null;
			for (int i = 0; i < users.size(); i++) {
				UserHistory user = users.get(i);
				objs = new Object[rowsName.length];
				objs[0] = i;
				objs[1] = user.getTimeaction();
				objs[2] = user.getUsername();
				objs[3] = user.getIpaddress();
				objs[4] = user.getNote();
				dataList.add(objs);

			}
			ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
			ex.export();
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

}
