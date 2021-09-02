package com.karold.onlinestore.controller;

import com.karold.onlinestore.model.Cart;
import com.karold.onlinestore.model.CartItem;
import com.karold.onlinestore.service.CartService;
import com.karold.onlinestore.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.security.Principal;

@RestController
@RequestMapping("/cart")
@PreAuthorize("hasAuthority('CUSTOMER')")
public class CartController {

    private CartService cartService;

    @Autowired
    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add/product/{productId}/quantity/{quantity}")
    public ResponseEntity<CartItem> addProductToCart(@PathVariable Long productId, @PathVariable @Positive int quantity, Principal principal) {
        return ResponseEntity.ok(cartService.addItemToCart(principal.getName(), productId, quantity));
    }

    @GetMapping("/get")
    public ResponseEntity<Cart> getCart(Principal principal) {
        return ResponseEntity.ok(cartService.getUserCart(principal.getName()));
    }

    @PostMapping("/changeQuantity/cartItem/{cartItemId}/quantity/{quantity}")
    public ResponseEntity<String> changeQuantity(@PathVariable Long cartItemId, @PathVariable int quantity, Principal principal) {
        cartService.changeQuantityForCartItem(principal.getName(), cartItemId, quantity);
        return ResponseEntity.ok("Cart item quantity has been updated");
    }

    @DeleteMapping("/remove/cartItem/{cartItemId}")
    public ResponseEntity<String> removeProductFromCart(@PathVariable Long cartItemId, Principal principal) {
        cartService.removeItemFromCart(principal.getName(), cartItemId);
        return ResponseEntity.ok("Product has been removed");
    }
}
