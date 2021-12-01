package org.lslonina.shop.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.lslonina.shop.dto.OrderItemDto;
import org.lslonina.shop.service.OrderItemService;

@Path("/order-items")
public class OrderItemResource {
    
    @Inject
    OrderItemService itemService;

    @GET
    @Path("/order/{id}")
    public List<OrderItemDto> findByOrderId(@PathParam("id") Long id) {
        return itemService.findByOrderId(id);
    }

    @GET
    @Path("/{id}")
    public OrderItemDto findById(@PathParam("id") Long id) {
        return itemService.findById(id);
    }

    @POST
    public void create(OrderItemDto orderItemDto) {
        itemService.create(orderItemDto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        itemService.delete(id);
    }
}
