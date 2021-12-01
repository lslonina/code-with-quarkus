package org.lslonina.shop.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.lslonina.shop.dto.ProductDto;
import org.lslonina.shop.service.ProductService;

@Path("/products")
public class ProductResource {
    
    @Inject
    ProductService productService;

    @GET
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @GET
    @Path("/count")
    public Long countAllProducts() {
        return productService.countAll();
    }

    @GET
    @Path("/{id}")
    public ProductDto findById(@PathParam("id") Long id) {
        return productService.findbyId(id);
    }

    @POST
    public ProductDto create(ProductDto productDto) {
        return productService.create(productDto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        productService.delete(id);
    }

    @GET
    @Path("/category/{id}")
    public List<ProductDto> findByCategoryId(@PathParam("id") Long id) {
        return productService.findByCategoryId(id);
    }

    @GET
    @Path("/count/category/{id}")
    public Long countByCategoryId(@PathParam("id") Long id) {
        return productService.countByCategoryId(id);
    }
}
