package com.store_management.store_management.entity;

import javafx.beans.property.*;

public class ProductRow {
    private final IntegerProperty productId;
    private final StringProperty name;
    private final DoubleProperty quantity;

    public ProductRow(int productId, String name) {
        this.productId = new SimpleIntegerProperty(productId);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleDoubleProperty(0.0); // default 0.0
    }

    public int getProductId() {
        return productId.get();
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

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
