package org.akj.springboot.dtx.repository;

import org.akj.springboot.dtx.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "select * from customer_order where user_id= :uid",nativeQuery = true)
    List<Order> findByUserId(@Param("uid") Integer uid);
}
