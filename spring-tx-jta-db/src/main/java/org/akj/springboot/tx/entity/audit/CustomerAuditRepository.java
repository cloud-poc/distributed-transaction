package org.akj.springboot.tx.entity.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface CustomerAuditRepository extends JpaRepository<CustomerAudit, Integer> {

    @Query("from CustomerAudit audit where audit.createDate <= :date")
    Page<CustomerAudit> findAllByDate(@Param("date") Date date, Pageable pageable);
}
