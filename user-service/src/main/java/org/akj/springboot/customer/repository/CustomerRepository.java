package org.akj.springboot.customer.repository;

import org.akj.springboot.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    /**
     * payment
     * @param uid  user id
     * @param amount amount to be payed
     * @return
     */
    @Query("update Customer set balance = balance - ?2 where id= ?1 and balance >= ?2")
    @Modifying(clearAutomatically = true)
    public int pay(String uid, BigDecimal amount);

    /**
     * refund
     * @param uid  user id
     * @param amount  amount to be returned
     * @return
     */
    @Query("update Customer set balance = balance + ?2 where id=?1")
    @Modifying(clearAutomatically = true)
    public int refund(String uid, BigDecimal amount);
}
