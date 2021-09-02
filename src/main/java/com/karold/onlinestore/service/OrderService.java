package com.karold.onlinestore.service;

import com.karold.onlinestore.model.Cart;
import com.karold.onlinestore.model.Order;
import com.karold.onlinestore.model.OrderStatus;

import java.util.UUID;

public interface OrderService {

    Order makeOrder(String email);

    void changeOrderStatus(UUID orderNumber, OrderStatus orderStatus);

}
