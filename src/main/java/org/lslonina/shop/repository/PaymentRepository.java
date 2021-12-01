package org.lslonina.shop.repository;

import java.math.BigDecimal;
import java.util.List;

import org.lslonina.shop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByAmountBetween(BigDecimal min, BigDecimal max);
}
