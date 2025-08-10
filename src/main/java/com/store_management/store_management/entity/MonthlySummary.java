package com.store_management.store_management.entity;

public class MonthlySummary {
    private int branchId;
    private int productId;
    private String productName;
    private double totalQuantity;

    public MonthlySummary() {}

    public MonthlySummary(int branchId, int productId, String productName, double totalQuantity) {
        this.branchId = branchId;
        this.productId = productId;
        this.productName = productName;
        this.totalQuantity = totalQuantity;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    public String toString() {
        return "MonthlySummary{" +
                "branchId=" + branchId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", totalQuantity=" + totalQuantity +
                '}';
    }
}
