package com.dgut.salesmanagementsystem.model;

import com.dgut.salesmanagementsystem.pojo.Customer;
import com.dgut.salesmanagementsystem.pojo.Salesman;
import com.dgut.salesmanagementsystem.pojo.ContactInfo;
import com.dgut.salesmanagementsystem.pojo.PerformanceStats;
import com.dgut.salesmanagementsystem.tool.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class SalesmanDAO {
    public void addSalesman(Salesman salesman) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO Salesman (name, contact_info, total_sales, commission) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            params.add(salesman.getName());
            params.add(salesman.getContactInfoJson());
            params.add(salesman.getTotalSales());
            params.add(salesman.getCommission());

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, null);
        }
    }

    public void updateSalesman(Salesman salesman) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE Salesman SET name = ?, contact_info = ?, total_sales = ?, commission = ? WHERE salesman_id = ?";
            preparedStatement = connection.prepareStatement(sql);

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            params.add(salesman.getName());
            params.add(salesman.getContactInfoJson());
            params.add(salesman.getTotalSales());
            params.add(salesman.getCommission());
            params.add(salesman.getSalesmanID());

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, null);
        }
    }

    public Salesman getSalesmanById(int salesmanId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Salesman salesman = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM Salesman WHERE salesman_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, salesmanId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                salesman = mapResultSetToSalesman(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return salesman;
    }

    public List<Salesman> getAllSalesmen() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Salesman> salesmen = new ArrayList<>();
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM Salesman";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                salesmen.add(mapResultSetToSalesman(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return salesmen;
    }

    public void deleteSalesman(int salesmanId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM Salesman WHERE salesman_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, salesmanId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, null);
        }
    }

    private Salesman mapResultSetToSalesman(ResultSet resultSet) throws Exception {
        Salesman salesman = new Salesman();
        salesman.setSalesmanID(resultSet.getInt("salesman_id"));
        salesman.setName(resultSet.getString("name"));
        salesman.setContactInfoJson(resultSet.getString("contact_info"));
        salesman.setPerformanceStatsJson(resultSet.getString("performance_stats"));
        salesman.setTotalSales(resultSet.getBigDecimal("total_sales"));
        salesman.setCommission(resultSet.getBigDecimal("commission"));
        return salesman;
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

    public List<Salesman> searchSalesmen(String searchKeyword, int pageNum, int pageSize) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Salesman> ret = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            // 构建 SQL 查询语句
            String sql = "SELECT * FROM Salesman WHERE name LIKE ? " +
                    "OR salesman_id = ?" +
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
                Salesman salesman = mapResultSetToSalesman(resultSet);
                ret.add(salesman);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return ret;
    }


    public int countSalesmen(String searchKeyword) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int totalRecords = 0;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM Salesman WHERE name LIKE ? OR salesman_id = ?";
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
}
