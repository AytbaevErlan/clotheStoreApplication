package com.school.storeapplication.service;

import com.school.storeapplication.domain.catalog.Product;
import com.school.storeapplication.dto.CartDto;
import com.school.storeapplication.dto.CartItemDto;
import com.school.storeapplication.repo.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "CART";

    private final ProductRepository products;

    public CartService(ProductRepository products) {
        this.products = products;
    }



    public CartDto getCart(HttpSession session) {
        CartDto cart = (CartDto) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new CartDto();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public void addToCart(HttpSession session, Long productId) {
        addToCart(session, productId, 1);
    }

    public void addToCart(HttpSession session, Long productId, int quantity) {
        if (quantity <= 0) {
            return;
        }

        CartDto cart = getCart(session);

        // check if already exists
        CartItemDto existing = findItem(cart, productId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            Product product = products.findById(productId)
                    .orElseThrow(() ->
                            new IllegalArgumentException("Product not found: " + productId));

            CartItemDto item = new CartItemDto(
                    product.getId(),
                    product.getName(),
                    quantity,
                    product.getPrice()
            );
            cart.getItems().add(item);
        }

        recalcCart(cart);
    }

    public void updateItemQuantity(HttpSession session, Long productId, int quantity) {
        CartDto cart = getCart(session);

        if (quantity <= 0) {

            removeItem(session, productId);
            return;
        }

        CartItemDto item = findItem(cart, productId);
        if (item != null) {
            item.setQuantity(quantity);
            recalcCart(cart);
        }
    }

    public void removeItem(HttpSession session, Long productId) {
        CartDto cart = getCart(session);
        List<CartItemDto> items = cart.getItems();

        if (items == null) {
            return;
        }

        Iterator<CartItemDto> it = items.iterator();
        while (it.hasNext()) {
            CartItemDto item = it.next();
            if (productId.equals(item.getProductId())) {
                it.remove();
                break;
            }
        }

        recalcCart(cart);

        if (cart.getItems().isEmpty()) {
            clearCart(session);
        }
    }



    private CartItemDto findItem(CartDto cart, Long productId) {
        if (cart.getItems() == null) {
            return null;
        }
        for (CartItemDto item : cart.getItems()) {
            if (productId.equals(item.getProductId())) {
                return item;
            }
        }
        return null;
    }

    private void recalcCart(CartDto cart) {
        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }
        for (CartItemDto item : cart.getItems()) {
            if (item.getUnitPrice() == null) {
                item.setUnitPrice(BigDecimal.ZERO);
            }
            item.recalcLineTotal();
        }
        cart.recalcTotal();
    }
}
