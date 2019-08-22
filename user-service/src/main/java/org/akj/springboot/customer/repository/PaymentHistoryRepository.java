package org.akj.springboot.customer.repository;

import org.akj.springboot.customer.entity.Customer;
import org.akj.springboot.customer.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, String> {

    @Query("from payment_history payment where payment.orderId=?1")
    public PaymentHistory findPaymentHistoryByOrderId(String orderId);
}
