package com.karold.onlinestore.model;

import com.karold.onlinestore.exception.IllegalPriceException;
import com.karold.onlinestore.exception.NegativeProductQuantityException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private BigDecimal price;

    @PositiveOrZero
    private int quantity;

    public void updatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.valueOf(0.0)) > 0) {
            this.price = price;
        } else {
            throw new IllegalPriceException();
        }
    }

    public void updateQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        } else {
            throw new NegativeProductQuantityException();
        }
    }

}
