package com.tcdt.qlnvsystem.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.tcdt.qlnvsystem.table.Role;





@Repository
public interface RolesRepository extends CrudRepository<Role, Long> {
	
}
