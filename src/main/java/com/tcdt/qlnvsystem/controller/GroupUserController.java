package com.tcdt.qlnvsystem.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tcdt.qlnvsystem.enums.EnumResponse;
import com.tcdt.qlnvsystem.repository.UserGroupPermissionRepository;
import com.tcdt.qlnvsystem.repository.UserGroupRepository;
import com.tcdt.qlnvsystem.repository.UserInfoRepository;
import com.tcdt.qlnvsystem.request.IdSearchReq;
import com.tcdt.qlnvsystem.request.SimpleSearchReq;
import com.tcdt.qlnvsystem.request.UserGroupPermissionReq;
import com.tcdt.qlnvsystem.request.UserGroupReq;
import com.tcdt.qlnvsystem.response.Resp;
import com.tcdt.qlnvsystem.table.UserGroup;
import com.tcdt.qlnvsystem.table.UserGroupPermission;
import com.tcdt.qlnvsystem.util.Contains;
import com.tcdt.qlnvsystem.util.PaginationSet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
@Api(tags = "Nhóm người sử dụng")
public class GroupUserController extends BaseController {
	@Autowired
	private UserGroupRepository userGroupRepository;
	@Autowired
	private UserGroupPermissionRepository userGroupPermissionRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;

	@ApiOperation(value = "Tạo mới nhóm người sử dụng", response = List.class)
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Resp> insert(@Valid HttpServletRequest request, @RequestBody UserGroupReq userGroupReq) {
		UserGroup group = new UserGroup();
		Resp resp = new Resp();
		Calendar cal = Calendar.getInstance();
		try {
			group.setGroupCode(userGroupReq.getGroupCode());
			group.setGroupName(userGroupReq.getGroupName());
			group.setDescription(userGroupReq.getDescription());
			group.setCreateTime(cal.getTime());
			group.setData(userGroupReq.getData());
			group.setStatus(Contains.HOAT_DONG);
			group.setDataFt(userGroupReq.getDataFt());
			group.setCreateBy(getUserName(request));

			UserGroup groupSubmit = userGroupRepository.save(group);
			if (!StringUtils.isEmpty(groupSubmit)) {
				ArrayList<UserGroupPermission> permissionList = new ArrayList<UserGroupPermission>();
				List<UserGroupPermissionReq> permissionReqList = userGroupReq.getGroupPermissionsReq();
				for (UserGroupPermissionReq permissionReq : permissionReqList) {
					UserGroupPermission permission = new UserGroupPermission();
					permission.setGroupId(groupSubmit.getId());
					permission.setModuleId(permissionReq.getModuleId());
					permissionList.add(permission);
				}
				userGroupPermissionRepository.saveAll(permissionList);
			}
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

	@ApiOperation(value = "Lấy thông tin nhóm người sử dụng", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> select(@RequestBody IdSearchReq idSearchReq) {
		Resp resp = new Resp();
		try {
			Optional<UserGroup> user = userGroupRepository.findById(idSearchReq.getId());
			if (!user.isPresent())
				throw new Exception("Không tồn tại bản ghi");

			List<UserGroupPermission> userGroupPermissions = userGroupPermissionRepository
					.findByGroupId(user.get().getId());

			user.get().setGroupPermissionsReq(userGroupPermissions);

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
			resp.setData(user);

		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Xoá thông tin nhóm người sử dụng", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> delete(@RequestBody IdSearchReq idSearchReq) {
		Resp resp = new Resp();
		try {
			if (StringUtils.isEmpty(idSearchReq.getId()))
				throw new Exception("Xoá thất bại, không tìm thấy mã nhóm");
			Long groupId = idSearchReq.getId();
			long countUser = userInfoRepository.countNhom(groupId + "");
			if (countUser > 0)
				throw new Exception("Xoá thất bại, nhóm quyền đang được sử dụng");
			Optional<UserGroup> userGroup = userGroupRepository.findById(groupId);
			if (!userGroup.isPresent())
				throw new Exception("Không tìm thấy nhóm cần xoá");
			userGroupPermissionRepository.deleteByGroupId(groupId);
			userGroupRepository.deleteById(groupId);
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

	@ApiOperation(value = "Tra cứu nhóm người sử dụng", response = List.class)
	@PostMapping(value = "/findList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> selectAll(@RequestBody SimpleSearchReq simpleSearchReq) {
		Resp resp = new Resp();
		try {
			int page = PaginationSet.getPage(simpleSearchReq.getPage());
			int limit = PaginationSet.getLimit(simpleSearchReq.getLimit());
			Pageable pageable = PageRequest.of(page, limit);

			Page<UserGroup> user = userGroupRepository.selectParams(simpleSearchReq.getCode(),
					simpleSearchReq.getName(), pageable);

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
			resp.setData(user);
		} catch (Exception e) {
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}

		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Cập nhật nhóm người sử dụng", response = List.class)
	@PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resp> update(@RequestBody UserGroupReq userGroupReq) {
		Resp resp = new Resp();
		try {
			if (StringUtils.isEmpty(userGroupReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy mã nhóm");

			Optional<UserGroup> userGroup = userGroupRepository.findById(Long.valueOf(userGroupReq.getId()));
			if (!userGroup.isPresent())
				throw new Exception("Không tìm thấy nhóm cần sửa");
			userGroup.get().setGroupCode(userGroupReq.getGroupCode());
			userGroup.get().setGroupName(userGroupReq.getGroupName());
			userGroup.get().setDescription(userGroupReq.getDescription());

			userGroupRepository.save(userGroup.get());

			// if (userGroupReq.getGroupPermissionsReq() != null &&
			// !userGroupReq.getGroupPermissionsReq().isEmpty()) {
			userGroupPermissionRepository.deleteByGroupId(userGroupReq.getId());
			ArrayList<UserGroupPermission> permissionList = new ArrayList<UserGroupPermission>();
			List<UserGroupPermissionReq> permissionReqList = userGroupReq.getGroupPermissionsReq();
			for (UserGroupPermissionReq permissionReq : permissionReqList) {
				UserGroupPermission permission = new UserGroupPermission();
				permission.setGroupId(userGroupReq.getId());
				permission.setModuleId(permissionReq.getModuleId());
				permissionList.add(permission);
			}
			userGroupPermissionRepository.saveAll(permissionList);
			// }
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
			resp.setMsg("Thành công");
		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(EnumResponse.RESP_FAIL.getValue());
			resp.setMsg(e.getMessage());
			log.error(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	@ApiOperation(value = "Kích hoạt/ Tạm dừng nhóm người sử dụng", response = List.class)
	@PostMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resp> updateStatus(@RequestBody IdSearchReq id) {
		Resp resp = new Resp();
		try {
			if (StringUtils.isEmpty(id.getId()))
				throw new Exception("Không tìm thấy mã nhóm");

			Optional<UserGroup> userGroup = userGroupRepository.findById(Long.valueOf(id.getId()));
			if (!userGroup.isPresent())
				throw new Exception("Không tìm thấy nhóm cần sửa");
			userGroup.get().setStatus(userGroup.get().getStatus().equals(Contains.HOAT_DONG) ? Contains.NGUNG_HOAT_DONG
					: Contains.HOAT_DONG);
			userGroupRepository.save(userGroup.get());
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

	@ApiOperation(value = "Lấy danh sách group trạng thái hoạt động", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/findAll")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> collect() {
		Resp resp = new Resp();
		try {
			Iterable<UserGroup> userGroups = userGroupRepository.findByStatus(Contains.HOAT_DONG);
			resp.setData(userGroups);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}