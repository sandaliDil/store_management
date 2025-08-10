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

    @FXML private TableView<ProductRow> productTable2;
    @FXML private TableColumn<ProductRow, String> nameColumn2;
    @FXML private TableColumn<ProductRow, Double> quantityColumn2;

    @FXML private TableView<ProductRow> productTable3;
    @FXML private TableColumn<ProductRow, String> nameColumn3;
    @FXML private TableColumn<ProductRow, Double> quantityColumn3;

    @FXML private TableView<ProductRow> productTable4;
    @FXML private TableColumn<ProductRow, String> nameColumn4;
    @FXML private TableColumn<ProductRow, Double> quantityColumn4;

    private final ProductRepository productRepository = new ProductRepository();
    private final StoreService storeService = new StoreService();
    private ObservableList<Branch> branchList;

    @FXML
    public void initialize() {
        // Load branches into ComboBox
        branchList = FXCollections.observableArrayList(storeService.getAllBranches());
        branchComboBox.setItems(branchList);

        // Display branch names properly in ComboBox cells and button
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

        // Setup the 4 tables
        setupProductTable(productTable1, nameColumn1, quantityColumn1);
        setupProductTable(productTable2, nameColumn2, quantityColumn2);
        setupProductTable(productTable3, nameColumn3, quantityColumn3);
        setupProductTable(productTable4, nameColumn4, quantityColumn4);

        // Load all products
        List<ProductRow> allProducts = productRepository.findAllProducts()
                .stream()
                .map(p -> new ProductRow(p.getProductId(), p.getName()))
                .collect(Collectors.toList());

        // Split products into 4 groups (max 10 each)
        ObservableList<ProductRow> list1 = FXCollections.observableArrayList(allProducts.subList(0, Math.min(10, allProducts.size())));
        ObservableList<ProductRow> list2 = FXCollections.observableArrayList(allProducts.size() > 10 ? allProducts.subList(10, Math.min(20, allProducts.size())) : List.of());
        ObservableList<ProductRow> list3 = FXCollections.observableArrayList(allProducts.size() > 20 ? allProducts.subList(20, Math.min(30, allProducts.size())) : List.of());
        ObservableList<ProductRow> list4 = FXCollections.observableArrayList(allProducts.size() > 30 ? allProducts.subList(30, Math.min(40, allProducts.size())) : List.of());

        productTable1.setItems(list1);
        productTable2.setItems(list2);
        productTable3.setItems(list3);
        productTable4.setItems(list4);
    }

    private void setupProductTable(TableView<ProductRow> table, TableColumn<ProductRow, String> nameCol, TableColumn<ProductRow, Double> qtyCol) {
        // Name column setup
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Quantity column setup with editable TextField cells
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        qtyCol.setCellFactory(col -> new TableCell<ProductRow, Double>() {
            private final TextField textField = new TextField();

            {
                textField.setMaxWidth(80);

                textField.focusedProperty().addListener((obs, oldFocused, newFocused) -> {
                    if (newFocused) {
                        if ("0.00".equals(textField.getText())) {
                            textField.clear();
                        }
                    } else {
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
                    int currentIndex = getIndex();
                    int nextIndex = currentIndex + 1;
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
                    if (text == null || text.equals(".") || text.isEmpty()) {
                        value = 0.0;
                    } else {
                        value = Double.parseDouble(text);
                    }
                } catch (NumberFormatException e) {
                    value = 0.0;
                }
                textField.setText(String.format("%.2f", value));
                ProductRow currentRow = getTableView().getItems().get(getIndex());
                currentRow.setQuantity(value);
            }

            @Override
            protected void updateItem(Double qty, boolean empty) {
                super.updateItem(qty, empty);
                if (empty || qty == null) {
                    setGraphic(null);
                } else {
                    textField.setText(String.format("%.2f", qty));
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
                    ps.setBranchId(selectedBranch.getBranchId());
                    ps.setProductId(pr.getProductId());
                    ps.setQuantity(pr.getQuantity());
                    return ps;
                })
                .toList();

        try {
            storeService.saveProductStocks(productStockList);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Product stock data saved successfully.");
            alert.showAndWait();
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

}