package com.school.storeapplication.service;

import com.school.storeapplication.domain.catalog.Product;
import com.school.storeapplication.domain.order.Order;
import com.school.storeapplication.domain.order.OrderItem;
import com.school.storeapplication.domain.order.OrderStatus;
import com.school.storeapplication.domain.user.User;
import com.school.storeapplication.dto.CartDto;
import com.school.storeapplication.dto.CartItemDto;
import com.school.storeapplication.repo.OrderRepository;
import com.school.storeapplication.repo.ProductRepository;
import com.school.storeapplication.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.Collections;


import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orders;
    private final UserRepository users;
    private final ProductRepository products;

    public OrderService(OrderRepository orders,
                        UserRepository users,
                        ProductRepository products) {
        this.orders = orders;
        this.users = users;
        this.products = products;
    }

    @Transactional
    public Order placeOrder(Principal principal, CartDto cart) {
        if (principal == null) {
            throw new IllegalStateException("User must be logged in to place an order");
        }
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot place order with empty cart");
        }

        User buyer = users.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Order order = new Order();
        order.setBuyer(buyer);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW);

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItemDto dto : cart.getItems()) {
            Product product = products.findById(dto.getProductId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Product not found: " + dto.getProductId()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(dto.getUnitPrice());
            item.setLineTotal(dto.getLineTotal());

            orderItems.add(item);
            total = total.add(dto.getLineTotal());
        }

        order.setItems(orderItems);
        order.setTotal(total);

        return orders.save(order);
    }

    @Transactional
    public List<Order> getOrdersForUser(Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("User must be logged in");
        }

        User buyer = users.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        return orders.findByBuyerOrderByCreatedAtDesc(buyer);
    }

    @Transactional
    public Order getOrderForUser(Long id, Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("User must be logged in");
        }

        User buyer = users.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        return orders.findByIdAndBuyer(id, buyer)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Transactional
    public Order updateOrderStatusForUser(Long id,
                                          OrderStatus newStatus,
                                          Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("User must be logged in");
        }

        User buyer = users.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Order order = orders.findByIdAndBuyer(id, buyer)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.setStatus(newStatus);
        return orders.save(order);
    }



    @Transactional
    public List<Order> getAllOrdersAdmin() {
        List<Order> all = orders.findAllByOrderByCreatedAtDesc();
        return all != null ? all : Collections.emptyList();
    }

    @Transactional
    public Order getOrderAdmin(Long id) {
        return orders.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Transactional
    public Order updateOrderStatusAdmin(Long id, OrderStatus newStatus) {
        Order order = orders.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.setStatus(newStatus);
        return orders.save(order);
    }


}
