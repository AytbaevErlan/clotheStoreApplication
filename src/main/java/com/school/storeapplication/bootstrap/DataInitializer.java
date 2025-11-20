package com.school.storeapplication.bootstrap;

import com.school.storeapplication.domain.Role;
import com.school.storeapplication.domain.catalog.Category;
import com.school.storeapplication.domain.catalog.Product;
import com.school.storeapplication.domain.user.User;
import com.school.storeapplication.repo.CategoryRepository;
import com.school.storeapplication.repo.ProductRepository;
import com.school.storeapplication.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(
            CategoryRepository categories,
            ProductRepository products,
            UserRepository users,
            PasswordEncoder encoder
    ) {
        return args -> {
            if (categories.count() == 0) {
                var men = categories.save(Category.builder().name("Men").build());
                var women = categories.save(Category.builder().name("Women").build());

                products.save(Product.builder()
                        .name("Classic T-Shirt")
                        .description("100% cotton T-Shirt")
                        .price(new BigDecimal("49.99"))
                        .sku("TSHIRT-001")
                        .stock(100)
                        .imageUrl("https://picsum.photos/200/300")
                        .category(men)
                        .active(true)
                        .build());

                products.save(Product.builder()
                        .name("Denim Jacket")
                        .description("Slim fit denim jacket")
                        .price(new BigDecimal("199.99"))
                        .sku("JACKET-001")
                        .stock(50)
                        .imageUrl("https://picsum.photos/200/301")
                        .category(women)
                        .active(true)
                        .build());
            }

            if (users.count() == 0) {
                users.save(User.builder()
                        .email("buyer@example.com")
                        .password(encoder.encode("password"))
                        .firstName("Buyer")
                        .lastName("User")
                        .roles(Set.of(Role.BUYER))
                        .build());

                users.save(User.builder()
                        .email("seller@example.com")
                        .password(encoder.encode("password"))
                        .firstName("Seller")
                        .lastName("User")
                        .roles(Set.of(Role.SELLER))
                        .build());

                users.save(User.builder()
                        .email("admin@store.com")
                        .password(encoder.encode("Admin#123"))
                        .firstName("Admin")
                        .lastName("User")
                        .roles(Set.of(Role.ADMIN))
                        .build());
            }
        };
    }
}
