package com.dgut.salesmanagementsystem.model;

import com.dgut.salesmanagementsystem.pojo.*;
import com.dgut.salesmanagementsystem.tool.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseListDAO {
    public List<PurchaseList> getPurchaseListsByContractID(int contractID, int pageNum, int pageSize) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<PurchaseList> ret = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            // 构建 SQL 查询语句
            String sql = "SELECT * FROM PurchaseList WHERE contract_id = ? " +
                    " LIMIT ? OFFSET ?";
            preparedStatement = connection.prepareStatement(sql);

            params.add(contractID);
            params.add(pageSize);  // 每页显示条数
            params.add((pageNum - 1) * pageSize); // 偏移量

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PurchaseList purchaseList = mapResultSetToPurchaseList(resultSet);
                ret.add(purchaseList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return ret;
    }

    public int countPurchaseLists(int contractID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int totalRecords = 0;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM PurchaseList WHERE contract_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, contractID);

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

    public List<RemainingProduct> searchRemainingProducts(String searchKeyword, int pageNum, int pageSize, int contractID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<RemainingProduct> ret = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            System.out.println(searchKeyword);

            // 构建 SQL 查询语句
            String sql = "SELECT " +
                    "    ContractItem.product_id AS product_id," +
                    "    ContractItem.product_name AS product_name," +
                    "    ContractItem.quantity - COALESCE(SUM(PurchaseListItem.quantity), 0) AS remaining_quantity," +
                    "    ContractItem.unit_price AS unit_price," +
                    "    Product.stock_quantity AS stock_quantity " +
                    "FROM ContractItem " +
                    "LEFT JOIN " +
                    "    PurchaseListItem ON ContractItem.product_id = PurchaseListItem.product_id " +
                    "LEFT JOIN " +
                    "    PurchaseList ON PurchaseListItem.purchase_list_id = PurchaseList.purchase_list_id " +
                    "LEFT JOIN " +
                    "    Product ON Product.product_id  = ContractItem.product_id " +
                    "WHERE " +
                    "    ContractItem.contract_id = ? AND (ContractItem.product_name LIKE ? OR ContractItem.product_id = ?)" +
                    "GROUP BY " +
                    "    ContractItem.product_id, ContractItem.product_name, ContractItem.quantity, ContractItem.unit_price, Product.stock_quantity LIMIT ? OFFSET ?;";
            preparedStatement = connection.prepareStatement(sql);

            params.add(contractID);
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
                RemainingProduct remainingProduct = mapResultSetToRemainingProduct(resultSet);
                ret.add(remainingProduct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return ret;
    }

    public void addPurchaseList(PurchaseList purchaseList) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            // 1. 插入合同表
            String sql = "INSERT INTO PurchaseList (" +
                    " contract_id," +
                    " total_price)" +
                    "VALUES (?, ?)";

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // 设置参数值
            params.add(purchaseList.getContractID());
            params.add(purchaseList.getTotalPrice());
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
                        int purchaseListID = generatedKeys.getInt(1); // 获取自增的合同ID
                        // 2. 插入合同商品项
                        insertPurchaseItems(purchaseListID, purchaseList.getPurchaseListItems());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, null);
        }
    }

    private void insertPurchaseItems(int purchaseListID, List<PurchaseListItem> purchaseListItems){

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            // 1. 插入合同表
            String sql = "INSERT INTO PurchaseListItem (purchase_list_id, product_id, product_name, quantity, unit_price) "
                    + "VALUES (?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(sql);

            for (PurchaseListItem item : purchaseListItems) {
                preparedStatement.setInt(1, purchaseListID); // 设置合同ID
                preparedStatement.setInt(2, item.getProductID()); // 设置商品ID
                preparedStatement.setString(3, item.getProductName()); // 设置商品名称
                preparedStatement.setInt(4, item.getQuantity()); // 设置商品数量
                preparedStatement.setBigDecimal(5, item.getUnitPrice());

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

    public void editPaymentStatus(int purchaseListID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        // 动态构建查询条件
        List<Object> params = new ArrayList<>();  // 存储查询参数
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql = "UPDATE PurchaseList " +
                    "SET payment_status = ? " +
                    "WHERE purchase_list_id = ?;";
            preparedStatement = connection.prepareStatement(sql);

            params.add(PaymentStatus.getInt(PaymentStatus.PAID.getValue()));
            params.add(purchaseListID);
            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeResources(connection, preparedStatement, null);
        }
    }

    // 获取PurchaseList（数据库里面的，没有PurchaseListItem的）
    public PurchaseList getPurchaseListByID(int purchaseListID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PurchaseList purchaseList = null;
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM PurchaseList WHERE purchase_list_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, purchaseListID);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                purchaseList = mapResultSetToPurchaseList(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeResources(connection, preparedStatement, resultSet);
        }

        return purchaseList;
    }

    public List<PurchaseListItem> getAllPurchaseListItemsByPurchaseListID(int purchaseListID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<PurchaseListItem> purchaseListItems = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM PurchaseListItem WHERE purchase_list_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, purchaseListID);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PurchaseListItem purchaseListItem = mapResultSetToPurchaseListItem(resultSet);
                purchaseListItems.add(purchaseListItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeResources(connection, preparedStatement, resultSet);
        }

        return purchaseListItems;
    }

    public List<PurchaseListItem> getPurchaseListItemsByPage(Integer purchaseListID, int pageNum, int pageSize) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<PurchaseListItem> purchaseListItems = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM PurchaseListItem WHERE purchase_list_id = ? LIMIT ? OFFSET ?";
            preparedStatement = connection.prepareStatement(sql);

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            params.add(purchaseListID);
            params.add(pageSize);
            params.add((pageNum - 1) * pageSize);

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PurchaseListItem purchaseListItem = mapResultSetToPurchaseListItem(resultSet);
                purchaseListItems.add(purchaseListItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeResources(connection, preparedStatement, resultSet);
        }

        return purchaseListItems;
    }

    public int countPurchaseListItems(Integer purchaseListID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int totalRecords = 0;
        try {
            connection = DatabaseConnection.getConnection();
            // 基本的查询语句，不包含条件部分
            String sql = "SELECT COUNT(*) FROM PurchaseListItem WHERE purchase_list_id = ?";

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            params.add(purchaseListID);

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

    public ShipOrder getShipOrderInfo(Integer purchaseListItemID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ShipOrder shipOrder = null;
        ResultSet resultSet = null;
        int totalRecords = 0;
        try {
            connection = DatabaseConnection.getConnection();
            // 基本的查询语句，不包含条件部分
            String sql = "SELECT "
                    + "pli.product_name, "
                    + "pli.product_id, "
                    + "(pli.quantity * pli.unit_price) AS total_amount, "
                    + "pl.purchase_list_id, "
                    + "pli.quantity, "
                    + "pli.unit_price, "
                    + "c.customer_id, "
                    + "c.customer_name "
                    + "FROM "
                    + "PurchaseListItem pli "
                    + "JOIN PurchaseList pl ON pli.purchase_list_id = pl.purchase_list_id "
                    + "JOIN Contract ct ON pl.contract_id = ct.contract_id "
                    + "JOIN Customer c ON ct.customer_id = c.customer_id "
                    + "WHERE "
                    + "pli.purchase_list_item_id = ?;";

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            params.add(purchaseListItemID);

            preparedStatement = connection.prepareStatement(sql);

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                shipOrder = new ShipOrder();
                shipOrder.setProductName(resultSet.getString("product_name"));
                shipOrder.setProductID(resultSet.getInt("product_id"));
                shipOrder.setTotalAmount(resultSet.getBigDecimal("total_amount"));
                shipOrder.setPurchaseListID(resultSet.getInt("purchase_list_id"));
                shipOrder.setQuantity(resultSet.getInt("quantity"));
                shipOrder.setUnitPrice(resultSet.getBigDecimal("unit_price"));
                shipOrder.setCustomerID(resultSet.getInt("customer_id"));
                shipOrder.setCustomerName(resultSet.getString("customer_name"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return shipOrder;
    }

    private PurchaseListItem mapResultSetToPurchaseListItem(ResultSet resultSet) throws SQLException {
        PurchaseListItem purchaseListItem = new PurchaseListItem();
        purchaseListItem.setPurchaseListItemID(resultSet.getInt("purchase_list_item_id"));
        purchaseListItem.setPurchaseListID(resultSet.getInt("purchase_list_id")); // 假设在ResultSet中有这个字段
        purchaseListItem.setProductID(resultSet.getInt("product_id"));
        purchaseListItem.setProductName(resultSet.getString("product_name"));
        purchaseListItem.setQuantity(resultSet.getInt("quantity"));
        purchaseListItem.setUnitPrice(resultSet.getBigDecimal("unit_price"));
        return purchaseListItem;
    }


    private RemainingProduct mapResultSetToRemainingProduct(ResultSet resultSet) throws SQLException {
        RemainingProduct remainingProduct = new RemainingProduct();
        remainingProduct.setProductID(resultSet.getInt("product_id"));
        remainingProduct.setProductName(resultSet.getString("product_name"));
        remainingProduct.setStockQuantity(resultSet.getInt("stock_quantity"));
        remainingProduct.setUnitPrice(resultSet.getBigDecimal("unit_price"));
        remainingProduct.setRemainingQuantity(resultSet.getInt("remaining_quantity"));
        return remainingProduct;
    }


    private PurchaseList mapResultSetToPurchaseList(ResultSet resultSet) throws Exception{
        PurchaseList purchaseList = new PurchaseList();
        purchaseList.setPurchaseListID(resultSet.getInt("purchase_list_id"));
        purchaseList.setContractID(resultSet.getInt("contract_id"));
        purchaseList.setTotalPrice(resultSet.getBigDecimal("total_price"));
        purchaseList.setPaymentStatus(resultSet.getString("payment_status"));
        // 使用 getTimestamp 获取 java.sql.Timestamp，然后转换为 LocalDateTime
        purchaseList.setCreateDate(resultSet.getTimestamp("create_date"));
        return purchaseList;
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
