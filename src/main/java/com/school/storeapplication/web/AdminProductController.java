package com.school.storeapplication.web;

import com.school.storeapplication.domain.catalog.Category;
import com.school.storeapplication.domain.catalog.Product;
import com.school.storeapplication.repo.CategoryRepository;
import com.school.storeapplication.repo.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductRepository products;
    private final CategoryRepository categories;

    public AdminProductController(ProductRepository products,
                                  CategoryRepository categories) {
        this.products = products;
        this.categories = categories;
    }

    // GET /admin/products  -> list all products
    @GetMapping
    public String list(Model model) {
        List<Product> all = products.findAll();
        model.addAttribute("products", all);
        return "admin-products"; // admin-products.html
    }

    // GET /admin/products/new -> show create form
    @GetMapping("/new")
    public String createForm(Model model) {
        Product product = new Product();
        product.setActive(true);
        product.setPrice(BigDecimal.ZERO);
        product.setStock(0);

        model.addAttribute("product", product);
        model.addAttribute("categories", categories.findAll());
        model.addAttribute("formTitle", "Create product");
        model.addAttribute("submitLabel", "Create");

        return "admin-product-form"; // admin-product-form.html
    }

    // POST /admin/products -> handle create
    @PostMapping
    public String create(@ModelAttribute("product") Product product,
                         @RequestParam("categoryId") Long categoryId,
                         RedirectAttributes redirectAttributes) {

        Category category = categories.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));

        product.setCategory(category);

        products.save(product);

        redirectAttributes.addFlashAttribute(
                "message",
                "Product '" + product.getName() + "' created."
        );

        return "redirect:/admin/products";
    }

    // GET /admin/products/{id}/edit -> show edit form
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = products.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        model.addAttribute("product", product);
        model.addAttribute("categories", categories.findAll());
        model.addAttribute("formTitle", "Edit product");
        model.addAttribute("submitLabel", "Update");

        return "admin-product-form";
    }

    // POST /admin/products/{id} -> handle update
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("product") Product formProduct,
                         @RequestParam("categoryId") Long categoryId,
                         RedirectAttributes redirectAttributes) {

        Product product = products.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Category category = categories.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));

        // copy editable fields
        product.setName(formProduct.getName());
        product.setDescription(formProduct.getDescription());
        product.setPrice(formProduct.getPrice());
        product.setSku(formProduct.getSku());
        product.setStock(formProduct.getStock());
        product.setImageUrl(formProduct.getImageUrl());
        product.setActive(formProduct.isActive());
        product.setCategory(category);

        products.save(product);

        redirectAttributes.addFlashAttribute(
                "message",
                "Product '" + product.getName() + "' updated."
        );

        return "redirect:/admin/products";
    }

    // POST /admin/products/{id}/delete -> delete product
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         RedirectAttributes redirectAttributes) {

        Product product = products.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        products.delete(product);

        redirectAttributes.addFlashAttribute(
                "message",
                "Product '" + product.getName() + "' deleted."
        );

        return "redirect:/admin/products";
    }
}
