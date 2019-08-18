package org.akj.springboot.tx.repository;

import org.akj.springboot.tx.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
