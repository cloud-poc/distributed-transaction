package org.akj.springboot.dtx.repository;

import org.akj.springboot.dtx.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
