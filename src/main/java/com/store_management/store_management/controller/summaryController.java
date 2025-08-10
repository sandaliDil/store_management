package com.store_management.store_management.controller;

import com.store_management.store_management.entity.MonthlySummary;
import com.store_management.store_management.service.StoreService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class summaryController {
    @FXML
    private DatePicker monthPicker;

    @FXML
    private TableView<MonthlySummaryView> summaryTable;

    @FXML
    private TableColumn<MonthlySummaryView, String> branchNameColumn;

    @FXML
    private TableColumn<MonthlySummaryView, String> productNameColumn;

    @FXML
    private TableColumn<MonthlySummaryView, Double> totalQuantityColumn;

    private final StoreService storeService = new StoreService();

    @FXML
    public void initialize() {
        branchNameColumn.setCellValueFactory(new PropertyValueFactory<>("branchName"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        totalQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));

        monthPicker.setPromptText("yyyy-MM");
        monthPicker.setConverter(new javafx.util.StringConverter<>() {
            private final String pattern = "yyyy-MM";
            private final java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) return formatter.format(date);
                else return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string + "-01", java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } else {
                    return null;
                }
            }
        });
    }

    @FXML
    public void loadMonthlySummary() {
        LocalDate selectedDate = monthPicker.getValue();
        if (selectedDate == null) {
            showAlert("Please select a month.");
            return;
        }

        int year = selectedDate.getYear();
        int month = selectedDate.getMonthValue();

        try {
            List<MonthlySummary> summaries = storeService.getMonthlySummary(year, month);

            List<MonthlySummaryView> viewList = summaries.stream()
                    .map(summary -> {
                        MonthlySummaryView view = new MonthlySummaryView();
                        view.setBranchName(storeService.getBranchNameById(summary.getBranchId()));
                        view.setProductName(storeService.getProductNameById(summary.getProductId()));
                        view.setTotalQuantity(summary.getTotalQuantity());
                        return view;
                    })
                    .collect(Collectors.toList());

            summaryTable.setItems(FXCollections.observableArrayList(viewList));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error loading summary: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class MonthlySummaryView {
        private String branchName;
        private String productName;
        private Double totalQuantity;

        public String getBranchName() { return branchName; }
        public void setBranchName(String branchName) { this.branchName = branchName; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public Double getTotalQuantity() { return totalQuantity; }
        public void setTotalQuantity(Double totalQuantity) { this.totalQuantity = totalQuantity; }
    }
}
