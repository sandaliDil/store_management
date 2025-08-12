package com.store_management.store_management.repository;

import com.store_management.store_management.database.DatabaseConnection;
import com.store_management.store_management.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    public List<Product> findAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = new Product();
                product.setProductCode(rs.getString("product_code"));
                product.setProductName(rs.getString("product_name"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public String findProductNameById(int productId) {
        String sql = "SELECT product_name FROM product WHERE product_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("product_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown Product";
    }
}
