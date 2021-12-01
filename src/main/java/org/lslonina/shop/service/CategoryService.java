package org.lslonina.shop.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.lslonina.shop.dto.CategoryDto;
import org.lslonina.shop.dto.ProductDto;
import org.lslonina.shop.entity.Category;
import org.lslonina.shop.repository.CategoryRepository;
import org.lslonina.shop.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Transactional
public class CategoryService {

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    ProductRepository productRepository;

    public static CategoryDto mapToDto(Category category, Long productCount) {
        return new CategoryDto(category.getId(), category.getName(), category.getDescription(), productCount);
    }

    public List<CategoryDto> findAll() {
        log.debug("Request to get all Categories");

        return categoryRepository.findAll().stream().map(
                category -> mapToDto(category, productRepository.countAllByCategoryId(category.getId())))
                .collect(Collectors.toList());
    }

    public CategoryDto findById(Long id) {
        log.debug("Request to get Category: {}", id);

        return categoryRepository.findById(id)
                .map(category -> mapToDto(category, productRepository.countAllByCategoryId(category.getId())))
                .orElse(null);
    }

    public CategoryDto create(CategoryDto categoryDto) {
        log.debug("Request to create Category: {}", categoryDto);

        return mapToDto(categoryRepository.save(new Category(categoryDto.getName(), categoryDto.getDescription())), 0L);
    }

    public void delete(Long id) {
        log.debug("Request to delete Category: {}", id);
        
        log.debug("Deleting all products for the Category: {}", id);
        productRepository.deleteAllByCategoryId(id);
        
        log.debug("Deleting Category: {}", id);
        categoryRepository.deleteById(id);
    }

    public List<ProductDto> findProductsByCategoryId(Long id) {
        return productRepository.findAllByCategoryId(id).stream().map(ProductService::mapToDto)
                .collect(Collectors.toList());
    }
}
