package com.school.storeapplication.web;

import com.school.storeapplication.domain.order.Order;
import com.school.storeapplication.domain.order.OrderStatus;
import com.school.storeapplication.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // GET /admin/orders  -> list all orders
    @GetMapping
    public String listAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrdersAdmin();
        model.addAttribute("orders", orders);
        return "admin-orders"; // admin-orders.html
    }

    // GET /admin/orders/{id} -> details for one order
    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderAdmin(id);
        model.addAttribute("order", order);
        return "admin-order-details"; // admin-order-details.html
    }

    // POST /admin/orders/{id}/status -> change status
    @PostMapping("/{id}/status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam("status") String status,
                                    RedirectAttributes redirectAttributes) {

        try {
            OrderStatus newStatus = OrderStatus.valueOf(status);
            Order updated = orderService.updateOrderStatusAdmin(id, newStatus);

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

        return "redirect:/admin/orders/" + id;
    }
}
