package com.school.storeapplication.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartDto {

    private List<CartItemDto> items = new ArrayList<>();
    private BigDecimal total = BigDecimal.ZERO;

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
        recalcTotal();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void recalcTotal() {
        BigDecimal sum = BigDecimal.ZERO;
        if (items != null) {
            for (CartItemDto item : items) {
                if (item.getLineTotal() != null) {
                    sum = sum.add(item.getLineTotal());
                }
            }
        }
        this.total = sum;
    }
}
