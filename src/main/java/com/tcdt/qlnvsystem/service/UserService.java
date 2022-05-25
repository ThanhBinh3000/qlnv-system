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
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//extends BaseController implements UserDetailsService
@Service
public interface UserService  {

	UserDetails loadUserByUsername(String username) throws Exception;

	Iterable<UserAction> findAll() throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void saveUserHistory(UserHistory userHistory) throws Exception;

	UserInfo selectUserInfo(BaseRequest str) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void resetPassword(String password, String token) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
				   HttpServletRequest request) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	UserInfo create(UserInfoReq req, HttpServletRequest request) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	UserInfo modify(UserInfoReq req) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void delete(Long id) throws Exception;

	Page<UserInfoEntity> search(UserSearchReq req) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	UserInfo toggle(BaseRequest str) throws Exception;

	void exportToExcel(UserSearchReq req, HttpServletResponse response) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	UserRoles addRole(UserRolesReq req) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	UserRoles activeRole(UserRolesReq req) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void removeRole(UserRolesReq req) throws Exception;
}