package org.akj.springboot.tx.entity.audit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAuditRepository extends JpaRepository<CustomerAudit, Integer> {

}
