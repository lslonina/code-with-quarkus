package org.lslonina.shop.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.lslonina.shop.dto.OrderDto;
import org.lslonina.shop.entity.Order;
import org.lslonina.shop.entity.OrderStatus;
import org.lslonina.shop.repository.CartRepository;
import org.lslonina.shop.repository.OrderRepository;
import org.lslonina.shop.repository.PaymentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Transactional
public class OrderService {
    @Inject
    OrderRepository orderRepository;

    @Inject
    PaymentRepository paymentRepository;

    @Inject
    CartRepository cartRepository;

    public List<OrderDto> findAll() {
        log.debug("Request to get all Orders");

        return orderRepository.findAll().stream().map(OrderService::mapToDto).collect(Collectors.toList());
    }

    public OrderDto findById(Long id) {
        log.debug("Request to get Order: {}", id);

        return orderRepository.findById(id).map(OrderService::mapToDto).orElse(null);
    }

    public List<OrderDto> findAllByUser(Long id) {
        return orderRepository.findByCartCustomerId(id).stream().map(OrderService::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto create(OrderDto orderDto) {
        log.debug("Request to create Order: {}", orderDto);

        var cartId = orderDto.getCart().getId();

        var cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalStateException("The cart with ID[" + cartId + "] was not found!"));

        return mapToDto(orderRepository.save(new Order(BigDecimal.ZERO, OrderStatus.CREATION, null, null,
                AddressService.createFromDto(orderDto.getShipmentAddress()), Collections.emptySet(), cart)));
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Order: {}", id);

        var order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Order with ID[" + id + "] cannot be found!"));
        Optional.ofNullable(order.getPayment()).ifPresent(paymentRepository::delete);
        orderRepository.delete(order);
    }

    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }

    private static OrderDto mapToDto(Order order) {
        var orderItems = order.getOrderItems().stream().map(OrderItemService::mapToDto).collect(Collectors.toSet());
        return new OrderDto(
                order.getId(),
                order.getPrice(),
                order.getStatus().name(),
                order.getShipped(),
                order.getPayment() != null ? order.getPayment().getId() : null,
                AddressService.maptoDto(order.getShipmentAddress()),
                orderItems,
                CartService.mapToDto(order.getCart()));
    }
}
