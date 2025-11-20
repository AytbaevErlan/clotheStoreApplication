//package com.school.storeapplication.web;
//
//import com.school.storeapplication.domain.catalog.Product;
//import com.school.storeapplication.service.CatalogService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.List;
//
//@Controller
//public class CatalogController {
//
//    private final CatalogService catalogService;
//
//    public CatalogController(CatalogService catalogService) {
//        this.catalogService = catalogService;
//    }
//
//    // GET /products  -> products list page
//    @GetMapping("/products")
//    public String listProducts(Model model) {
//        List<Product> products = catalogService.getAll();
//        model.addAttribute("products", products);
//        return "products";          // view: products.html
//    }
//
//    // GET /product/{id}  -> single product page
//    @GetMapping("/product/{id}")
//    public String productDetails(@PathVariable Long id, Model model) {
//        Product product = catalogService.getById(id);
//        model.addAttribute("product", product);
//        return "product";           // view: product.html
//    }
//}
