package org.lslonina.shop.repository;

import java.util.List;

import org.lslonina.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    Long countAllByCategoryId(Long categoryId);

    @Query("SELECT p from Product p JOIN p.reviews r WHERE r.id = ?1")
    Product findProductByReviewId(Long reviewid);

    void deleteAllByCategoryId(Long id);

    List<Product> findAllByCategoryId(Long id);
}
