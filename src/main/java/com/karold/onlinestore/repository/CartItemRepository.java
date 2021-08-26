package com.karold.onlinestore.repository;

import com.karold.onlinestore.model.Cart;
import com.karold.onlinestore.model.CartItem;
import com.karold.onlinestore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    void deleteByCartAndId(Cart cart, Long id);

    Optional<CartItem> findByCartAndId(Cart cart, Long id);
}
