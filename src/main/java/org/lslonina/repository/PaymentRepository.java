package org.lslonina.repository;

import java.math.BigDecimal;
import java.util.List;

import org.lslonina.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByAmountBetween(BigDecimal min, BigDecimal max);
}
