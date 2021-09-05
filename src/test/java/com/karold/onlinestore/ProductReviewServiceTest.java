package com.karold.onlinestore;

import com.karold.onlinestore.exception.ProductNotFoundException;
import com.karold.onlinestore.exception.ProductReviewCannotBeEdited;
import com.karold.onlinestore.exception.ResourceNotFoundException;
import com.karold.onlinestore.model.*;
import com.karold.onlinestore.payload.ProductReviewRequest;
import com.karold.onlinestore.repository.ProductRepository;
import com.karold.onlinestore.repository.ProductReviewRepository;
import com.karold.onlinestore.repository.UserRepository;
import com.karold.onlinestore.service.impl.ProductReviewServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductReviewServiceTest {

    @Mock
    private ProductReviewRepository productReviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductReviewServiceImpl productReviewService;

    private Product validProduct;

    private User user;

    private ProductReviewRequest request;

    private ProductReview review;

    @Before
    public void init() {
        request = new ProductReviewRequest(1L, "Review title", "Lorem impsum dolot sit amet.");
        validProduct = new Product(1L, "Samsung Galaxy s10", BigDecimal.valueOf(2744.00), 27);
        user = new User(1L, "John", "Doe", "example@gmail.com", "password", UserType.CUSTOMER, new Address());
        review = new ProductReview(user, validProduct, "Review Title", "Lorem impsum dolor sit amet.");
    }

    @Test
    public void addProductReview_ValidDataGiven_ShouldAddProductReview() {
        when(userRepository.getByEmail(any(String.class))).thenReturn(user);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(validProduct));
        when(productReviewRepository.save(any(ProductReview.class))).thenAnswer(i -> {
            ProductReview review = i.getArgument(0);
            return review;
        });

        ProductReview result = productReviewService.addProductReview("example@gmail.com", request);
        Assert.assertEquals(user, result.getAuthor());
        Assert.assertEquals(validProduct, result.getProduct());
        Assert.assertEquals(request.getContent(), result.getContent());
        Assert.assertEquals(request.getTitle(), result.getTitle());
    }

    @Test
    public void addProductReview_shouldThrowProductNotFound() {
        when(userRepository.getByEmail(any(String.class))).thenReturn(user);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assert.assertThrows(ProductNotFoundException.class, () -> productReviewService.addProductReview("example@gmail.com", request));
    }

    @Test
    public void editProductReview_ShouldReturnEditedReview() {
        when(productReviewRepository.findById(any(Long.class))).thenReturn(Optional.of(review));
        when(userRepository.getByEmail("example@gmail.com")).thenReturn(user);
        when(productReviewRepository.save(any(ProductReview.class))).thenAnswer(i -> {
            ProductReview editedReview = i.getArgument(0);
            return editedReview;
        });

        ProductReviewRequest editRequest = new ProductReviewRequest(1L, "Review Title Edited", "Review content edited");
        ProductReview result = productReviewService.editProductReview("example@gmail.com", 1L, editRequest);
        Assert.assertEquals(editRequest.getTitle(), result.getTitle());
        Assert.assertEquals(editRequest.getContent(), result.getContent());
    }

    @Test
    public void editProductReview_ShouldThrowResourceNotFound() {
        when(productReviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        ProductReviewRequest editRequest = new ProductReviewRequest(1L, "Review Title Edited", "Review content edited");
        Assert.assertThrows(ResourceNotFoundException.class, () -> productReviewService.editProductReview("example@gmail.com", 2L, editRequest));
    }

    @Test
    public void deleteProductReview_ShouldThrowResourceNotFound() {
        when(productReviewRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assert.assertThrows(ResourceNotFoundException.class, () -> productReviewService.deleteProductReview("example@gmail.com", 2L));
    }

    @Test
    public void deleteProductReview_ShouldThrowProductReviewCannotBeEdited() {
        when(productReviewRepository.findById(any(Long.class))).thenReturn(Optional.of(review));
        when(userRepository.getByEmail(any(String.class))).thenReturn(new User());
        Assert.assertThrows(ProductReviewCannotBeEdited.class, () -> productReviewService.deleteProductReview("email@abc.com", 1L));
    }
}
