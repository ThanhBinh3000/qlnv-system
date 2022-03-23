package com.tcdt.qlnvsystem.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.tcdt.qlnvsystem.util.Contains;
import com.tcdt.qlnvsystem.util.PaginationSet;
import com.tcdt.qlnvsystem.entities.UserInfoEntity;
import com.tcdt.qlnvsystem.jwt.CustomUserDetails;
import com.tcdt.qlnvsystem.repository.*;
import com.tcdt.qlnvsystem.request.BaseRequest;
import com.tcdt.qlnvsystem.request.UserInfoReq;
import com.tcdt.qlnvsystem.request.UserRolesReq;
import com.tcdt.qlnvsystem.request.UserSearchReq;
import com.tcdt.qlnvsystem.service.UserService;
import com.tcdt.qlnvsystem.table.UserAction;
import com.tcdt.qlnvsystem.table.UserHistory;
import com.tcdt.qlnvsystem.table.UserInfo;
import com.tcdt.qlnvsystem.table.UserRoles;
import com.tcdt.qlnvsystem.util.ExportExcel;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService {
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

	@Override
	public Iterable<UserAction> findAll() {
		return userActionRepository.findAll();
	}

	@Override
	public void saveUserHistory(UserHistory userHistory) {
		userHistoryRepository.save(userHistory);
	}

	@Override
	public UserInfo selectUserInfo(BaseRequest str) {
		return userRepository.findByUserIgnoreCase(str.getStr());
	}
	@Override
	public void resetPassword(String password, String token) throws Exception {
		UserInfo userInfo = userRepository.findByToken(token);
		if (userInfo == null)
			throw new Exception("Lỗi token");
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		userRepository.updatePassword(bCryptPasswordEncoder.encode(password), token);
	}
	@Override
	public void changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
								 HttpServletRequest request) throws Exception {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String username = getUserName(request);

		UserInfo kbUserInfo = userRepository.findByUsername(username);
		if (!bCryptPasswordEncoder.matches(decodeValue(oldPassword), kbUserInfo.getPassword())) {
			throw new Exception("Password cũ không đúng");
		}

		userRepository.updatePasswordWhereUsername(bCryptPasswordEncoder.encode(decodeValue(newPassword)),
				username);
	}
	@Override
	public UserInfo create(UserInfoReq req, HttpServletRequest request) {
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
 		return dataInfo;
	}
	@Override
	public UserInfo modify(UserInfoReq req) {
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
		return this.userRepository.save(info);
	}
	@Override
	public void delete(BaseRequest str) throws Exception {
		if (org.apache.commons.lang3.StringUtils.isBlank(str.getStr()))
			throw new UnsupportedOperationException("delete-user-unknown");

		UserInfo info = userRepository.findByUsername(str.getStr());
		if (info == null)
			throw new UnsupportedOperationException("delete-user-unknown");

		userRepository.delete(info);
	}

	@Override
	public Page<UserInfoEntity> search(UserSearchReq req) {
		int page = PaginationSet.getPage(req.getPaggingReq().getPage());
		int limit = PaginationSet.getLimit(req.getPaggingReq().getLimit());
		Pageable pageable = PageRequest.of(page, limit, Sort.by("id").ascending());
		return userRepository.selectParams(req.getDvql(), req.getUsername(), req.getFullName(),
				req.getSysType(), req.getDvql(), req.getStatus(), pageable);
	}
	@Override
	public UserInfo toggle(BaseRequest str) throws JsonMappingException {
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

		return userRepository.save(dataDTB);
	}
	@Override
	public void exportToExcel(UserSearchReq req, HttpServletResponse response) throws Exception {
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
	}
	@Override
	public UserRoles addRole(UserRolesReq req) {
		UserRoles dataMap = new ModelMapper().map(req, UserRoles.class);
		dataMap.setStatus(Contains.HOAT_DONG);
		return this.userRolesRepository.save(dataMap);
	}

	@Override
	public UserRoles activeRole(UserRolesReq req) {
		UserRoles dataMap = userRolesRepository.findByUserIdAndRoleId(req.getUserId(),req.getRoleId());
		if (dataMap.getStatus().equals(Contains.HOAT_DONG))
			dataMap.setStatus(Contains.TAM_KHOA);
		else
			dataMap.setStatus(Contains.HOAT_DONG);
		return this.userRolesRepository.save(dataMap);
	}
	@Override
	public void removeRole(UserRolesReq req) {
		Long roleId = req.getRoleId();
		Long userId = req.getUserId();
		UserRoles  userRoles= userRolesRepository.findByUserIdAndRoleId(userId,roleId);
		userRolesRepository.deleteByUserIdAndRoleId(userId,roleId);
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
