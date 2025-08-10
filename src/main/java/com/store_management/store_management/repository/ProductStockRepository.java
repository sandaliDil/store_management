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


    public void saveAll(List<ProductStock> stocks) throws SQLException {
        String insertSql = """
            INSERT INTO product_stock (date, product_id, branch_id, quantity)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE quantity = VALUES(quantity)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

            for (ProductStock stock : stocks) {
                pstmt.setDate(1, java.sql.Date.valueOf(stock.getDate()));
                pstmt.setInt(2, stock.getProductId());
                pstmt.setInt(3, stock.getBranchId());
                pstmt.setDouble(4, stock.getQuantity());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        }
    }

    public static List<MonthlySummary> getMonthlySummary(int year, int month) throws SQLException {
        String sql = """
            SELECT branch_id, product_id, SUM(quantity) AS total_quantity
            FROM product_stock
            WHERE YEAR(date) = ? AND MONTH(date) = ?
            GROUP BY branch_id, product_id
            ORDER BY branch_id, product_id
            """;

        List<MonthlySummary> summaries = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, year);
            stmt.setInt(2, month);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MonthlySummary summary = new MonthlySummary();
                summary.setBranchId(rs.getInt("branch_id"));
                summary.setProductId(rs.getInt("product_id"));
                summary.setTotalQuantity(rs.getDouble("total_quantity"));
                summaries.add(summary);
            }
        }

        return summaries;
    }

//    public List<MonthlySummary> findMonthlySummaryByBranch(int branchId, int year, int month) {
//        List<MonthlySummary> summaries = new ArrayList<>();
//        String sql = """
//        SELECT p.product_id, p.name AS product_name, SUM(ps.quantity) AS total_quantity
//        FROM product_stock ps
//        JOIN product p ON ps.product_id = p.product_id
//        WHERE ps.branch_id = ? AND YEAR(ps.date) = ? AND MONTH(ps.date) = ?
//        GROUP BY p.product_id, p.name
//        """;
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setInt(1, branchId);
//            stmt.setInt(2, year);
//            stmt.setInt(3, month);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    MonthlySummary summary = new MonthlySummary();
//                    summary.setProductId(rs.getInt("product_id"));
//                    summary.setProductName(rs.getString("product_name"));
//                    summary.setTotalQuantity(rs.getDouble("total_quantity"));
//                    summaries.add(summary);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return summaries;
//    }

}
