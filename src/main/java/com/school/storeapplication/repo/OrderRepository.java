package com.school.storeapplication.repo;

import com.school.storeapplication.domain.order.Order;
import com.school.storeapplication.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // user-specific lists
    List<Order> findByBuyerOrderByCreatedAtDesc(User buyer);

    Optional<Order> findByIdAndBuyer(Long id, User buyer);

    // admin: all orders
    List<Order> findAllByOrderByCreatedAtDesc();
}
