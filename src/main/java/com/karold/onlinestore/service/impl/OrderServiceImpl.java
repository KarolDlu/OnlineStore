package com.karold.onlinestore.service.impl;

import com.karold.onlinestore.exception.ResourceNotFoundException;
import com.karold.onlinestore.model.Cart;
import com.karold.onlinestore.model.CartItem;
import com.karold.onlinestore.model.Order;
import com.karold.onlinestore.model.OrderStatus;
import com.karold.onlinestore.repository.CartItemRepository;
import com.karold.onlinestore.repository.CartRepository;
import com.karold.onlinestore.repository.OrderRepository;
import com.karold.onlinestore.repository.ProductRepository;
import com.karold.onlinestore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private CartItemRepository cartItemRepository;
    private CartRepository cartRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, CartItemRepository cartItemRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public Order makeOrder(String email) {
        Cart cart = cartRepository.findByUser_Email(email).orElseThrow(() -> {
            throw new ResourceNotFoundException("Cart", "email", email);
        });
        List<CartItem> items = cart.getCartItems();
        items.forEach(cartItem -> {
            cartItem.getProduct().reserveProductAmount(cartItem.getQuantity());
            productRepository.save(cartItem.getProduct());
        });
        BigDecimal price = cart.calculatePrice();
        Order order = new Order(cart, price);
        return orderRepository.save(order);
    }

    @Override
    public void changeOrderStatus(UUID orderNumber, OrderStatus orderStatus) {
        orderRepository.findById(orderNumber).orElseThrow(() -> {
            throw new ResourceNotFoundException("Order", "id", orderNumber);
        });
    }
}
