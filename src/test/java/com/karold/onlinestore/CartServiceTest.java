package com.karold.onlinestore;

import com.karold.onlinestore.exception.IllegalProductAmountException;
import com.karold.onlinestore.exception.NotEnoughProductQuantity;
import com.karold.onlinestore.exception.ProductNotFoundException;
import com.karold.onlinestore.model.*;
import com.karold.onlinestore.repository.CartItemRepository;
import com.karold.onlinestore.repository.CartRepository;
import com.karold.onlinestore.repository.ProductRepository;
import com.karold.onlinestore.repository.UserRepository;
import com.karold.onlinestore.service.impl.CartServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Product product;

    private User user;

    private Cart cart;

    private LocalValidatorFactoryBean validator;

    @Before
    public void init() {
        validator = new LocalValidatorFactoryBean();
        product = new Product(1L, "Samsung Galaxy s10", BigDecimal.valueOf(2744.00), 5);
        user = new User(1L, "John", "Doe", "john@doe.com", "password", UserType.CUSTOMER, new Address());
        cart = new Cart(user);
    }

    @Test
    public void addItemToCart_shouldThrowProductNotFoundException() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Assert.assertThrows(ProductNotFoundException.class, () -> cartService.addItemToCart("example@gmail.com", 1L, 2));
    }

    @Test
    public void addItemToCart_givenAmountIsGreaterThenProductQuantity_shouldThrowNotEnoughProductQuantity() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(cartRepository.findByUser_Email(any(String.class))).thenReturn(Optional.empty());
        Assert.assertThrows(NotEnoughProductQuantity.class, () -> cartService.addItemToCart("example@gmail.com", 1L, 7));
    }

    @Test
    public void addItemToCart_givenAmountIsNegativeOrEqualsZero_shouldThrowIllegalProductAmount() {
        Assert.assertThrows(IllegalProductAmountException.class, () -> cartService.addItemToCart("example@gmail.com", 1L, -12));
        Assert.assertThrows(IllegalProductAmountException.class, () -> cartService.addItemToCart("example@gmail.com", 1L, 0));
    }

    @Test
    public void addItemToCart_cartWithGivenEmailDoesNotExists_shouldCreateCartAndAddItem() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(cartRepository.findByUser_Email(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.getByEmail("example@gmail.com")).thenReturn(user);
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> {
            Cart cart = i.getArgument(0);
            return cart;
        });
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(i -> {
            CartItem cartItem = i.getArgument(0);
            return cartItem;
        });
        CartItem result = cartService.addItemToCart("example@gmail.com", 1L, 2);
        Assert.assertEquals(2, result.getQuantity());
        Assert.assertEquals(product, result.getProduct());
        Assert.assertEquals(user, result.getCart().getUser());
    }

    @Test
    public void addItemToCart_cartWithGivenEmailExists_shouldAddItem() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(cartRepository.findByUser_Email(any(String.class))).thenReturn(Optional.of(cart));
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(i -> {
            CartItem cartItem = i.getArgument(0);
            return cartItem;
        });
        CartItem result = cartService.addItemToCart("example@gmail.com", 1L, 2);
        Assert.assertEquals(2, result.getQuantity());
        Assert.assertEquals(product, result.getProduct());
        Assert.assertEquals(user, result.getCart().getUser());
    }

    @Test
    public void addItemToCart_cartAlreadyContainProduct_shouldAddQuantity() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUser_Email(any(String.class))).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndProduct(any(Cart.class), any(Product.class))).thenReturn(Optional.of(new CartItem(cart, product, 2)));
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(i -> {
            CartItem cartItem = i.getArgument(0);
            return cartItem;
        });
        CartItem result = cartService.addItemToCart("john@doe.com", 1L, 3);
        Assert.assertEquals(5, result.getQuantity());
        Assert.assertEquals(product, result.getProduct());
        Assert.assertEquals(user, result.getCart().getUser());
    }

    @Test
    public void addItemToCart_newProductGiven_shouldAddCartItem() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUser_Email(any(String.class))).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndProduct(any(Cart.class), any(Product.class))).thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(i -> {
            CartItem cartItem = i.getArgument(0);
            return cartItem;
        });
        CartItem result = cartService.addItemToCart("john@doe.com", 1L, 2);
        Assert.assertEquals(2, result.getQuantity());
        Assert.assertEquals(product, result.getProduct());
        Assert.assertEquals(user, result.getCart().getUser());
    }

    public void removeItemFromCart(){

    }
}
