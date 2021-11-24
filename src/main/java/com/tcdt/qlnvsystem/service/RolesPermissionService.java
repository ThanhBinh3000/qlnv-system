package com.tcdt.qlnvsystem.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcdt.qlnvsystem.repository.PermissionRolesRepository;
import com.tcdt.qlnvsystem.request.RolePermissionRequest;
import com.tcdt.qlnvsystem.table.PermissionRoles;

@Service
public class RolesPermissionService {
	@Autowired
	private PermissionRolesRepository permissionRolesRepository;

	@Transactional
	public void save(RolePermissionRequest rolePermissionRequest) {
		permissionRolesRepository.deleteByRoleId(Long.parseLong(rolePermissionRequest.getRoleId()));
		ArrayList<PermissionRoles> permissionList = new ArrayList<PermissionRoles>();
		List<String> permissionReqList = rolePermissionRequest.getLstPermission();
		for (String permissionReq : permissionReqList) {
			PermissionRoles permission = new PermissionRoles();
			permission.setRoleId(Long.parseLong(rolePermissionRequest.getRoleId()));
			permission.setPermissionId(Long.parseLong(permissionReq));
			permissionList.add(permission);
		}
		permissionRolesRepository.saveAll(permissionList);
	}
}
