package org.akj.springboot.order.repository;

import org.akj.springboot.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query(value = "select * from ticket_order where user_id= :uid", nativeQuery = true)
    List<Order> findByUserId(@Param("uid") String uid);

    @Query(value = "select * from ticket_order where user_id= :uid and status='COMPLETED'", nativeQuery = true)
    List<Order> findCompletedOrderByUserId(@Param("uid") String uid);

    @Query(value = "select order from ticket_order order where order.status=?1")
    List<Order> findOrderByStatus(String payment_pending);
}
