package org.lslonina.shop.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.lslonina.shop.dto.CartDto;
import org.lslonina.shop.entity.Cart;
import org.lslonina.shop.entity.CartStatus;
import org.lslonina.shop.repository.CartRepository;
import org.lslonina.shop.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Transactional
public class CartService {

    @Inject
    CartRepository cartRepository;

    @Inject
    CustomerRepository customerRepository;

    public List<CartDto> findAll() {
        log.debug("Requests to get all Carts");

        return cartRepository.findAll().stream().map(CartService::mapToDto).collect(Collectors.toList());
    }

    public List<CartDto> findAllActiveCarts() {
        return cartRepository.findByStatus(CartStatus.NEW).stream().map(CartService::mapToDto)
                .collect(Collectors.toList());
    }

    public Cart create(Long customerId) {
        if (getActiveCart(customerId) != null) {
            throw new IllegalStateException("There is already an active cart!");
        }

        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalStateException("The customer does not exist!"));
        var cart = new Cart(customer, CartStatus.NEW);

        return cartRepository.save(cart);
    }

    public CartDto createDto(Long customerId) {
        return mapToDto(create(customerId));
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public CartDto findById(Long id) {
        log.debug("Request to get Cart: {}", id);

        return cartRepository.findById(id).map(CartService::mapToDto).orElse(null);
    }

    public void delete(Long id) {
        log.debug("Request to delete Cart: {}", id);

        var cart = cartRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find cart with id " + id));

        cart.setStatus(CartStatus.CANCELED);
        cartRepository.save(cart);
    }

    public CartDto getActiveCart(Long customerId) {
        var carts = this.cartRepository.findByStatusAndCustomerId(CartStatus.NEW, customerId);

        if (carts == null || carts.isEmpty()) {
            return null;
        }
        if (carts.size() > 1) {
            throw new IllegalStateException("Many active carts detected!");
        }
        return mapToDto(carts.get(0));
    }

    public static CartDto mapToDto(Cart cart) {
        return new CartDto(cart.getId(), CustomerService.mapToDto(cart.getCustomer()), cart.getStatus().name());
    }

}
