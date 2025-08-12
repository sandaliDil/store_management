package com.store_management.store_management.entity;

import javafx.beans.property.*;

public class Product {
    private final IntegerProperty productId;
    private final StringProperty productName;
    private final DoubleProperty unitPrice;
    private final StringProperty productCode;

    public Product() {
        this.productId = new SimpleIntegerProperty();
        this.productName = new SimpleStringProperty();
        this.unitPrice = new SimpleDoubleProperty();
        this.productCode = new SimpleStringProperty();
    }

    public Product(int id, String productName, double unitPrice, String productCode) {
        this.productId = new SimpleIntegerProperty(id);
        this.productName = new SimpleStringProperty(productName);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.productCode = new SimpleStringProperty(productCode);
    }

    public int getId() {
        return productId.get();
    }

    public void setId(int id) {
        this.productId.set(id);
    }

    public IntegerProperty idProperty() {
        return productId;
    }

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public DoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    public String getProductCode() {
        return productCode.get();
    }

    public void setProductCode(String productCode) {
        this.productCode.set(productCode);
    }

    public StringProperty productCodeProperty() {
        return productCode;
    }
}
