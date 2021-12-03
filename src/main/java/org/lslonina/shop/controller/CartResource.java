package org.lslonina.shop.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.lslonina.shop.dto.CartDto;
import org.lslonina.shop.service.CartService;

@Path("/carts")
public class CartResource {

    @Inject
    CartService cartService;

    @GET
    public List<CartDto> findAll() {
        return cartService.findAll();
    }

    @GET
    @Path("/active")
    public List<CartDto> findAllActiveCarts() {
        return cartService.findAllActiveCarts();
    }

    @GET
    @Path("/customer/{id}")
    public CartDto getactiveCartForCustomer(@PathParam("id") Long customerId) {
        return cartService.getActiveCart(customerId);
    }

    @GET
    @Path("/{id}")
    public CartDto findById(@PathParam("id") Long id) {
        return cartService.findById(id);
    }

    @POST
    @Path("/customer/{id}")
    public CartDto create(@PathParam("id") Long id) {
        return cartService.createDto(id);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        cartService.delete(id);
    }
}
