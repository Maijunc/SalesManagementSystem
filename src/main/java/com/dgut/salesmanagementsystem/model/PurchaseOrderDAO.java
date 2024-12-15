package com.dgut.salesmanagementsystem.model;

import com.dgut.salesmanagementsystem.pojo.PurchaseOrder;
import com.dgut.salesmanagementsystem.pojo.PurchaseOrderStatus;
import com.dgut.salesmanagementsystem.tool.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderDAO {

    // 添加进货单
    // 添加进货单
    public void addPurchaseOrder(PurchaseOrder purchaseOrder) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO PurchaseOrder (product_id, product_name, required_quantity, " +
                    "purchase_order_status, notes) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            List<Object> params = new ArrayList<>();
            params.add(purchaseOrder.getProductID());
            params.add(purchaseOrder.getProductName());
            params.add(purchaseOrder.getRequiredQuantity());
            params.add(PurchaseOrderStatus.getInt(purchaseOrder.getPurchaseOrderStatus().getValue()));
            params.add(purchaseOrder.getNotes());

            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, null);
        }
    }

    // 根据 ID 更新进货单
    public boolean updatePurchaseOrder(PurchaseOrder purchaseOrder) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE PurchaseOrder SET product_name = ?, required_quantity = ?, actual_quantity = ?, " +
                    "purchase_order_status = ?, supplier_name = ?, notes = ?, updated_at = ? WHERE purchase_order_id = ?";
            preparedStatement = connection.prepareStatement(sql);

            // 动态构建查询参数
            List<Object> params = new ArrayList<>();
            params.add(purchaseOrder.getProductName());
            params.add(purchaseOrder.getRequiredQuantity());
            params.add(purchaseOrder.getActualQuantity());
            params.add(PurchaseOrderStatus.getInt(purchaseOrder.getPurchaseOrderStatus().getValue()));
            params.add(purchaseOrder.getSupplierName());
            params.add(purchaseOrder.getNotes());
            params.add(purchaseOrder.getUpdatedAt());
            params.add(purchaseOrder.getPurchaseOrderID());

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, null);
        }

        return false;
    }

    public List<PurchaseOrder> getPurchaseOrdersByPage(int pageNum, int pageSize) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<PurchaseOrder> purchaseOrders = new ArrayList<>();
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM PurchaseOrder ORDER BY created_at DESC LIMIT ?, ?";
            preparedStatement = connection.prepareStatement(sql);

            // 计算起始行索引
            int startIndex = (pageNum - 1) * pageSize;
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, pageSize);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                purchaseOrders.add(mapResultSetToPurchaseOrder(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, resultSet);
        }
        return purchaseOrders;
    }

    // 获取总记录数
    public int getPurchaseOrderCount() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM PurchaseOrder";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, resultSet);
        }
        return count;
    }


    // 从 ResultSet 映射到 PurchaseOrder
    private PurchaseOrder mapResultSetToPurchaseOrder(ResultSet resultSet) throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderID(resultSet.getInt("purchase_order_id"));
        purchaseOrder.setProductID(resultSet.getInt("product_id"));
        purchaseOrder.setProductName(resultSet.getString("product_name"));
        purchaseOrder.setRequiredQuantity(resultSet.getInt("required_quantity"));
        purchaseOrder.setActualQuantity(resultSet.getInt("actual_quantity"));
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.fromString(resultSet.getString("purchase_order_status")));
        purchaseOrder.setSupplierName(resultSet.getString("supplier_name"));
        purchaseOrder.setNotes(resultSet.getString("notes"));
        purchaseOrder.setCreatedAt(resultSet.getTimestamp("created_at"));
        purchaseOrder.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return purchaseOrder;
    }

    public PurchaseOrder getPurchaseOrderByID(Integer purchaseOrderID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PurchaseOrder purchaseOrder = null;

        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            // 动态构建 SQL 查询语句
            String sql = "SELECT * FROM PurchaseOrder WHERE purchase_order_id = ?;";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, purchaseOrderID);

            // 执行查询
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                purchaseOrder = mapResultSetToPurchaseOrder(resultSet);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // 错误处理：可以记录日志或返回错误信息
        } finally {
            // 关闭资源
            DatabaseConnection.closeResources(connection, preparedStatement, resultSet);
        }

        return purchaseOrder;
    }
}
