package com.store_management.store_management.repository;

import com.store_management.store_management.database.DatabaseConnection;
import com.store_management.store_management.entity.MonthlySummary;
import com.store_management.store_management.entity.ProductStock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductStockRepository {


    public void saveAll(List<ProductStock> productStocks) throws SQLException {
        String sql = """
            INSERT INTO productStock (date, branch_code, product_code, quantity, return_quantity)
            VALUES (?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                quantity = VALUES(quantity),
                return_quantity = VALUES(return_quantity)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (ProductStock ps : productStocks) {
                stmt.setDate(1, java.sql.Date.valueOf(ps.getDate()));
                stmt.setString(2, ps.getBranchCode());
                stmt.setString(3, ps.getProductCode());
                stmt.setDouble(4, ps.getQuantity());
                stmt.setDouble(5, ps.getReturnQuantity());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

//    public static List<MonthlySummary> getMonthlySummary(int year, int month) throws SQLException {
//        String sql = """
//            SELECT branch_id, product_id, SUM(quantity) AS total_quantity
//            FROM product_stock
//            WHERE YEAR(date) = ? AND MONTH(date) = ?
//            GROUP BY branch_id, product_id
//            ORDER BY branch_id, product_id
//            """;
//
//        List<MonthlySummary> summaries = new ArrayList<>();
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setInt(1, year);
//            stmt.setInt(2, month);
//
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                MonthlySummary summary = new MonthlySummary();
//                summary.setBranchId(rs.getInt("branch_id"));
//                summary.setProductId(rs.getInt("product_id"));
//                summary.setTotalQuantity(rs.getDouble("total_quantity"));
//                summaries.add(summary);
//            }
//        }
//
//        return summaries;
//    }

    public List<MonthlySummary> findMonthlySummaryByBranch(int branchId, int year, int month) {
        List<MonthlySummary> summaries = new ArrayList<>();

        String sql = "SELECT branch_id, product_id, SUM(quantity) AS total_quantity " +
                "FROM productStock " +
                "WHERE branch_id = ? " +
                "AND YEAR(date) = ? " +
                "AND MONTH(date) = ? " +
                "GROUP BY branch_id, product_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, branchId);
            stmt.setInt(2, year);
            stmt.setInt(3, month);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MonthlySummary summary = new MonthlySummary();
                    summary.setBranchId(rs.getInt("branch_id"));
                    summary.setProductId(rs.getInt("product_id"));
                    summary.setTotalQuantity(rs.getDouble("total_quantity"));
                    summaries.add(summary);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return summaries;
    }
}
