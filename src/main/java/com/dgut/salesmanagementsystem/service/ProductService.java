package com.dgut.salesmanagementsystem.service;

import com.dgut.salesmanagementsystem.model.ProductDAO;
import com.dgut.salesmanagementsystem.pojo.PaginatedResult;
import com.dgut.salesmanagementsystem.pojo.Product;
import com.dgut.salesmanagementsystem.pojo.Salesman;

import java.util.List;

public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();
    public List<Product> searchProducts(String searchKeyword, int pageNum, int pageSize) {
        List<Product> productList = productDAO.searchProducts(searchKeyword, pageNum, pageSize);

        return productList;
    }

    public PaginatedResult<Product> getProductsByPage(String searchKeyword, int curPage, int pageSize, List<Product> productList) {
        int totalRecords = productDAO.countProducts(searchKeyword);
        // 算出总共有几页
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        totalPages = totalPages > 0 ? totalPages : 1;
        // 设置分页相关属性
        PaginatedResult<Product> result = new PaginatedResult<>();
        result.setElementList(productList);
        result.setCurrentPage(curPage);
        result.setTotalPages(totalPages);
        result.setTotalRecords(totalRecords);

        return result;
    }

    public Product getProductByID(int productID)
    {
        return productDAO.getProductByID(productID);
    }

    public boolean reduceStock(Integer productID, int quantity) {
        Product product = productDAO.getProductByID(productID);
        if (product == null) {
            throw new IllegalArgumentException("商品不存在");
        }

        int currentStock = product.getStockQuantity();
        if (currentStock < quantity) {
            return false; // 库存不足
        }
        // 更新库存
        int newStock = currentStock - quantity;
        return productDAO.updateStock(productID, newStock);
    }
}
