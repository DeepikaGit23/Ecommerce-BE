package com.si.ecommerce.services;

import com.si.ecommerce.domain.Cart;
import com.si.ecommerce.domain.CartItem;
import com.si.ecommerce.domain.Product;
import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.models.CartItemRequest;
import com.si.ecommerce.models.CartResponse;
import com.si.ecommerce.models.CartUpdateRequest;
import com.si.ecommerce.repository.CartItemRepository;
import com.si.ecommerce.repository.CartRepository;
import com.si.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CartItemService {
    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    private final CartService cartService;

    public CartResponse createCartItem(int userId, CartItemRequest cartItemRequest)
            throws NotFoundException {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        Cart cart = optionalCart.orElseThrow(() -> new NotFoundException("Cart not found"));

        Optional<Product> optionalProduct = productRepository.findById(cartItemRequest.getProductId());
        Product product = optionalProduct.orElseThrow(() -> new NotFoundException("Product not found"));

        List<CartItem> cartItemList = cartItemRepository.findAllByCartId(cart.getId());
        for (CartItem cartItem: cartItemList) {
            if (cartItem.getProduct().getId() == cartItemRequest.getProductId()) {
                updateCartItemQuantity(cartItem.getId(), cartItem.getQuantity() + cartItemRequest.getQuantity());
                return cartService.getCart(cart.getUser().getId());
            }
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemRequest.getQuantity());

        cartItemRepository.save(cartItem);
        return cartService.getCart(cart.getUser().getId());
    }

    public void updateCartItem(int cartItemId, CartUpdateRequest cartUpdateRequest) throws NotFoundException {
        updateCartItemQuantity(cartItemId, cartUpdateRequest.getQuantity());
    }

    public void deleteCartItem(int cartItemId) throws NotFoundException {
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        CartItem cartItem = optionalCartItem.orElseThrow(() -> new NotFoundException("Cart item not found"));
        cartItemRepository.delete(cartItem);
    }

    private void updateCartItemQuantity(int cartItemId, int quantity) throws NotFoundException {
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        CartItem cartItem = optionalCartItem.orElseThrow(() -> new NotFoundException("Cart item not found"));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }
}

