package org.lslonina.shop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.lslonina.shop.dto.PaymentDto;
import org.lslonina.shop.entity.Order;
import org.lslonina.shop.entity.OrderStatus;
import org.lslonina.shop.entity.Payment;
import org.lslonina.shop.entity.PaymentStatus;
import org.lslonina.shop.repository.OrderRepository;
import org.lslonina.shop.repository.PaymentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Transactional
public class PaymentService {
    @Inject
    PaymentRepository paymentRepository;

    @Inject
    OrderRepository orderRepository;

    public List<PaymentDto> findByPriceRange(Double max) {
        return paymentRepository.findAllByAmountBetween(BigDecimal.ZERO, BigDecimal.valueOf(max)).stream()
                .map(payment -> mapToDto(payment, findOrderByPaymentId(payment.getId()).getId()))
                .collect(Collectors.toList());
    }

    public List<PaymentDto> findAll() {
        return paymentRepository.findAll().stream().map(payment -> findbyId(payment.getId()))
                .collect(Collectors.toList());
    }

    public PaymentDto findbyId(Long id) {
        log.debug("Request to get Payment: {}", id);

        var order = findOrderByPaymentId(id);
        return paymentRepository.findById(id).map(payment -> mapToDto(payment, order.getId())).orElse(null);
    }

    public PaymentDto create(PaymentDto paymentDto) {
        log.debug("Request to create Payment: {}" + paymentDto);

        var order = orderRepository.findById(paymentDto.getOrderId())
                .orElseThrow(() -> new IllegalStateException("The Order does not exists!"));
        order.setStatus(OrderStatus.PAID);

        var payment = paymentRepository.saveAndFlush(new Payment(
                paymentDto.getPaypalPaymentId(),
                PaymentStatus.valueOf(paymentDto.getStatus()),
                order.getPrice()));

        orderRepository.saveAndFlush(order);

        return mapToDto(payment, order.getId());
    }

    private Order findOrderByPaymentId(Long id) {
        return orderRepository.findByPaymentId(id)
                .orElseThrow(() -> new IllegalStateException("No Order exists for the Payment ID " + id));
    }

    public void delete(Long id) {
        log.debug("Request to delete Payment: {}", id);

        paymentRepository.deleteById(id);
    }

    public static PaymentDto mapToDto(Payment payment, Long orderId) {
        if (payment == null) {
            return null;
        }
        return new PaymentDto(
                payment.getId(),
                payment.getPaypalPaymentId(),
                payment.getStatus().name(),
                orderId);
    }

}
