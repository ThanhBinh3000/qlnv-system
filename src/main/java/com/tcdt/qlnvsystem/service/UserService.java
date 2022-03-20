package com.tcdt.qlnvsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tcdt.qlnvsystem.controller.BaseController;
import com.tcdt.qlnvsystem.entities.UserInfoEntity;
import com.tcdt.qlnvsystem.enums.EnumResponse;
import com.tcdt.qlnvsystem.repository.*;
import com.tcdt.qlnvsystem.request.*;
import com.tcdt.qlnvsystem.response.Resp;
import com.tcdt.qlnvsystem.table.*;
import com.tcdt.qlnvsystem.util.Contains;
import com.tcdt.qlnvsystem.util.ExportExcel;
import com.tcdt.qlnvsystem.util.PaginationSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tcdt.qlnvsystem.jwt.CustomUserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class UserService extends BaseController implements UserDetailsService {
	@Autowired
	private UserInfoRepository userRepository;

	@Autowired
	private UserInfoEntityRepository userInfoEntityRepository;
	@Autowired
	UserHistoryRepository userHistoryRepository;

	@Autowired
	UserActionRepository userActionRepository;

	@Autowired
	UserRolesRepository userRolesRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new CustomUserDetails(user);
	}

	public Iterable<UserAction> findAll() {
		return userActionRepository.findAll();
	}

	public void saveUserHistory(UserHistory userHistory) {
		userHistoryRepository.save(userHistory);
	}
	public ResponseEntity<Resp> selectUserInfo(BaseRequest str) {
		Resp resp = new Resp();
		try {
			if (StringUtils.isEmpty(str.getStr()))
				throw new Exception("Tên tài khoản không được để trống");
			UserInfo user = userRepository.findByUserIgnoreCase(str.getStr());
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
	public String resetPassword(String password, String token) {
		Resp resp = new Resp();
		try {
			UserInfo userInfo = userRepository.findByToken(token);
			if (userInfo == null)
				throw new Exception("Lỗi token");

			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			userRepository.updatePassword(bCryptPasswordEncoder.encode(password), token);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return new Gson().toJson(resp);
	}
	public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
								 HttpServletRequest request) {
		Resp resp = new Resp();
		try {
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String username = getUserName(request);

			UserInfo kbUserInfo = userRepository.findByUsername(username);
			if (!bCryptPasswordEncoder.matches(decodeValue(oldPassword), kbUserInfo.getPassword())) {
				throw new Exception("Password cũ không đúng");
			}

			userRepository.updatePasswordWhereUsername(bCryptPasswordEncoder.encode(decodeValue(newPassword)),
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
	public ResponseEntity<Resp> create( UserInfoReq req, HttpServletRequest request) {
		Resp resp = new Resp();
		try {
			if (this.userRepository.findByUsername(req.getUsername()) != null)
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

			UserInfo dataInfo = this.userRepository.save(info);

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
	public ResponseEntity<Resp> modify(UserInfoReq req) {
		Resp resp = new Resp();
		try {
			UserInfo info;
			info = this.userRepository.findByUsername(req.getUsername());
			if (info == null)
				throw new UnsupportedOperationException("update-user-unknown");

			info.setFullName(req.getFullName());
			info.setPhoneNo(req.getPhoneNo());
			info.setEmail(req.getEmail());
			info.setDvql(req.getDvql());
			info.setGroupId(req.getGroupId());
			info.setGroupsArr(req.getGroupsArr());
			int data = 0;
			UserInfo dataInfo = this.userRepository.save(info);
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
	public ResponseEntity<Resp> delete(BaseRequest str) throws Exception {
		Resp resp = new Resp();
		try {
			if (StringUtils.isBlank(str.getStr()))
				throw new UnsupportedOperationException("delete-user-unknown");

			UserInfo info = userRepository.findByUsername(str.getStr());
			if (info == null)
				throw new UnsupportedOperationException("delete-user-unknown");

			userRepository.delete(info);
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

	public ResponseEntity<Resp> search(UserSearchReq req) {
		Resp resp = new Resp();
		try {
			int page = PaginationSet.getPage(req.getPaggingReq().getPage());
			int limit = PaginationSet.getLimit(req.getPaggingReq().getLimit());
			Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
			Page<UserInfoEntity> data = userRepository.selectParams(req.getDvql(), req.getUsername(), req.getFullName(),
					req.getSysType(), req.getDvql(), req.getStatus(), pageable);
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
	public String toggle(BaseRequest str) {
		Resp resp = new Resp();
		try {
			if (StringUtils.isEmpty(str.getStr()))
				throw new UnsupportedOperationException("Không tìm thấy tên người dùng");
			UserInfo dataDTB = userRepository.findByUsername(str.getStr());
			if (dataDTB == null)
				throw new UnsupportedOperationException("Không tìm thấy tên người dùng");
			UserInfo dataMap = new UserInfo();
			if (dataDTB.getStatus().equals(Contains.HOAT_DONG))
				dataMap.setStatus(Contains.TAM_KHOA);
			else
				dataMap.setStatus(Contains.HOAT_DONG);
			updateObjectToObject(dataDTB, dataMap);

			UserInfo updateCheck = userRepository.save(dataDTB);

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
	public void exportToExcel(UserSearchReq req, HttpServletResponse response) throws Exception {
		try {

			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String currentDateTime = dateFormatter.format(new Date());

			String filename = "users_" + currentDateTime + ".xlsx";

			String title = "Danh sách người sử dụng";
			String[] rowsName = new String[] { "STT", "Tên NSD", "Tên đăng nhập", "E-mail", "SĐT", "Đơn vị", "Ngày tạo",
					"Người tạo", "Trạng thái" };
			List<UserInfoEntity> users = userInfoEntityRepository.selectParams(req.getUsername(), req.getFullName(),
					req.getSysType(), req.getDvql(), req.getStatus());

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
	public ResponseEntity<Resp> addRole(UserRolesReq req) {
		Resp resp = new Resp();
		try {
			UserRoles dataMap = new ModelMapper().map(req, UserRoles.class);
			dataMap.setStatus(Contains.HOAT_DONG);
			UserRoles dataInfo = this.userRolesRepository.save(dataMap);
			resp.setData(dataInfo);
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
	public ResponseEntity<Resp> activeRole(UserRolesReq req) {
		Resp resp = new Resp();
		try {
			UserRoles dataMap = userRolesRepository.findByUserIdAndRoleId(req.getUserId(),req.getRoleId());
			if (dataMap.getStatus().equals(Contains.HOAT_DONG))
				dataMap.setStatus(Contains.TAM_KHOA);
			else
				dataMap.setStatus(Contains.HOAT_DONG);
			UserRoles dataInfo = this.userRolesRepository.save(dataMap);
			resp.setData(dataInfo);
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
	public ResponseEntity<Resp> removeRole(UserRolesReq req) {
		Resp resp = new Resp();
		try {
			Long roleId = req.getRoleId();
			Long userId = req.getUserId();
			UserRoles  userRoles= userRolesRepository.findByUserIdAndRoleId(userId,roleId);
			userRolesRepository.deleteByUserIdAndRoleId(userId,roleId);
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