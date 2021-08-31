package com.karold.onlinestore.repository;

import com.karold.onlinestore.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    List<ProductReview> findAllByProduct_Id(Long productId);
}
