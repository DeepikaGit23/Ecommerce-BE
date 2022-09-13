package com.si.ecommerce.services;

import com.si.ecommerce.domain.Address;
import com.si.ecommerce.domain.Cart;
import com.si.ecommerce.domain.CartItem;
import com.si.ecommerce.domain.Order;
import com.si.ecommerce.domain.OrderItem;
import com.si.ecommerce.domain.Product;
import com.si.ecommerce.domain.User;
import com.si.ecommerce.exceptions.BadRequestException;
import com.si.ecommerce.exceptions.NotFoundException;
import com.si.ecommerce.models.OrderItemResponse;
import com.si.ecommerce.models.OrderRequest;
import com.si.ecommerce.models.OrderResponse;
import com.si.ecommerce.models.ProductResponse;
import com.si.ecommerce.repository.AddressRepository;
import com.si.ecommerce.repository.CartItemRepository;
import com.si.ecommerce.repository.CartRepository;
import com.si.ecommerce.repository.OrderItemRepository;
import com.si.ecommerce.repository.OrderRepository;
import com.si.ecommerce.repository.ProductRepository;
import com.si.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderService {

    private final CartRepository cartRepository;

    private final AddressRepository addressRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final OrderItemRepository orderItemRepository;

    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderRequest orderRequest) throws NotFoundException, BadRequestException {
        Optional<Cart> optionalCart = cartRepository.findById(orderRequest.getCartId());
        Cart cart = optionalCart.orElseThrow(() -> new NotFoundException("Cart not found"));

        Optional<Address> optionalAddress = addressRepository.findById(orderRequest.getAddressId());
        Address address = optionalAddress.orElseThrow(() -> new NotFoundException("Address not found"));

        Optional<User> optionalUser = userRepository.findById(orderRequest.getUserId());
        User user = optionalUser.orElseThrow(() -> new NotFoundException("User details not found"));

        List<CartItem> cartItemList = cartItemRepository.findAllByCartId(cart.getId());

        if (cartItemList.isEmpty()) {
            throw new BadRequestException("Please add products to create order");
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(Order.OrderStatus.PLACED);
        orderRepository.save(order);

        List<OrderItemResponse> orderItemResponseList = new ArrayList<>();

        for (CartItem cartItem : cartItemList) {

            Optional<Product> optionalProduct = productRepository.findById(cartItem.getProduct().getId());
            Product product = optionalProduct.orElseThrow(() -> new NotFoundException("Product details not found"));

            if (product.getAvailableQuantity() < cartItem.getQuantity()) {
                throw new BadRequestException("Insufficient product quantity");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setPrice(product.getPrice());
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItemRepository.save(orderItem);

            product.setAvailableQuantity(product.getAvailableQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            orderItemResponseList.add(OrderItemResponse.from(orderItem));

            cartItemRepository.deleteById(cartItem.getId());
        }

        orderRepository.save(order);
        return OrderResponse.from(order, orderItemResponseList);
    }

    public List<OrderResponse> getAllOrder(int userId) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()) {
            throw new NotFoundException("User details not found");
        }

        List<Order> orderList = orderRepository.findAllByUserId(userId);

        List<OrderResponse> orderResponseList = new ArrayList<>();

        for (Order order: orderList) {
            List<OrderItem> orderItemList = orderItemRepository.findOrderItemByOrderId(order.getId());

            List<OrderItemResponse> orderItemResponseList = new ArrayList<>();

            for (OrderItem orderItem: orderItemList) {
                orderItemResponseList.add(OrderItemResponse.from(orderItem));
            }

            orderResponseList.add(OrderResponse.from(order,orderItemResponseList));
        }

        return orderResponseList;
    }

}