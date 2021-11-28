package org.lslonina.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.lslonina.entity.Review;
import org.lslonina.repository.ProductRepository;
import org.lslonina.repository.ReviewRepository;
import org.lslonina.service.dto.ReviewDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Transactional
public class ReviewService {

    @Inject
    ReviewRepository reviewRepository;

    @Inject
    ProductRepository productRepository;

    public List<ReviewDto> findReviewsByProductId(Long id) {
        log.debug("Request to get all Reviews");

        return reviewRepository.findReviewsByProductId(id).stream().map(ReviewService::mapToDto)
                .collect(Collectors.toList());
    }

    public ReviewDto findById(Long id) {
        log.debug("Request to get Review: {}", id);

        return reviewRepository.findById(id).map(ReviewService::mapToDto).orElse(null);
    }

    public ReviewDto create(ReviewDto reviewDto, long productId) {
        log.debug("Request to create Review: {} of the Product {}", reviewDto, productId);

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product with ID: " + productId + " was not found"));

        var savedReview = reviewRepository.saveAndFlush(new Review(
                reviewDto.getTitle(),
                reviewDto.getDescription(),
                reviewDto.getRating()));
        product.getReviews().add(savedReview);
        productRepository.saveAndFlush(product);
        return mapToDto(savedReview);
    }

    public void delete(Long reviewId) {
        log.debug("Request to delete Review: {}", reviewId);

        var review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("Product with ID:" + reviewId + " was not found!"));
        var product = productRepository.findProductByReviewId(reviewId);
        product.getReviews().remove(review);
        productRepository.saveAndFlush(product);
        reviewRepository.delete(review);
    }

    public static ReviewDto mapToDto(Review review) {
        return new ReviewDto(
            review.getId(),
            review.getTitle(),
            review.getDescription(),
            review.getRating()
        );
    }
}
