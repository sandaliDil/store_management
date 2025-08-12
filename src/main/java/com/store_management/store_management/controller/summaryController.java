package com.store_management.store_management.controller;

import com.store_management.store_management.entity.Branch;
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
    private javafx.scene.control.ComboBox<Branch> branchComboBox;

    @FXML
    private TableColumn<MonthlySummaryView, String> productNameColumn;

    @FXML
    private TableColumn<MonthlySummaryView, Double> totalQuantityColumn;

    private final StoreService storeService = new StoreService();

    @FXML
    public void initialize() {
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
        List<Branch> branches = storeService.getAllBranches();
        branchComboBox.setItems(FXCollections.observableArrayList(branches));

        branchComboBox.setPromptText("Select Branch");

        branchComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Branch branch) {
                return branch == null ? "" : branch.getBranchName();
            }
            @Override
            public Branch fromString(String string) {
                return null; // No editing support needed
            }
        });
    }

    @FXML
    public void loadMonthlySummary() {
        LocalDate selectedDate = monthPicker.getValue();
        Branch selectedBranch = branchComboBox.getValue();

        if (selectedDate == null) {
            showAlert("Please select a month.");
            return;
        }
        if (selectedBranch == null) {
            showAlert("Please select a branch.");
            return;
        }

        int year = selectedDate.getYear();
        int month = selectedDate.getMonthValue();
        int branchId = selectedBranch.getBranchId();

        try {
            // Pass branchId to get summaries only for that branch
            List<MonthlySummary> summaries = storeService.getMonthlySummaryByBranch(branchId, year, month);

            List<MonthlySummaryView> viewList = summaries.stream()
                    .map(summary -> {
                        MonthlySummaryView view = new MonthlySummaryView();
                        view.setBranchName(selectedBranch.getBranchName());  // branch already known
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

    public String generateMonthlySummaryHtml(String branchName, String monthYear, List<MonthlySummaryView> data) {
        StringBuilder html = new StringBuilder();

        java.util.function.Function<Double, String> formatQuantity = q -> {
            if (q == null) return "";
            if (q == q.longValue()) {
                return String.valueOf(q.longValue()); // No decimal if whole number
            } else {
                return String.valueOf(q); // Keep decimal
            }
        };

        html.append("""
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Monthly Summary Report</title>
        <style>
            body {
                font-family: Arial, sans-serif;
            }
            table {
                width: 30%;
                border-collapse: collapse;
                margin-top: 15px;
                font-size: 8px;
            }
            h3, h5 {
                margin: 2px 0;
            }
            th, td {
                border: 1px solid #333;
                padding: 1px 2px;
                text-align: center;
                font-size: 10px; /* default font size for table cells */
            }
            th {
                background-color: #007bff;
                color: white;
            }
            tfoot td {
                font-weight: bold;
            }
            td:first-child {
                            text-align: left;
                            padding-left: 25px;
                            font-size: 8px; /* optional smaller size */
                            width: 50px;
                        }
            th:nth-child(2), td:nth-child(2) {
                                        width: 20px;  /* Adjust as needed */
                                        text-align: center;
                                        font-size: 12px;
            }
            @media print {
                body {
                    margin: 0;
                }
                table {
                    page-break-after: auto;
                }
                tr {
                    page-break-inside: avoid;
                    page-break-after: auto;
                }
                td, th {
                    page-break-inside: avoid;
                    page-break-after: auto;
                }
            }
        </style>
    </head>
    <body>
    """);

        html.append("<h3>Monthly Summary Report</h3>");
        html.append("<h5>Branch: ").append(branchName).append("</h5>");
        html.append("<h5>Month: ").append(monthYear).append("</h5>");

        html.append("""
    <table>
        <thead>
            <tr>
                <th>Product Name</th>
                <th>Total Quantity</th>
            </tr>
        </thead>
        <tbody>
    """);

        double totalQuantitySum = 0;

        for (MonthlySummaryView item : data) {
            html.append("<tr>");
            html.append("<td>").append(item.getProductName()).append("</td>");
            html.append("<td>").append(formatQuantity.apply(item.getTotalQuantity())).append("</td>");
            html.append("</tr>");
            totalQuantitySum += item.getTotalQuantity();
        }

        html.append("</tbody>");

        html.append("<tfoot>");
        html.append("<tr>");
        html.append("<td>Total</td>");
        html.append("<td>").append(formatQuantity.apply(totalQuantitySum)).append("</td>");
        html.append("</tr>");
        html.append("</tfoot>");

        html.append("</table>");

        html.append("</body></html>");

        return html.toString();
    }

    @FXML
    public void printMonthlySummary() {
        LocalDate selectedDate = monthPicker.getValue();
        Branch selectedBranch = branchComboBox.getValue();

        if (selectedDate == null) {
            showAlert("Please select a month.");
            return;
        }
        if (selectedBranch == null) {
            showAlert("Please select a branch.");
            return;
        }

        int year = selectedDate.getYear();
        int month = selectedDate.getMonthValue();
        int branchId = selectedBranch.getBranchId();

        try {
            List<MonthlySummary> summaries = storeService.getMonthlySummaryByBranch(branchId, year, month);

            List<MonthlySummaryView> viewList = summaries.stream()
                    .map(summary -> {
                        MonthlySummaryView view = new MonthlySummaryView();
                        view.setBranchName(selectedBranch.getBranchName());
                        view.setProductName(storeService.getProductNameById(summary.getProductId()));
                        view.setTotalQuantity(summary.getTotalQuantity());
                        return view;
                    })
                    .collect(Collectors.toList());

            String monthYear = String.format("%04d-%02d", year, month);
            String htmlContent = generateMonthlySummaryHtml(selectedBranch.getBranchName(), monthYear, viewList);

            // Load HTML in WebView
            javafx.scene.web.WebView webView = new javafx.scene.web.WebView();
            webView.getEngine().loadContent(htmlContent);

            // Wait for content to load before printing
            webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                    javafx.print.PrinterJob job = javafx.print.PrinterJob.createPrinterJob();
                    if (job != null && job.showPrintDialog(summaryTable.getScene().getWindow())) {
                        boolean success = job.printPage(webView); // ✅ FIX: printPage instead of print()
                        if (success) {
                            job.endJob();
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error printing summary: " + e.getMessage());
        }
    }

}
