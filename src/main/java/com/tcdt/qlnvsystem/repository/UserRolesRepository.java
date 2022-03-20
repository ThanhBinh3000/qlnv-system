package com.tcdt.qlnvsystem.repository;

import com.tcdt.qlnvsystem.table.UserRoles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRolesRepository extends CrudRepository<UserRoles, Long> {
    UserRoles findByUserIdAndRoleId(Long userId,Long roleId);

    @Transactional
    void deleteByUserIdAndRoleId(Long userId,Long roleId);
}
