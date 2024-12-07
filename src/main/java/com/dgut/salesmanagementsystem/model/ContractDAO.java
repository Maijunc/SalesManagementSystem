package com.dgut.salesmanagementsystem.model;

import com.dgut.salesmanagementsystem.pojo.*;
import com.dgut.salesmanagementsystem.tool.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractDAO {
    public List<Contract> searchContracts(ContractSearchCriteria contractSearchCriteria, int pageNum, int pageSize) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Contract> ret = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 基本的查询语句，不包含条件部分
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Contract WHERE 1=1");

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            // 如果 contractName 不为空，则添加相关条件
            if (contractSearchCriteria.getContractName() != null && !contractSearchCriteria.getContractName().isEmpty()) {
                sqlBuilder.append(" AND contract_name LIKE ?");
                params.add("%" + contractSearchCriteria.getContractName() + "%");
            }

            // 如果 contractID 不为空，则添加相关条件
            if (contractSearchCriteria.getContractID() != null) {
                sqlBuilder.append(" AND contract_id = ?");
                params.add(contractSearchCriteria.getContractID());
            }

            // 如果 status 不为空，则添加相关条件
            if (contractSearchCriteria.getStatus() != null && !contractSearchCriteria.getStatus().isEmpty()) {
                sqlBuilder.append(" AND contract_status = ?");
                params.add(contractSearchCriteria.getStatus());
            }

            // 如果 startDateStr 不为空，则添加相关条件
            if (contractSearchCriteria.getStartDateStr() != null && !contractSearchCriteria.getStartDateStr().isEmpty() && contractSearchCriteria.getEndDateStr() != null && !contractSearchCriteria.getEndDateStr().isEmpty()) {
                sqlBuilder.append(" AND start_date BETWEEN ? AND ?");
                params.add(contractSearchCriteria.getStartDateStr());
                params.add(contractSearchCriteria.getEndDateStr());
            }

            // 添加分页条件
            sqlBuilder.append(" LIMIT ? OFFSET ?");
            params.add(pageSize);
            params.add((pageNum - 1) * pageSize);

            // 构建最终的 SQL 查询语句
            String sql = sqlBuilder.toString();

            preparedStatement = connection.prepareStatement(sql);

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Contract contract = new Contract();
                contract.setContractID(resultSet.getInt("contract_id"));
                contract.setContractName(resultSet.getString("contract_name"));
                contract.setContractDate(resultSet.getDate("contract_date"));
                contract.setStartDate(resultSet.getDate("start_date"));
                contract.setEndDate(resultSet.getDate("end_date"));
                contract.setContractStatus(ContractStatus.fromString(resultSet.getString("contract_status")));
                contract.setTotalAmount(resultSet.getBigDecimal("total_amount"));
                contract.setPaidAmount(resultSet.getBigDecimal("paid_amount"));
                contract.setRemainingAmount(resultSet.getBigDecimal("remaining_amount"));
                contract.setSalesmanID(resultSet.getInt("salesman_id"));
                contract.setCustomerID(resultSet.getInt("customer_id"));
                ret.add(contract);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return ret;
    }

    public int countContracts(ContractSearchCriteria contractSearchCriteria) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int totalRecords = 0;
        try {
            connection = DatabaseConnection.getConnection();
//            String sql = "SELECT COUNT(*) FROM Contract WHERE (contract_name LIKE ? " +
//                    "OR contract_id = ? OR contract_status = ?) AND start_date BETWEEN ? AND ?";
            // 基本的查询语句，不包含条件部分
            StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(*) FROM Contract WHERE 1=1");

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            // 如果 contractName 不为空，则添加相关条件
            if (contractSearchCriteria.getContractName() != null && !contractSearchCriteria.getContractName().isEmpty()) {
                sqlBuilder.append(" AND contract_name LIKE ?");
                params.add("%" + contractSearchCriteria.getContractName() + "%");
            }

            // 如果 contractID 不为空，则添加相关条件
            if (contractSearchCriteria.getContractID() != null) {
                sqlBuilder.append(" AND contract_id = ?");
                params.add(contractSearchCriteria.getContractID());
            }

            // 如果 status 不为空，则添加相关条件
            if (contractSearchCriteria.getStatus() != null && !contractSearchCriteria.getStatus().isEmpty()) {
                sqlBuilder.append(" AND contract_status = ?");
                params.add(contractSearchCriteria.getStatus());
            }

            // 如果 startDateStr 和 endDateStr 不为空，则添加相关条件
            if (contractSearchCriteria.getStartDateStr() != null && !contractSearchCriteria.getStartDateStr().isEmpty() && contractSearchCriteria.getEndDateStr() != null && !contractSearchCriteria.getEndDateStr().isEmpty()) {
                sqlBuilder.append(" AND start_date BETWEEN ? AND ?");
                params.add(contractSearchCriteria.getStartDateStr());
                params.add(contractSearchCriteria.getEndDateStr());
            }

            // 构建最终的 SQL 查询语句
            String sql = sqlBuilder.toString();

            preparedStatement = connection.prepareStatement(sql);

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }
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

    public void addContract(ContractModifyCriteria criteria) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            // 1. 插入合同表
            String sql = "INSERT INTO Contract (contract_name, contract_date, start_date, end_date, contract_status, total_amount, paid_amount, remaining_amount, customer_id, salesman_id) " +
                    "VALUES (?, CURRENT_DATE, ?, ?, ?, ?, 0, ?, ?, ?)";

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // 设置参数值
            params.add(criteria.getContractName());
            params.add(criteria.getStartDateStr());
            params.add(criteria.getEndDateStr());
            params.add(criteria.getStatus());
            params.add(criteria.getTotalPrice());
            params.add(criteria.getTotalPrice());
            params.add(criteria.getCustomerID());
            params.add(criteria.getSalesmanID());
            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            // 执行插入
            int affectedRows = preparedStatement.executeUpdate();

            // 获取自动生成的合同ID
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int contractID = generatedKeys.getInt(1); // 获取自增的合同ID
                        // 2. 插入合同商品项
                        insertContractItems(contractID, criteria.getContractItemList());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, null);
        }
    }

    // 插入合同商品项
    private void insertContractItems(int contractID, List<ContractItem> contractItemList){

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            // 1. 插入合同表
            String sql = "INSERT INTO ContractItem (contract_id, product_id, product_name, quantity, unit_price) "
                    + "VALUES (?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(sql);

            for (ContractItem item : contractItemList) {
                preparedStatement.setInt(1, contractID); // 设置合同ID
                preparedStatement.setInt(2, item.getProductID()); // 设置商品ID
                preparedStatement.setString(3, item.getProductName()); // 设置商品名称
                preparedStatement.setInt(4, item.getQuantity()); // 设置商品数量
                preparedStatement.setBigDecimal(5, item.getUnitPrice()); // 设置商品单价

                // 执行插入操作
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch(); // 批量执行插入操作
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, null);
        }
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
}
