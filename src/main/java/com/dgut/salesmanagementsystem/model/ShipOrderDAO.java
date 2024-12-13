package com.dgut.salesmanagementsystem.model;

import com.dgut.salesmanagementsystem.pojo.ShipOrder;
import com.dgut.salesmanagementsystem.pojo.ShipOrderStatus;
import com.dgut.salesmanagementsystem.tool.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShipOrderDAO {
    public void createShipOrder(ShipOrder shipOrder) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            // 动态构建 SQL 插入语句
            String sql = "INSERT INTO ShipOrder (product_name, product_id, purchase_list_id, purchase_list_item_id, unit_price, customer_id, " +
                    "customer_name, total_amount, quantity, recipient_name, recipient_phone, shipping_address, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            preparedStatement = connection.prepareStatement(sql);

            // 设置参数值
            List<Object> params = new ArrayList<>();
            params.add(shipOrder.getProductName());
            params.add(shipOrder.getProductID());
            params.add(shipOrder.getPurchaseListID());
            params.add(shipOrder.getPurchaseListItemID());
            params.add(shipOrder.getUnitPrice());
            params.add(shipOrder.getCustomerID());
            params.add(shipOrder.getCustomerName());
            params.add(shipOrder.getTotalAmount());
            params.add(shipOrder.getQuantity());
            params.add(shipOrder.getRecipientName());
            params.add(shipOrder.getRecipientPhone());
            params.add(shipOrder.getShippingAddress());
            params.add(shipOrder.getNotes());

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            // 执行插入操作
            preparedStatement.executeUpdate();

            System.out.println("Ship Order has been successfully created.");

        } catch (Exception e) {
            e.printStackTrace();
            // 错误处理：可以记录日志或返回错误信息
        } finally {
            // 关闭资源
            closeResources(connection, preparedStatement, null);
        }
    }

    private ShipOrder mapResultSetToShipOrder(ResultSet resultSet) throws SQLException {
        ShipOrder shipOrder = new ShipOrder();



        return shipOrder;
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

    public int checkShipOrderExists(int purchaseListItemID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int totalRecords = 0;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM ShipOrder WHERE purchase_list_item_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, purchaseListItemID);

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
