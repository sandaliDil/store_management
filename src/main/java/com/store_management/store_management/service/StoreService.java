package com.store_management.store_management.service;

import com.store_management.store_management.entity.MonthlySummary;
import com.store_management.store_management.entity.Branch;
import com.store_management.store_management.entity.Product;
import com.store_management.store_management.entity.ProductStock;
import com.store_management.store_management.repository.BranchRepository;
import com.store_management.store_management.repository.ProductRepository;
import com.store_management.store_management.repository.ProductStockRepository;

import java.sql.SQLException;
import java.util.List;

public class StoreService {
    private final BranchRepository branchRepo = new BranchRepository();
    private final ProductRepository productRepo = new ProductRepository();
    private final ProductStockRepository stockRepo = new ProductStockRepository();

    public List<Branch> getAllBranches() {
        return branchRepo.findAllBranches();
    }

    public List<Product> getAllProducts() {
        return productRepo.findAllProducts();
    }

    private final ProductStockRepository productStockRepository = new ProductStockRepository();

    // Save product stocks through repository
    public void saveProductStocks(List<ProductStock> stocks) throws SQLException {
        if (stocks == null || stocks.isEmpty()) {
            throw new IllegalArgumentException("Product stock list cannot be null or empty");
        }

        productStockRepository.saveAll(stocks);
    }

    public List<MonthlySummary> getMonthlySummary(int year, int month) throws SQLException {
        return ProductStockRepository.getMonthlySummary(year, month);
    }
    public String getBranchNameById(int branchId) {
        return branchRepo.findBranchNameById(branchId);
    }

    public String getProductNameById(int productId) {
        return productRepo.findProductNameById(productId);
    }

}
