package org.lslonina.shop.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.lslonina.shop.dto.CustomerDto;
import org.lslonina.shop.service.CustomerService;

@Path("/customers")
public class CustomerResource {
    
    @Inject
    CustomerService customerService;

    @GET
    public List<CustomerDto> findAll() {
        return customerService.findAll();
    }

    @GET
    @Path("/{id}")
    public CustomerDto findById(@PathParam("id") Long id) {
        return customerService.findById(id);
    }

    @GET
    @Path("/active")
    public List<CustomerDto> findAllActive() {
        return customerService.findAllActive();
    }

    @GET
    @Path("/inactive")
    public List<CustomerDto> findAllInactive() {
        return customerService.findAllInactive();
    }

    @POST
    public CustomerDto create(CustomerDto customerDto) {
        return customerService.create(customerDto);
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        customerService.delete(id);
    }
}
