package com.karold.onlinestore.controller;

import com.karold.onlinestore.model.Order;
import com.karold.onlinestore.model.OrderStatus;
import com.karold.onlinestore.payload.ChangeOrderStatusRequest;
import com.karold.onlinestore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/makeOrder")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Order> makeOrder(Principal principal){
        return ResponseEntity.ok(orderService.makeOrder(principal.getName()));
    }

    @PostMapping("/change/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> changeOrderStatus(@RequestParam ChangeOrderStatusRequest status){
        orderService.changeOrderStatus(status.getOrderNumber(), status.getStatus());
        return ResponseEntity.ok("Order status changed!");
    }
}
