package com.store_management.store_management.entity;

import java.time.LocalDate;

public class ProductStock {
    private int stockId;
    private LocalDate date;
    private int productId;
    private int branchId;
    private Double quantity;

    public ProductStock(int stockId, Double quantity, int branchId, int productId, LocalDate date) {
        this.stockId = stockId;
        this.quantity = quantity;
        this.branchId = branchId;
        this.productId = productId;
        this.date = date;
    }

    public ProductStock() {
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }
}
