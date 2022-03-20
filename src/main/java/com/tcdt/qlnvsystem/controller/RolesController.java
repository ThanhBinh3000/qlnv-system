package com.tcdt.qlnvsystem.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.tcdt.qlnvsystem.repository.RolesPermissonRepository;
import com.tcdt.qlnvsystem.repository.RolesRepository;
import com.tcdt.qlnvsystem.table.Roles;
import com.tcdt.qlnvsystem.table.RolesPermission;
import com.tcdt.qlnvsystem.table.SysPermission;
import org.modelmapper.ModelMapper;
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
import com.tcdt.qlnvsystem.repository.UserInfoRepository;
import com.tcdt.qlnvsystem.request.IdSearchReq;
import com.tcdt.qlnvsystem.request.SimpleSearchReq;
import com.tcdt.qlnvsystem.request.RolesPermissionReq;
import com.tcdt.qlnvsystem.request.RolesReq;
import com.tcdt.qlnvsystem.response.Resp;
import com.tcdt.qlnvsystem.util.Contains;
import com.tcdt.qlnvsystem.util.PaginationSet;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/nhom-nguoi-dung")
@Api(tags = "Nhóm người sử dụng")
public class RolesController extends BaseController {
	@Autowired
	private RolesRepository rolesRepository;
	@Autowired
	private RolesPermissonRepository rolesPermissionRepository;

	@Autowired
	private UserInfoRepository userInfoRepository;

	@ApiOperation(value = "Tạo mới nhóm người sử dụng", response = List.class)
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Resp> insert(@Valid HttpServletRequest request, @RequestBody RolesReq req) {
		Resp resp = new Resp();
		try {
			Roles dataMap = new ModelMapper().map(req, Roles.class);

			Roles dataInfo = this.rolesRepository.save(dataMap);
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

	@ApiOperation(value = "Lấy thông tin nhóm người sử dụng", response = List.class, produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Resp> select(@RequestBody IdSearchReq idSearchReq) {
		Resp resp = new Resp();
		try {
			Optional<Roles> role = rolesRepository.findById(idSearchReq.getId());
			if (!role.isPresent())
				throw new Exception("Không tồn tại bản ghi");
			List<RolesPermission> RolesPermissions = rolesPermissionRepository
					.findByRoleId(role.get().getId());

			role.get().setRolesPermission(RolesPermissions);

			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
			resp.setData(role);

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
			Long roleId = idSearchReq.getId();
			long countUser = userInfoRepository.countByRoleId(roleId);
			if (countUser > 0)
				throw new Exception("Xoá thất bại, nhóm quyền đang được sử dụng");
			Optional<Roles> Roles = rolesRepository.findById(roleId);
			if (!Roles.isPresent())
				throw new Exception("Không tìm thấy nhóm cần xoá");
			rolesPermissionRepository.deleteByRoleId(roleId);
			rolesRepository.deleteById(roleId);
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

			Page<Roles> user = rolesRepository.selectParams(simpleSearchReq.getCode(),
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
	public ResponseEntity<Resp> update(@RequestBody RolesReq RolesReq) {
		Resp resp = new Resp();
		try {
			if (StringUtils.isEmpty(RolesReq.getId()))
				throw new Exception("Sửa thất bại, không tìm thấy mã nhóm");

			Optional<Roles> Roles = rolesRepository.findById(Long.valueOf(RolesReq.getId()));
			if (!Roles.isPresent())
				throw new Exception("Không tìm thấy nhóm cần sửa");
			Roles.get().setCode(RolesReq.getCode());
			Roles.get().setName(RolesReq.getName());

			rolesRepository.save(Roles.get());

			// if (RolesReq.getGroupPermissionsReq() != null &&
			// !RolesReq.getGroupPermissionsReq().isEmpty()) {
			rolesPermissionRepository.deleteByRoleId(RolesReq.getId());
			ArrayList<RolesPermission> permissionList = new ArrayList<>();
			List<RolesPermissionReq> permissionReqList = RolesReq.getRolePermissionsReq();
			for (RolesPermissionReq permissionReq : permissionReqList) {
				RolesPermission permission = new RolesPermission();
				permission.setRoleId(RolesReq.getId());
				permission.setPermissionId(permissionReq.getPermissionId());
				permissionList.add(permission);
			}
			rolesPermissionRepository.saveAll(permissionList);
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

			Optional<Roles> roles = rolesRepository.findById(Long.valueOf(id.getId()));
			if (!roles.isPresent())
				throw new Exception("Không tìm thấy nhóm cần sửa");
			roles.get().setStatus(roles.get().getStatus().equals(Contains.HOAT_DONG) ? Contains.NGUNG_HOAT_DONG
					: Contains.HOAT_DONG);
			rolesRepository.save(roles.get());
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
			Iterable<Roles> roles = rolesRepository.findByStatus(Contains.HOAT_DONG);
			resp.setData(roles);
			resp.setStatusCode(EnumResponse.RESP_SUCC.getValue());
			resp.setMsg(EnumResponse.RESP_SUCC.getDescription());
		} catch (Exception e) {
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}
}