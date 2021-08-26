package com.karold.onlinestore.service.impl;

import com.karold.onlinestore.exception.NotEnoughProductQuantity;
import com.karold.onlinestore.exception.ProductNotFoundException;
import com.karold.onlinestore.model.Cart;
import com.karold.onlinestore.model.CartItem;
import com.karold.onlinestore.model.Product;
import com.karold.onlinestore.repository.CartItemRepository;
import com.karold.onlinestore.repository.CartRepository;
import com.karold.onlinestore.repository.ProductRepository;
import com.karold.onlinestore.repository.UserRepository;
import com.karold.onlinestore.service.CartService;
import com.karold.onlinestore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;

    private ProductRepository productRepository;

    private CartItemRepository cartItemRepository;

    private UserRepository userRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, CartItemRepository cartItemRepository, UserRepository userRepository, ProductServiceImpl productService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CartItem addItemToCart(String email, Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(()->{throw new ProductNotFoundException(productId);});
        Cart cart = cartRepository.findByUser_Email(email)
                .orElseGet(() -> cartRepository.save(new Cart(userRepository.getByEmail(email))));

        if (product.isAmountAvailable(quantity)) {
            return cartItemRepository.findByCartAndProduct(cart, product).map(dbCartItem -> {
                dbCartItem.increaseQuantity(quantity);
                return cartItemRepository.save(dbCartItem);
            }).orElseGet(() -> cartItemRepository.save(new CartItem(cart, product, quantity)));
        } else {
            throw new NotEnoughProductQuantity(product.getQuantity(), quantity);
        }
    }

    @Override
    public void removeItemFromCart(String email, Long cartItemId) {
        cartRepository.findByUser_Email(email).ifPresent(cart -> {
            cartItemRepository.deleteByCartAndId(cart, cartItemId);
        });
    }

    @Override
    public void changeQuantityForCartItem(String email, Long cartItemId, int quantity) {
        cartRepository.findByUser_Email(email).flatMap(cart -> cartItemRepository.findByCartAndId(cart, cartItemId)).ifPresent(cartItem -> {
            cartItem.changeQuantity(quantity);
            cartItemRepository.save(cartItem);
        });
    }

    @Override
    public Cart getUserCart(String email) {
        return cartRepository.findByUser_Email(email).orElseGet(() -> cartRepository.save(new Cart(userRepository.getByEmail(email))));

    }
}
