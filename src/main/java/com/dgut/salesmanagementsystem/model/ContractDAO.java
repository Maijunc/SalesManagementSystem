package com.dgut.salesmanagementsystem.model;

import com.dgut.salesmanagementsystem.pojo.*;
import com.dgut.salesmanagementsystem.tool.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
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
                Contract contract = mapResultSetToContract(resultSet);
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
            String sql = "INSERT INTO Contract (" +
                    " contract_name," +
//                    " contract_date," +
//                    " start_date," +
//                    " end_date," +
                    " contract_status," +
                    " total_amount," +
                    " paid_amount," +
                    " customer_id," +
                    " salesman_id) " +
                    "VALUES (?, ?, ?, 0, ?, ?)";

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // 设置参数值
            params.add(criteria.getContractName());
//            params.add(criteria.getStartDateStr());
//            params.add(criteria.getEndDateStr());
            params.add(ContractStatus.getInt(ContractStatus.NOT_STARTED.getValue()));
            params.add(criteria.getTotalPrice());
//            params.add(criteria.getTotalPrice());
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

    public void editContract(ContractModifyCriteria criteria) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false); // 开启事务

            // 1. 更新合同表
            String sql = "UPDATE Contract SET " +
                    "contract_name = ?, " +
                    "start_date = ?, " +
                    "end_date = ?, " +
                    "contract_status = ?, " +
                    "total_amount = ?, " +
                    "customer_id = ?, " +
                    "salesman_id = ? " +
                    "WHERE contract_id = ?";

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // 设置参数值
            params.add(criteria.getContractName());
            params.add(criteria.getStartDateStr());
            params.add(criteria.getEndDateStr());
            params.add(criteria.getStatus());
            params.add(criteria.getTotalPrice());
            params.add(criteria.getCustomerID());
            params.add(criteria.getSalesmanID());
            params.add(criteria.getContractID());
            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            // 执行插入
            preparedStatement.executeUpdate();

            // 2. 删除原有的 ContractItem 记录
            String deleteSql = "DELETE FROM ContractItem WHERE contract_id = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, criteria.getContractID());
                deleteStmt.executeUpdate();
            }

            // 3. 插入新的合同项
            String insertContractItemSql = "INSERT INTO ContractItem (contract_id, product_id, product_name, quantity, unit_price) " +
                    "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertContractItemSql)) {
                for (ContractItem item : criteria.getContractItemList()) {
                    insertStmt.setInt(1, criteria.getContractID());
                    insertStmt.setInt(2, item.getProductID());
                    insertStmt.setString(3, item.getProductName());
                    insertStmt.setInt(4, item.getQuantity());
                    insertStmt.setBigDecimal(5, item.getUnitPrice());
                    insertStmt.addBatch(); // 批量插入
                }
                insertStmt.executeBatch(); // 执行批量插入
            }

            // 提交事务
            connection.commit();

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

    public Contract getContractById(Integer contractID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Contract contract = null;
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM Contract WHERE contract_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, contractID);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                contract = mapResultSetToContract(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeResources(connection, preparedStatement, resultSet);
        }

        return contract;
    }

    public List<ContractItem> getContractItemListById(Integer contractID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<ContractItem> ret = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM ContractItem WHERE contract_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, contractID);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ContractItem contractItem = mapResultSetToContractItem(resultSet);
                ret.add(contractItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeResources(connection, preparedStatement, resultSet);
        }

        return ret;
    }

    public String getContractName(Integer contractID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String contractName = null;
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql = "SELECT contract_name FROM Contract WHERE contract_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, contractID);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                contractName = resultSet.getString("contract_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeResources(connection, preparedStatement, resultSet);
        }

        return contractName;
    }

    private ContractItem mapResultSetToContractItem(ResultSet resultSet) throws Exception {
        ContractItem contractItem = new ContractItem();

        // 从ResultSet中获取各列的值并设置到ContractItem对象中
        contractItem.setContractItemID(resultSet.getInt("contract_item_id"));
        contractItem.setContractID(resultSet.getInt("contract_id"));
        contractItem.setProductID(resultSet.getInt("product_id"));
        contractItem.setProductName(resultSet.getString("product_name"));
        contractItem.setQuantity(resultSet.getInt("quantity"));
        contractItem.setUnitPrice(resultSet.getBigDecimal("unit_price"));
        contractItem.setTotalPrice(resultSet.getBigDecimal("total_price"));

        return contractItem;
    }

    private Contract mapResultSetToContract(ResultSet resultSet) throws Exception {
        Contract contract = new Contract();

        // 从ResultSet中获取各列的值并设置到Contract对象中
        contract.setContractID(resultSet.getInt("contract_id"));
        contract.setContractName(resultSet.getString("contract_name"));
        contract.setContractDate(resultSet.getTimestamp("contract_date"));
        contract.setStartDate(resultSet.getTimestamp("start_date"));
        contract.setEndDate(resultSet.getTimestamp("end_date"));
        contract.setContractStatus(ContractStatus.fromString(resultSet.getString("contract_status")));
        contract.setTotalAmount(resultSet.getBigDecimal("total_amount"));
        contract.setPaidAmount(resultSet.getBigDecimal("paid_amount"));
        contract.setRemainingAmount(resultSet.getBigDecimal("remaining_amount"));
        contract.setCustomerID(resultSet.getInt("customer_id"));
        contract.setSalesmanID(resultSet.getInt("salesman_id"));

        return contract;
    }

    public void payForPurchaseList(int contractID, BigDecimal totalPrice) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        // 动态构建查询条件
        List<Object> params = new ArrayList<>();  // 存储查询参数
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();
//            connection.setAutoCommit(false); // 开启事务

            // 基本的查询语句，不包含条件部分
            // 1.修改已支付金额
            StringBuilder sqlBuilder = new StringBuilder("UPDATE Contract SET paid_amount = paid_amount + ? ");

            String updatePayAmountSql = "UPDATE Contract " +
                    "SET paid_amount = paid_amount + ?, " +
                    "contract_status = ?, start_date = ? " +
                    "WHERE contract_id = ?;";
            preparedStatement = connection.prepareStatement(updatePayAmountSql);

            // 设置参数值
            params.add(totalPrice);      // customer_name
            params.add(ContractStatus.getInt(ContractStatus.IN_PROGRESS.getValue()));
            params.add(LocalDateTime.now());
            params.add(contractID);     // contact_person

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            preparedStatement.executeUpdate();
//
//            // 清除参数list 用于下一个sql语句注入
//            params.clear();
//
//            // 2. 修改合同状态 如果是未开始则设置成进行中
//            String updateContractStatusSql = "UPDATE Contract SET contract_status = ? WHERE contract_id = ? AND contract_status = ?;";
//            preparedStatement = connection.prepareStatement(updateContractStatusSql);
//            // 设置参数值
//            params.add(ContractStatus.getInt(ContractStatus.IN_PROGRESS.getValue())); //进行中
//            params.add(contractID);     // contact_person
//            params.add(ContractStatus.getInt(ContractStatus.NOT_STARTED.getValue())); //未开始
//
//            // 设置查询参数
//            for (int i = 0; i < params.size(); i++) {
//                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
//            }
//
//            // 提交事务
//            connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeResources(connection, preparedStatement, null);
        }
    }

    public boolean updateContractStatus(Integer contractID, ContractStatus contractStatus) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        // 动态构建查询条件
        List<Object> params = new ArrayList<>();  // 存储查询参数
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql= "UPDATE Contract " +
                    "SET contract_status = ? " +
                    "WHERE contract_id = ?;";
            preparedStatement = connection.prepareStatement(sql);

            // 设置参数值
            params.add(ContractStatus.getInt(contractStatus.getValue()));
            params.add(contractID);

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            return preparedStatement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeResources(connection, preparedStatement, null);
        }

        return false;
    }

    private List<ContractItem> mapResultSetToContractItems(ResultSet resultSet) throws SQLException {
        List<ContractItem> contractItems = new ArrayList<>();

        // 假设有多个 ContractItem 记录存在，并且每行都是一个合同项的记录
        do {
            ContractItem item = new ContractItem();
            item.setContractItemID(resultSet.getInt("contract_item_id"));
            item.setProductID(resultSet.getInt("product_id"));
            item.setProductName(resultSet.getString("product_name"));
            item.setQuantity(resultSet.getInt("quantity"));
            item.setUnitPrice(resultSet.getBigDecimal("unit_price"));
            item.setTotalPrice(resultSet.getBigDecimal("total_price"));
            contractItems.add(item);

            contractItems.add(item);
        } while (resultSet.next()); // 假设 ResultSet 中每一行都是一个合同项

        return contractItems;
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

    public List<Contract> searchContractsBySalesman(ContractSearchCriteria contractSearchCriteria, int pageNum, int pageSize, int salesmanID) {
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

            sqlBuilder.append(" AND salesman_id = ?");
            params.add(salesmanID);

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
                Contract contract = mapResultSetToContract(resultSet);
                ret.add(contract);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return ret;
    }

    public int countContractsBySalesman(ContractSearchCriteria contractSearchCriteria, int salesmanID) {
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

            sqlBuilder.append(" AND salesman_id = ?");
            params.add(salesmanID);

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

    public boolean setEndDate(Integer contractID, LocalDateTime now) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        // 动态构建查询条件
        List<Object> params = new ArrayList<>();  // 存储查询参数
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql= "UPDATE Contract " +
                    "SET end_date = ? " +
                    "WHERE contract_id = ?;";
            preparedStatement = connection.prepareStatement(sql);

            // 设置参数值
            params.add(now);
            params.add(contractID);

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            return preparedStatement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeResources(connection, preparedStatement, null);
        }

        return false;
    }

    public BigDecimal getTotalSales(String startDate, String endDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        BigDecimal totalSales = BigDecimal.ZERO;
        try {
            connection = DatabaseConnection.getConnection();
            StringBuilder sqlBuilder = new StringBuilder("SELECT SUM(total_amount) AS total_sales FROM Contract WHERE contract_status = 'completed'");

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();

            // 如果 startDate 和 endDate 不为空，则添加相关条件
            if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                sqlBuilder.append(" AND end_date BETWEEN ? AND ?");
                params.add(startDate);
                params.add(endDate);
            }

            String sql = sqlBuilder.toString();
            preparedStatement = connection.prepareStatement(sql);

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalSales = resultSet.getBigDecimal("total_sales");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return totalSales;
    }

    public List<CustomerSales> getCustomerSales(String startDate, String endDate, String customerIDStr) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<CustomerSales> customerSalesList = new ArrayList<>();
        try {
            connection = DatabaseConnection.getConnection();
            StringBuilder sqlBuilder = new StringBuilder(
                    "SELECT c.customer_name, SUM(ci.total_price) AS total_sales " +
                            "FROM Contract ct " +
                            "JOIN Customer c ON ct.customer_id = c.customer_id " +
                            "JOIN ContractItem ci ON ct.contract_id = ci.contract_id " +
                            "WHERE ct.contract_status = 'completed' "
            );

            List<Object> params = new ArrayList<>();

            // 如果 startDate 和 endDate 不为空，则添加相关条件
            if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                sqlBuilder.append("AND ct.end_date BETWEEN ? AND ? ");
                params.add(startDate);
                params.add(endDate);
            }

            if(customerIDStr != null && !customerIDStr.isEmpty()) {
                sqlBuilder.append("AND c.customer_id = ? ");
                params.add(Integer.parseInt(customerIDStr));
            }

            sqlBuilder.append("GROUP BY c.customer_name");

            String sql = sqlBuilder.toString();
            preparedStatement = connection.prepareStatement(sql);

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String customerName = resultSet.getString("customer_name");
                BigDecimal totalSales = resultSet.getBigDecimal("total_sales");
                customerSalesList.add(new CustomerSales(customerName, totalSales));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return customerSalesList;
    }

    public List<ProductSales> getProductSales(String startDate, String endDate, String productIDStr) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<ProductSales> productSalesList = new ArrayList<>();
        try {
            connection = DatabaseConnection.getConnection();
            StringBuilder sqlBuilder = new StringBuilder(
                    "SELECT p.product_name, SUM(ci.total_price) AS total_sales " +
                            "FROM Contract ct " +
                            "JOIN ContractItem ci ON ct.contract_id = ci.contract_id " +
                            "JOIN Product p ON ci.product_id = p.product_id " +
                            "WHERE ct.contract_status = 'completed' "
            );

            List<Object> params = new ArrayList<>();

            // 如果 startDate 和 endDate 不为空，则添加相关条件
            if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
                sqlBuilder.append("AND ct.end_date BETWEEN ? AND ? ");
                params.add(startDate);
                params.add(endDate);
            }
            if(productIDStr != null && !productIDStr.isEmpty()) {
                sqlBuilder.append("AND p.product_id = ? ");
                params.add(Integer.parseInt(productIDStr));
            }

            sqlBuilder.append("GROUP BY p.product_name");

            String sql = sqlBuilder.toString();
            preparedStatement = connection.prepareStatement(sql);

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String productName = resultSet.getString("product_name");
                BigDecimal totalSales = resultSet.getBigDecimal("total_sales");
                productSalesList.add(new ProductSales(productName, totalSales));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return productSalesList;
    }

}
