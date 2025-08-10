package com.store_management.store_management.dto;

public class MonthlySummaryView {
    private String branchName;
    private String productName;
    private Double totalQuantity;

    // getters and setters

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Double getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(Double totalQuantity) { this.totalQuantity = totalQuantity; }
}
