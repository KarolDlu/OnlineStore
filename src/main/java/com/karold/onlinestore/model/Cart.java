package com.karold.onlinestore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems;

    public Cart(User user) {
        this.user = user;
    }

    public BigDecimal calculatePrice(){
        BigDecimal price = BigDecimal.valueOf(0);
        this.cartItems.forEach(item -> {
            price.add(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        });
        return price;
    }
}
