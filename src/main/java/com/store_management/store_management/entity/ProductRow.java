package com.store_management.store_management.entity;

import javafx.beans.property.*;

public class ProductRow {
    private final StringProperty productCode;
    private final StringProperty name;
    private final DoubleProperty quantity;
    private final DoubleProperty returnQuantity;  // new property


    public ProductRow(String productCode, String name) {
        this.productCode = new SimpleStringProperty(productCode);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleDoubleProperty(0.0);
        this.returnQuantity = new SimpleDoubleProperty(0.0);  // initialize new property
    }

    public double getReturnQuantity() {
        return returnQuantity.get();
    }

    public DoubleProperty returnQuantityProperty() {
        return returnQuantity;
    }

    public void setReturnQuantity(double returnQuantity) {
        this.returnQuantity.set(returnQuantity);
    }

    public String getProductCode() {
        return productCode.get();
    }

    public StringProperty productCodeProperty() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode.set(productCode);
    }

    // name
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    // quantity
    public double getQuantity() {
        return quantity.get();
    }

    public DoubleProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity.set(quantity);
    }
}
