package com.school.storeapplication.web;

import com.school.storeapplication.domain.catalog.Category;
import com.school.storeapplication.domain.catalog.Product;
import com.school.storeapplication.repo.CategoryRepository;
import com.school.storeapplication.repo.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    private final ProductRepository products;
    private final CategoryRepository categories;

    public ProductController(ProductRepository products,
                             CategoryRepository categories) {
        this.products = products;
        this.categories = categories;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }


    @GetMapping("/products")
    public String listProducts(@RequestParam(name = "categoryId", required = false) Long categoryId,
                               @RequestParam(name = "q", required = false) String query,
                               Model model) {

        List<Product> all = products.findAll();


        if (categoryId != null) {
            all = all.stream()
                    .filter(p -> p.getCategory() != null
                            && p.getCategory().getId().equals(categoryId))
                    .collect(Collectors.toList());
        }

        if (query != null && !query.isBlank()) {
            String qLower = query.toLowerCase();
            all = all.stream()
                    .filter(p ->
                            p.getName().toLowerCase().contains(qLower)
                                    || p.getDescription().toLowerCase().contains(qLower)
                                    || p.getSku().toLowerCase().contains(qLower))
                    .collect(Collectors.toList());
        }

        List<Category> allCategories = categories.findAll();

        model.addAttribute("products", all);
        model.addAttribute("categories", allCategories);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("query", query);

        return "products"; // products.html
    }


    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        Product product = products.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        List<Product> related = products.findAll().stream()
                .filter(p -> !p.getId().equals(product.getId()))
                .filter(p -> p.getCategory() != null
                        && product.getCategory() != null
                        && p.getCategory().getId().equals(product.getCategory().getId()))
                .limit(4)
                .collect(Collectors.toList());

        model.addAttribute("product", product);
        model.addAttribute("related", related);

        return "product-details"; // product-details.html
    }
}
