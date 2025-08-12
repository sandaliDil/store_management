package com.store_management.store_management.entity;

import javafx.beans.property.*;

public class Branch {
    private final IntegerProperty branchId;
    private final StringProperty branchName;
    private final StringProperty branchCode;

    public Branch() {
        this.branchId = new SimpleIntegerProperty();
        this.branchName = new SimpleStringProperty();
        this.branchCode = new SimpleStringProperty();
    }

    public Branch(int branchId, String branchName, String branchCode) {
        this.branchId = new SimpleIntegerProperty(branchId);
        this.branchName = new SimpleStringProperty(branchName);
        this.branchCode = new SimpleStringProperty(branchCode);
    }

    public int getBranchId() {
        return branchId.get();
    }

    public void setBranchId(int branchId) {
        this.branchId.set(branchId);
    }

    public IntegerProperty branchIdProperty() {
        return branchId;
    }

    public String getBranchName() {
        return branchName.get();
    }

    public void setBranchName(String branchName) {
        this.branchName.set(branchName);
    }

    public StringProperty branchNameProperty() {
        return branchName;
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
}
