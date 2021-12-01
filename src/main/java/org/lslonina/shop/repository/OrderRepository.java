package org.lslonina.shop.repository;

import java.util.List;
import java.util.Optional;

import org.lslonina.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCartCustomerId(Long customerId);

    Optional<Order> findByPaymentId(Long id);
}
