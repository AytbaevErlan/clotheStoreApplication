package com.school.storeapplication.web;

import com.school.storeapplication.domain.catalog.Product;
import com.school.storeapplication.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/seller")
public class SellerController {

    private final CatalogService catalogService;

    public SellerController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /**
     * Simple seller dashboard page.
     * URL: GET /seller/dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        String name = (principal != null) ? principal.getName() : "Seller";
        model.addAttribute("sellerName", name);
        return "seller-dashboard";   // -> seller-dashboard.html
    }

    /**
     * List all products for now (later you can filter by seller).
     * URL: GET /seller/products
     */
    @GetMapping("/products")
    public String listSellerProducts(Model model) {
        List<Product> products = catalogService.getAll();
        model.addAttribute("products", products);
        return "seller-products";    // -> seller-products.html
    }
}
