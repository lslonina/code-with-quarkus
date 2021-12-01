package org.lslonina.shop.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.lslonina.shop.dto.OrderItemDto;
import org.lslonina.shop.entity.OrderItem;
import org.lslonina.shop.repository.OrderItemRepository;
import org.lslonina.shop.repository.OrderRepository;
import org.lslonina.shop.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Transactional
public class OrderItemService {

    @Inject
    OrderItemRepository orderItemRepository;

    @Inject
    OrderRepository orderRepository;

    @Inject
    ProductRepository productRepository;

    public OrderItemDto findById(Long id) {
        log.debug("Request to get OrderItem: {}", id);

        return orderItemRepository.findById(id).map(OrderItemService::mapToDto).orElse(null);
    }

    public OrderItemDto create(OrderItemDto orderItemDto) {
        log.debug("Request to create OrderItem: {}", orderItemDto);

        var order = orderRepository.findById(orderItemDto.getOrderId())
                .orElseThrow(() -> new IllegalStateException("The Order does not exists!"));
        var product = productRepository.findById(orderItemDto.getProductId())
                .orElseThrow(() -> new IllegalStateException("The Product does not exists!"));

        var orderItem = orderItemRepository.save(new OrderItem(orderItemDto.getQuantity(), product, order));
        order.setPrice(order.getPrice().add(orderItem.getProduct().getPrice()));
        orderRepository.save(order);

        return mapToDto(orderItem);
    }

    public void delete(Long id) {
        log.debug("Request to delete OrderItem: {}", id);

        var orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("The OrderItem does not exists!"));
        var order = orderItem.getOrder();
        order.setPrice(order.getPrice().subtract(orderItem.getProduct().getPrice()));
        orderItemRepository.deleteById(id);
        order.getOrderItems().remove(orderItem);
        orderRepository.save(order);
    }

    public List<OrderItemDto> findByOrderId(Long id) {
        log.debug("Request to get all OrderItems of OrderId: {}", id);

        return orderItemRepository.findAllByOrderId(id).stream().map(OrderItemService::mapToDto)
                .collect(Collectors.toList());
    }

    public static OrderItemDto mapToDto(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getProduct().getId(),
                orderItem.getOrder().getId());
    }
}
