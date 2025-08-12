package com.store_management.store_management.controller;

import com.store_management.store_management.entity.Branch;
import com.store_management.store_management.entity.ProductRow;
import com.store_management.store_management.entity.ProductStock;
import com.store_management.store_management.repository.ProductRepository;
import com.store_management.store_management.service.StoreService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StoreController {

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Branch> branchComboBox;

    // Four tables and their columns
    @FXML private TableView<ProductRow> productTable1;
    @FXML private TableColumn<ProductRow, String> nameColumn1;
    @FXML private TableColumn<ProductRow, Double> quantityColumn1;
    @FXML private TableColumn<ProductRow, Double> returnQuantityColumn1;

    @FXML private TableView<ProductRow> productTable2;
    @FXML private TableColumn<ProductRow, String> nameColumn2;
    @FXML private TableColumn<ProductRow, Double> quantityColumn2;
    @FXML private TableColumn<ProductRow, Double> returnQuantityColumn2;

    @FXML private TableView<ProductRow> productTable3;
    @FXML private TableColumn<ProductRow, String> nameColumn3;
    @FXML private TableColumn<ProductRow, Double> quantityColumn3;
    @FXML private TableColumn<ProductRow, Double> returnQuantityColumn3;

    @FXML private TableView<ProductRow> productTable4;
    @FXML private TableColumn<ProductRow, String> nameColumn4;
    @FXML private TableColumn<ProductRow, Double> quantityColumn4;
    @FXML private TableColumn<ProductRow, Double> returnQuantityColumn4;

    private final ProductRepository productRepository = new ProductRepository();
    private final StoreService storeService = new StoreService();
    private ObservableList<Branch> branchList;

    @FXML
    public void initialize() {
        // Load branches
        branchList = FXCollections.observableArrayList(storeService.getAllBranches());
        branchComboBox.setItems(branchList);

        // ComboBox display
        branchComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Branch branch, boolean empty) {
                super.updateItem(branch, empty);
                setText(empty || branch == null ? "" : branch.getBranchName());
            }
        });
        branchComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Branch branch, boolean empty) {
                super.updateItem(branch, empty);
                setText(empty || branch == null ? "" : branch.getBranchName());
            }
        });

        // Setup 4 product tables (quantity columns)
        setupProductTable(productTable1, nameColumn1, quantityColumn1);
        setupProductTable(productTable2, nameColumn2, quantityColumn2);
        setupProductTable(productTable3, nameColumn3, quantityColumn3);
        setupProductTable(productTable4, nameColumn4, quantityColumn4);

        // Setup 4 return quantity columns
        setupReturnQuantityColumn(returnQuantityColumn1);
        setupReturnQuantityColumn(returnQuantityColumn2);
        setupReturnQuantityColumn(returnQuantityColumn3);
        setupReturnQuantityColumn(returnQuantityColumn4);

        // Load products and split into 4 lists
        List<ProductRow> allProducts = productRepository.findAllProducts()
                .stream()
                .map(p -> new ProductRow(p.getProductCode(), p.getProductName()))
                .collect(Collectors.toList());

        productTable1.setItems(FXCollections.observableArrayList(allProducts.subList(0, Math.min(10, allProducts.size()))));
        productTable2.setItems(FXCollections.observableArrayList(allProducts.size() > 10 ? allProducts.subList(10, Math.min(20, allProducts.size())) : List.of()));
        productTable3.setItems(FXCollections.observableArrayList(allProducts.size() > 20 ? allProducts.subList(20, Math.min(30, allProducts.size())) : List.of()));
        productTable4.setItems(FXCollections.observableArrayList(allProducts.size() > 30 ? allProducts.subList(30, Math.min(40, allProducts.size())) : List.of()));
    }

    private void setupProductTable(TableView<ProductRow> table, TableColumn<ProductRow, String> nameCol, TableColumn<ProductRow, Double> qtyCol) {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        qtyCol.setCellFactory(col -> new TableCell<>() {
            private final TextField textField = new TextField();

            {
                textField.setMaxWidth(80);

                textField.focusedProperty().addListener((obs, oldFocused, newFocused) -> {
                    if (newFocused && "0.000".equals(textField.getText())) {
                        textField.clear();
                    } else if (!newFocused) {
                        saveAndFormat();
                    }
                });

                textField.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal == null || newVal.isEmpty()) return;
                    if (!newVal.matches("\\d*\\.?\\d*")) {
                        textField.setText(oldVal);
                    }
                });

                textField.setOnAction(event -> {
                    saveAndFormat();
                    int nextIndex = getIndex() + 1;
                    if (nextIndex < getTableView().getItems().size()) {
                        getTableView().edit(nextIndex, qtyCol);
                        getTableView().getSelectionModel().select(nextIndex);
                    }
                });
            }

            private void saveAndFormat() {
                String text = textField.getText();
                double value;
                try {
                    value = (text == null || text.equals(".") || text.isEmpty()) ? 0.0 : Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    value = 0.0;
                }
                textField.setText(String.format("%.3f", value));
                getTableView().getItems().get(getIndex()).setQuantity(value);
            }

            @Override
            protected void updateItem(Double qty, boolean empty) {
                super.updateItem(qty, empty);
                if (empty || qty == null) {
                    setGraphic(null);
                } else {
                    textField.setText(String.format("%.3f", qty));
                    setGraphic(textField);
                }
            }
        });
    }

    private void setupReturnQuantityColumn(TableColumn<ProductRow, Double> returnQtyCol) {
        returnQtyCol.setCellValueFactory(new PropertyValueFactory<>("returnQuantity"));

        returnQtyCol.setCellFactory(col -> new TableCell<>() {
            private final TextField textField = new TextField();

            {
                textField.setMaxWidth(80);

                textField.focusedProperty().addListener((obs, oldFocused, newFocused) -> {
                    if (newFocused && "0.000".equals(textField.getText())) {
                        textField.clear();
                    } else if (!newFocused) {
                        saveAndFormat();
                    }
                });

                textField.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal == null || newVal.isEmpty()) return;
                    if (!newVal.matches("\\d*\\.?\\d*")) {
                        textField.setText(oldVal);
                    }
                });

                textField.setOnAction(event -> {
                    saveAndFormat();
                    int nextIndex = getIndex() + 1;
                    if (nextIndex < getTableView().getItems().size()) {
                        getTableView().edit(nextIndex, returnQtyCol);
                        getTableView().getSelectionModel().select(nextIndex);
                    }
                });
            }

            private void saveAndFormat() {
                String text = textField.getText();
                double value;
                try {
                    value = (text == null || text.equals(".") || text.isEmpty()) ? 0.0 : Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    value = 0.0;
                }
                textField.setText(String.format("%.3f", value));
                getTableView().getItems().get(getIndex()).setReturnQuantity(value);
            }

            @Override
            protected void updateItem(Double qty, boolean empty) {
                super.updateItem(qty, empty);
                if (empty || qty == null) {
                    setGraphic(null);
                } else {
                    textField.setText(String.format("%.3f", qty));
                    setGraphic(textField);
                }
            }
        });
    }

    @FXML
    public void saveProductStocks() {
        LocalDate selectedDate = datePicker.getValue();
        Branch selectedBranch = branchComboBox.getValue();

        if (selectedDate == null) {
            showAlert("Please select a date.");
            return;
        }
        if (selectedBranch == null) {
            showAlert("Please select a branch.");
            return;
        }

        List<ProductRow> allProductRows = new ArrayList<>();
        allProductRows.addAll(productTable1.getItems());
        allProductRows.addAll(productTable2.getItems());
        allProductRows.addAll(productTable3.getItems());
        allProductRows.addAll(productTable4.getItems());

        if (allProductRows.isEmpty()) {
            showAlert("No products to save.");
            return;
        }

        var productStockList = allProductRows.stream()
                .map(pr -> {
                    ProductStock ps = new ProductStock();
                    ps.setDate(selectedDate);
                    ps.setBranchCode(selectedBranch.getBranchCode());
                    ps.setProductCode(pr.getProductCode());
                    ps.setQuantity(pr.getQuantity());
                    ps.setReturnQuantity(pr.getReturnQuantity());
                    return ps;
                })
                .toList();

        try {
            storeService.saveProductStocks(productStockList);
            showInfo("Product stock data saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to save product stock data: " + e.getMessage());
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
