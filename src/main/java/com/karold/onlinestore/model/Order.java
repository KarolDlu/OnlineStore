package com.karold.onlinestore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    private UUID orderNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne
    private Cart cart;

    @NotNull
    private BigDecimal totalPrice;

    @NotNull
    private LocalDateTime orderDate;

    public Order(Cart cart, @NotNull BigDecimal totalPrice) {
        this.status = OrderStatus.PAYMENT_COMPLETE;
        this.cart = cart;
        this.totalPrice = totalPrice;
        this.orderDate = LocalDateTime.now();
    }

    public void changeOrderStatus(OrderStatus status) {
        this.status = status;
    }
}
