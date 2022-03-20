package com.tcdt.qlnvsystem.repository;

import com.tcdt.qlnvsystem.table.Roles;
import com.tcdt.qlnvsystem.table.RolesPermission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface RolesPermissonRepository extends CrudRepository<RolesPermission, Long> {
    @Transactional
    void deleteByRoleId(Long id);

    List<RolesPermission> findByRoleId(Long id);
}
