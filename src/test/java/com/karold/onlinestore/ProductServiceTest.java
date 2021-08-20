package com.karold.onlinestore;

import com.karold.onlinestore.exception.NegativeProductQuantityException;
import com.karold.onlinestore.exception.ProductAlreadyExistsException;
import com.karold.onlinestore.exception.ProductNotFoundException;
import com.karold.onlinestore.model.Product;
import com.karold.onlinestore.repository.ProductRepository;
import com.karold.onlinestore.service.impl.ProductServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private LocalValidatorFactoryBean validator;

    private Product validProduct;

    @Before
    public void init(){
        validator = new LocalValidatorFactoryBean();
        validProduct = new Product(1l, "Samsung Galaxy s10", BigDecimal.valueOf(2744.00), 27);
    }

    @Test
    public void addProduct_ValidProductGiven_ReturnAddedProduct() {
        when(productRepository.save(validProduct)).thenReturn(validProduct);
        Product result = productService.addProduct(validProduct);
        Assert.assertEquals(validProduct, result);
    }

    @Test
    public void addProduct_ProductWithAlreadyExistingNameGiven_ShouldThrowProductAlreadyExist() {
        when(productRepository.findByName("Samsung Galaxy s10")).thenReturn(Optional.of(validProduct));
        Product givenProduct = new Product(2l, "Samsung Galaxy s10", BigDecimal.valueOf(1457.00), 27);
        Assert.assertThrows(ProductAlreadyExistsException.class, () -> {
            productService.addProduct(givenProduct);
        });
    }

    @Test
    public void addProduct_ProductWithNegativeQuantityGiven_ShouldThrowConstraintViolationException() {
        validator.afterPropertiesSet();
        Product product = new Product(1l, "Samsung Galaxy s10", BigDecimal.valueOf(2744.00), -27);
        Set<ConstraintViolation<Product>> violations = validator.validate(product, Default.class);
        when(productRepository.save(any(Product.class))).thenThrow(new ConstraintViolationException(violations));
        Assert.assertThrows(ConstraintViolationException.class, () -> productService.addProduct(product));
        Assert.assertEquals(1, violations.size());

    }

    @Test
    public void getAllProduct_ShouldReturnListOfProduct() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1l, "Samsung Galaxy s10", BigDecimal.valueOf(1744.00), 27));
        products.add(new Product(2l, "Asus Zenfone 8", BigDecimal.valueOf(2684.00), 31));
        products.add(new Product(3l, "Lenovo Legion Y520-15 i5-7300HQ/8GB/1000 GTX1050", BigDecimal.valueOf(3449.00), 17));
        when(productRepository.findAll()).thenReturn(products);
        Assert.assertEquals(products, productService.getAllProducts());
    }

    @Test
    public void getProduct_NotExistingProductIdGiven_ShouldThrowProductNotFoundException() {
        when(productRepository.findById(1l)).thenReturn(Optional.empty());
        Assert.assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(1l);
        });
    }

    @Test
    public void getProduct_ExistingProductIdGiven_ShouldReturnProductWithGivenId() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(validProduct));
        Product result = productService.getProductById(1l);
        Assert.assertEquals(validProduct, result);
    }

    @Test
    public void updateProductQuantity_ShouldReturnProductWithUpdatedQuantity() {
        when(productRepository.findById(1l)).thenReturn(Optional.of(validProduct));
        when(productRepository.save(any(Product.class))).thenReturn(validProduct);
        Product updatedProduct = productService.updateProductQuantity(1l, 29);
        Assert.assertEquals(29, updatedProduct.getQuantity());
    }

    @Test
    public void updateProductQuantity_NegativeQuantityGiven_ShouldThrowException() {
        when(productRepository.findById(1l)).thenReturn(Optional.of(validProduct));
        Assert.assertThrows(NegativeProductQuantityException.class, () -> productService.updateProductQuantity(1l, -12));
    }
}
