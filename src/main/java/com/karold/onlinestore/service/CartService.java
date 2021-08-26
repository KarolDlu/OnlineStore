package com.karold.onlinestore.service;

import com.karold.onlinestore.model.Cart;
import com.karold.onlinestore.model.CartItem;

public interface CartService {

    CartItem addItemToCart(String email, Long productId, int quantity);

    void removeItemFromCart(String email, Long cartItemId);

    void changeQuantityForCartItem(String email, Long cartItemId, int quantity);

    Cart getUserCart(String email);

}
