package com.dgut.salesmanagementsystem.model;

import com.dgut.salesmanagementsystem.pojo.Product;
import com.dgut.salesmanagementsystem.tool.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public int countProducts(String searchKeyword) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int totalRecords = 0;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM Product WHERE product_name LIKE ? OR product_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + searchKeyword + "%");
            preparedStatement.setString(2, searchKeyword);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalRecords = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return totalRecords;
    }

    // 根据searchKeyword获取某个页码的商品集合
    public List<Product> searchProducts(String searchKeyword, int pageNum, int pageSize) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Product> ret = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            // 构建 SQL 查询语句
            String sql = "SELECT * FROM Product WHERE product_name LIKE ? " +
                    "OR product_id = ?" +
                    " LIMIT ? OFFSET ?";
            preparedStatement = connection.prepareStatement(sql);

            params.add("%" + searchKeyword + "%"); // 模糊查询
            params.add(searchKeyword);
            params.add(pageSize);  // 每页显示条数
            params.add((pageNum - 1) * pageSize); // 偏移量

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Product product = mapResultSetToProduct(resultSet);
                ret.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return ret;
    }

    public int getStockQuantityByID(int productID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int ret = 0;
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            // 构建 SQL 查询语句
            String sql = "SELECT stock_quantity FROM Product WHERE product_id = ?";
            preparedStatement = connection.prepareStatement(sql);

            params.add(productID);

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ret = resultSet.getInt("stock_quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return ret;
    }

    private void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Product mapResultSetToProduct(ResultSet resultSet) throws Exception {
        Product product = new Product();
        product.setProductID(resultSet.getInt("product_id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setStockQuantity(resultSet.getInt("stock_quantity"));
        product.setLowStockThreshold(resultSet.getInt("low_stock_threshold"));

        return product;
    }

    public Product getProductByID(int productID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Product product = null;
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            // 构建 SQL 查询语句
            String sql = "SELECT * FROM Product WHERE product_id = ?";
            preparedStatement = connection.prepareStatement(sql);

            params.add(productID);

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                product = mapResultSetToProduct(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return product;
    }

    public boolean updateStock(Integer productID, int newStock) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            // 构建 SQL 查询语句
            String sql = "UPDATE Product SET stock_quantity = ? WHERE product_id = ?";
            preparedStatement = connection.prepareStatement(sql);

            params.add(newStock);
            params.add(productID);

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, null);
        }
        return false;
    }
}
