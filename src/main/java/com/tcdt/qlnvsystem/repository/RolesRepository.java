package com.tcdt.qlnvsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tcdt.qlnvsystem.table.Roles;





@Repository
public interface RolesRepository extends CrudRepository<Roles, Long> {
    final String qr = "SELECT * FROM ROLES WHERE (:code is null or lower(CODE) like lower(concat(concat('%', :code),'%'))) AND (:name is null or lower(NAME) like lower(concat(concat('%', :name),'%')))";
    final String qrCount = "SELECT count(1) FROM ROLES WHERE (:code is null or lower(CODE) like lower(concat(concat('%', :code),'%'))) AND (:name is null or lower(NAME) like lower(concat(concat('%', :name),'%')))";

    @Query(value = qr,
            countQuery = qrCount, nativeQuery = true)
    Page<Roles> selectParams(@Param("code") String code, @Param("name") String name, Pageable pageable);

    Iterable<Roles> findByStatus(String status);
}
