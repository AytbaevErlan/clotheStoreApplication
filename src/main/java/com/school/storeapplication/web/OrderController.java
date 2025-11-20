package com.school.storeapplication.web;

import com.school.storeapplication.domain.order.Order;
import com.school.storeapplication.dto.CartDto;
import com.school.storeapplication.service.CartService;
import com.school.storeapplication.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.school.storeapplication.domain.order.OrderStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService,
                           CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    // GET /orders  -> list of user's orders
    @GetMapping("/orders")
    public String listOrders(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("orders", Collections.emptyList());
            return "orders";  // orders.html
        }

        List<Order> orders = orderService.getOrdersForUser(principal);
        model.addAttribute("orders", orders);
        return "orders";
    }

    // GET /checkout -> show checkout page for current cart
    @GetMapping("/checkout")
    public String checkoutPage(Model model, HttpSession session) {
        CartDto cart = cartService.getCart(session);

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cart", cart);
        return "checkout";      // checkout.html
    }

    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable Long id,
                               Principal principal,
                               Model model) {

        Order order = orderService.getOrderForUser(id, principal); // throws if not found / not owner
        model.addAttribute("order", order);

        return "order-details"; // order-details.html
    }


    // POST /checkout -> place order
    @PostMapping("/checkout")
    public String placeOrder(HttpSession session,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {

        CartDto cart = cartService.getCart(session);

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("message",
                    "Cannot place order with empty cart.");
            return "redirect:/cart";
        }

        if (principal == null) {
            redirectAttributes.addFlashAttribute("message",
                    "Please log in to place an order.");
            return "redirect:/login";
        }

        try {
            Order order = orderService.placeOrder(principal, cart);

            // clear cart after successful order
            cartService.clearCart(session);

            redirectAttributes.addFlashAttribute("message",
                    "Order #" + order.getId() + " has been placed.");

            // simple: go to orders list (we can add order-details later)
            return "redirect:/orders";

        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("message",
                    "Failed to place order: " + ex.getMessage());
            return "redirect:/checkout";
        }
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam("status") String status,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {

        try {
            OrderStatus newStatus = OrderStatus.valueOf(status);
            Order updated = orderService.updateOrderStatusForUser(id, newStatus, principal);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Order #" + updated.getId() + " status updated to " + updated.getStatus()
            );
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Invalid status: " + status
            );
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Failed to update order status: " + ex.getMessage()
            );
        }

        return "redirect:/orders/" + id;
    }

}
