package org.lslonina.shop.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.lslonina.shop.dto.ProductDto;
import org.lslonina.shop.entity.Product;
import org.lslonina.shop.entity.ProductStatus;
import org.lslonina.shop.repository.CategoryRepository;
import org.lslonina.shop.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Transactional
public class ProductService {

    @Inject
    ProductRepository productRepository;

    @Inject
    CategoryRepository categoryRepository;

    public List<ProductDto> findAll() {
        log.debug("Request to get all Products");

        return productRepository.findAll().stream().map(ProductService::mapToDto).collect(Collectors.toList());
    }

    public ProductDto findbyId(Long id) {
        log.debug("Request to get Product: {}", id);

        return productRepository.findById(id).map(ProductService::mapToDto).orElse(null);
    }

    public Long countAll() {
        return productRepository.count();
    }

    public Long countByCategoryId(Long id) {
        return productRepository.countAllByCategoryId(id);
    }

    public ProductDto create(ProductDto productDto) {
        log.debug("Request to create Product: {}", productDto);

        return mapToDto(productRepository.save(new Product(
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                ProductStatus.valueOf(productDto.getStatus()),
                productDto.getSalesCounter(),
                Collections.emptySet(),
                categoryRepository.findById(productDto.getCategoryId()).orElse(null))));
    }

    public void delete(Long id) {
        log.debug("Request to delete Product: {}", id);

        productRepository.deleteById(id);
    }

    public List<ProductDto> findByCategoryId(Long id) {
        return productRepository.findByCategoryId(id).stream().map(ProductService::mapToDto)
                .collect(Collectors.toList());
    }

    public static ProductDto mapToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDesription(),
                product.getPrice(),
                product.getStatus().name(),
                product.getSalesCounter(),
                product.getReviews().stream().map(ReviewService::mapToDto).collect(Collectors.toSet()),
                product.getCategory().getId());
    }
}
