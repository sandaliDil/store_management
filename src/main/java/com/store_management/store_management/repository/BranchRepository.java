package com.store_management.store_management.repository;

import com.store_management.store_management.database.DatabaseConnection;
import com.store_management.store_management.entity.Branch;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchRepository {
    public List<Branch> findAllBranches() {
        List<Branch> branches = new ArrayList<>();
        String sql = "SELECT * FROM branch";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Branch branch = new Branch();
                branch.setBranchCode(rs.getString("branch_code"));
                branch.setBranchName(rs.getString("branch_name"));
                branches.add(branch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return branches;
    }

    public String findBranchNameById(int branchId) {
        String sql = "SELECT branch_name FROM branch WHERE branch_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, branchId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("branch_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown Branch";
    }
}
