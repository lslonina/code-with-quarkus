package org.lslonina.shop.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.lslonina.shop.dto.PaymentDto;
import org.lslonina.shop.service.PaymentService;

@Path("/payments")
public class PaymentResource {
    
    @Inject
    PaymentService paymentService;

    @GET
    public List<PaymentDto> findAll() {
        return paymentService.findAll();
    }

    @GET
    @Path("/{id}")
    public PaymentDto findById(@PathParam("id") Long id) {
        return paymentService.findbyId(id);
    }

    @POST
    public PaymentDto create(PaymentDto paymentDto) {
        return paymentService.create(paymentDto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        paymentService.delete(id);
    }

    @GET
    @Path("/price/{max}")
    public List<PaymentDto> findPaymentsByAmountRangeMax(@PathParam("max") double max) {
        return paymentService.findByPriceRange(max);
    }
}
