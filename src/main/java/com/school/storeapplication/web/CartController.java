package com.school.storeapplication.web;

import com.school.storeapplication.dto.CartDto;
import com.school.storeapplication.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }



    // GET /cart -> show cart page
    @GetMapping("/cart")
    public String cartPage(HttpSession session, Model model) {
        CartDto cart = cartService.getCart(session);
        model.addAttribute("cart", cart);
        return "cart"; // cart.html
    }



    // POST /cart/add/{productId}  (used from product list + product details)
    @PostMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable Long productId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        try {
            cartService.addToCart(session, productId);
            redirectAttributes.addFlashAttribute("message", "Product added to cart.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Failed to add to cart: " + ex.getMessage()
            );
        }

        // you can change to "redirect:/cart" if you prefer going to cart
        return "redirect:/products";
    }



    // POST /cart/update  (update quantity of a line item)
    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam("productId") Long productId,
                                 @RequestParam("quantity") int quantity,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        try {
            cartService.updateItemQuantity(session, productId, quantity);
            redirectAttributes.addFlashAttribute("message", "Cart updated.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Failed to update cart: " + ex.getMessage()
            );
        }

        return "redirect:/cart";
    }


    // POST /cart/remove  (remove a line item)
    @PostMapping("/cart/remove")
    public String removeItem(@RequestParam("productId") Long productId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        try {
            cartService.removeItem(session, productId);
            redirectAttributes.addFlashAttribute("message", "Item removed from cart.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Failed to remove item: " + ex.getMessage()
            );
        }

        return "redirect:/cart";
    }
}
