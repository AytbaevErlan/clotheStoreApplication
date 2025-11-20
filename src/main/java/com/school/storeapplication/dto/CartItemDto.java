package com.school.storeapplication.dto;

import java.math.BigDecimal;

public class CartItemDto {

    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;

    public CartItemDto() {
    }

    public CartItemDto(Long productId,
                       String productName,
                       int quantity,
                       BigDecimal unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        recalcLineTotal();
    }

    public void recalcLineTotal() {
        if (unitPrice == null) {
            this.lineTotal = BigDecimal.ZERO;
        } else {
            this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }



    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        recalcLineTotal();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        recalcLineTotal();
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }
}
