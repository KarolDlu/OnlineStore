package com.karold.onlinestore.service.impl;

import com.karold.onlinestore.exception.ProductAlreadyExistsException;
import com.karold.onlinestore.exception.ProductNotFoundException;
import com.karold.onlinestore.model.Product;
import com.karold.onlinestore.repository.ProductRepository;
import com.karold.onlinestore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Transactional
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product addProduct(Product product) {
        try {
            findProductByName(product.getName());
            throw new ProductAlreadyExistsException(product.getName());
        } catch (ProductNotFoundException ex) {
            return productRepository.save(product);
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> {
            throw new ProductNotFoundException(productId);
        });
    }

    @Override
    public Product updateProductQuantity(Long productId, int quantity) {
        Product product = getProductById(productId);
        product.updateQuantity(quantity);
        return productRepository.save(product);
    }

    @Override
    public Product updateProductPrice(Long productId, BigDecimal price) {
        Product product = getProductById(productId);
        product.updatePrice(price);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    private Product findProductByName(String name) {
        return productRepository.findByName(name).orElseThrow(() -> {
            throw new ProductNotFoundException(name);
        });
    }
}
