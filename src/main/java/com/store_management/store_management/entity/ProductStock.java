package com.store_management.store_management.entity;

import javafx.beans.property.*;
import java.time.LocalDate;

public class ProductStock {
    private final IntegerProperty id;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty productCode;
    private final StringProperty branchCode;
    private final DoubleProperty quantity;
    private final DoubleProperty returnQuantity;

    public ProductStock() {
        this.id = new SimpleIntegerProperty();
        this.date = new SimpleObjectProperty<>();
        this.productCode = new SimpleStringProperty();
        this.branchCode = new SimpleStringProperty();
        this.quantity = new SimpleDoubleProperty();
        this.returnQuantity = new SimpleDoubleProperty();
    }

    public ProductStock(int id, LocalDate date, String productCode, String branchCode, double quantity, double returnQuantity) {
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleObjectProperty<>(date);
        this.productCode = new SimpleStringProperty(productCode);
        this.branchCode = new SimpleStringProperty(branchCode);
        this.quantity = new SimpleDoubleProperty(quantity);
        this.returnQuantity = new SimpleDoubleProperty(returnQuantity);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
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

    public String getBranchCode() {
        return branchCode.get();
    }

    public void setBranchCode(String branchCode) {
        this.branchCode.set(branchCode);
    }

    public StringProperty branchCodeProperty() {
        return branchCode;
    }

    public double getQuantity() {
        return quantity.get();
    }

    public void setQuantity(double quantity) {
        this.quantity.set(quantity);
    }

    public DoubleProperty quantityProperty() {
        return quantity;
    }

    public double getReturnQuantity() {
        return returnQuantity.get();
    }

    public void setReturnQuantity(double returnQuantity) {
        this.returnQuantity.set(returnQuantity);
    }

    public DoubleProperty returnQuantityProperty() {
        return returnQuantity;
    }

}
