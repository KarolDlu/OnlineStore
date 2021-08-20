package com.karold.onlinestore.service;

import com.karold.onlinestore.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product addProduct(Product product);

    List<Product> getAllProducts();

    Product getProductById(Long productId);

    Product updateProductQuantity(Long productId, int quantity);

    Product updateProductPrice(Long productId, BigDecimal price);

    boolean deleteProduct(Long productId);

}
