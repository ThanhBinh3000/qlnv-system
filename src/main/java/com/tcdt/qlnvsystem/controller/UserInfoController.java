package com.tcdt.qlnvsystem.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.tcdt.qlnvsystem.repository.UserInfoEntityRepository;
import com.tcdt.qlnvsystem.repository.UserInfoRepository;
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
	private UserInfoRepository userInfoRepository;
	@Autowired
	private UserInfoEntityRepository userInfoEntityRepository;

	@ApiOperation(value = "Lấy thông tin UserInfo", response = List.class)
	@PostMapping(value = "/userInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> selectUserInfo(@RequestBody BaseRequest str) {
		Resp resp = new Resp();
		try {
			if (StringUtils.isEmpty(str.getStr()))
				throw new Exception("Tên tài khoản không được để trống");
			UserInfo user = userInfoRepository.findByUserIgnoreCase(str.getStr());
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
			UserInfo userInfo = userInfoRepository.findByToken(token);
			if (userInfo == null)
				throw new Exception("Lỗi token");

			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			userInfoRepository.updatePassword(bCryptPasswordEncoder.encode(password), token);
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
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String username = getUserName(request);

			UserInfo kbUserInfo = userInfoRepository.findByUsername(username);
			if (!bCryptPasswordEncoder.matches(decodeValue(oldPassword), kbUserInfo.getPassword())) {
				throw new Exception("Password cũ không đúng");
			}

			userInfoRepository.updatePasswordWhereUsername(bCryptPasswordEncoder.encode(decodeValue(newPassword)),
					username);
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
			if (this.userInfoRepository.findByUsername(req.getUsername()) != null)
				throw new UnsupportedOperationException("Tên tài khoản đã tồn tại");

			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

			UserInfo info = new UserInfo();
			info.setFullName(req.getFullName());
			info.setPhoneNo(req.getPhoneNo());
			info.setEmail(req.getEmail());
			info.setUsername(req.getUsername());
			info.setPassword(bCryptPasswordEncoder.encode(decodeValue(req.getPassword())));
			info.setSysType(req.getSysType());
			info.setStatus(Contains.HOAT_DONG);
			info.setDvql(req.getDvql());
			info.setGroupId(req.getGroupId());
			info.setGroupsArr(req.getGroupsArr());
			info.setCreateBy(getUserName(request));
			info.setCreateTime(new Timestamp(System.currentTimeMillis()));

			UserInfo dataInfo = this.userInfoRepository.save(info);

			int data = 0;
			if (dataInfo.getId() > 0)
				data = 1;
			resp.setData(data);
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
			UserInfo info;
			info = this.userInfoRepository.findByUsername(req.getUsername());
			if (info == null)
				throw new UnsupportedOperationException("update-user-unknown");

			info.setFullName(req.getFullName());
			info.setPhoneNo(req.getPhoneNo());
			info.setEmail(req.getEmail());
			info.setDvql(req.getDvql());
			info.setGroupId(req.getGroupId());
			info.setGroupsArr(req.getGroupsArr());
			int data = 0;
			UserInfo dataInfo = this.userInfoRepository.save(info);
			if (dataInfo.getId() > 0)
				data = 1;
			resp.setData(data);
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
	public ResponseEntity<Resp> delete(@RequestBody BaseRequest str) throws Exception {
		Resp resp = new Resp();
		try {
			if (StringUtils.isBlank(str.getStr()))
				throw new UnsupportedOperationException("delete-user-unknown");

			UserInfo info = userInfoRepository.findByUsername(str.getStr());
			if (info == null)
				throw new UnsupportedOperationException("delete-user-unknown");

			userInfoRepository.delete(info);
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
			int page = PaginationSet.getPage(req.getPage());
			int limit = PaginationSet.getLimit(req.getLimit());
			Pageable pageable = PageRequest.of(page, limit);
			Page<UserInfoEntity> data = userInfoEntityRepository.selectParams(req.getUsername(), req.getFullName(),
					req.getSysType(), req.getDvql(), req.getGroupsArr(), req.getStatus(), pageable);
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

	@ApiOperation(value = "Kích hoạt/Tạm dừng tài khoản nsd", response = List.class)
	@PostMapping(value = "/updateStatus", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public String toggle(@RequestBody BaseRequest str) {
		Resp resp = new Resp();
		try {
			if (StringUtils.isEmpty(str.getStr()))
				throw new UnsupportedOperationException("Không tìm thấy tên người dùng");
			UserInfo dataDTB = userInfoRepository.findByUsername(str.getStr());
			if (dataDTB == null)
				throw new UnsupportedOperationException("Không tìm thấy tên người dùng");
			UserInfo dataMap = new UserInfo();
			if (dataDTB.getStatus().equals(Contains.HOAT_DONG))
				dataMap.setStatus(Contains.TAM_KHOA);
			else
				dataMap.setStatus(Contains.HOAT_DONG);
			updateObjectToObject(dataDTB, dataMap);

			UserInfo updateCheck = userInfoRepository.save(dataDTB);

			resp.setData(updateCheck);
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

			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String currentDateTime = dateFormatter.format(new Date());

			String filename = "users_" + currentDateTime + ".xlsx";

			String title = "Danh sách người sử dụng";
			String[] rowsName = new String[] { "STT", "Tên NSD", "Tên đăng nhập", "E-mail", "SĐT", "Đơn vị", "Ngày tạo",
					"Người tạo", "Trạng thái" };
			List<UserInfoEntity> users = userInfoEntityRepository.selectParams(req.getUsername(), req.getFullName(),
					req.getSysType(), req.getDvql(), req.getGroupsArr(), req.getStatus());

			if (users.isEmpty())
				throw new UnsupportedOperationException("Không tìm thấy tên người dùng");

			List<Object[]> dataList = new ArrayList<Object[]>();
			Object[] objs = null;
			for (int i = 0; i < users.size(); i++) {
				UserInfoEntity user = users.get(i);
				objs = new Object[rowsName.length];
				objs[0] = i;
				objs[1] = user.getFullName();
				objs[2] = user.getUsername();
				objs[3] = user.getEmail();
				objs[4] = user.getPhoneNo();
				objs[5] = user.getDvqlName();
				objs[6] = user.getCreateTime();
				objs[7] = user.getCreateBy();
				objs[8] = user.getStatus().equals(Contains.HOAT_DONG) ? "Hoạt động" : "Tạm khóa";
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

	public static String Sha1EncryptText(String sInputText) {
		try {
			return DigestUtils.sha1Hex(sInputText);
		} catch (Exception e) {
			return null;
		}
	}

	// Decodes a URL encoded string using `UTF-8`
	public static String decodeValue(String value) {
		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}
	}
}