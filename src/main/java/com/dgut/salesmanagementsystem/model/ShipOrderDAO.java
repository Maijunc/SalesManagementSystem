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

    public ShipOrder getShipOrderById(int shipOrderID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ShipOrder shipOrder = null;

        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            // 动态构建 SQL 查询语句
            String sql = "SELECT * FROM ShipOrder WHERE ship_order_id = ?;";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, shipOrderID);

            // 执行查询
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                shipOrder = new ShipOrder();
                shipOrder = mapResultSetToShipOrder(resultSet);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // 错误处理：可以记录日志或返回错误信息
        } finally {
            // 关闭资源
            closeResources(connection, preparedStatement, resultSet);
        }

        return shipOrder;
    }

    public List<ShipOrder> getShipOrdersWithPagination(int page, int pageSize) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<ShipOrder> shipOrders = new ArrayList<>();

        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            // 动态构建 SQL 查询语句
            String sql = "SELECT * FROM ShipOrder LIMIT ? OFFSET ?";

            preparedStatement = connection.prepareStatement(sql);

            // 设置分页参数
            int offset = (page - 1) * pageSize;
            preparedStatement.setInt(1, pageSize); // 每页记录数
            preparedStatement.setInt(2, offset);  // 偏移量

            // 执行查询
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                shipOrders.add(mapResultSetToShipOrder(resultSet));
            }

        } catch (Exception e) {
            e.printStackTrace();
            // 错误处理：可以记录日志或返回错误信息
        } finally {
            // 关闭资源
            closeResources(connection, preparedStatement, resultSet);
        }

        return shipOrders;
    }

    // 获取总记录数
    public int getShipOrderCount() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;

        try {
            connection = DatabaseConnection.getConnection();

            String sql = "SELECT COUNT(*) AS total FROM ShipOrder";
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }

        return count;
    }

    public boolean updateShipOrder(ShipOrder shipOrder){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        // 动态构建查询条件
        List<Object> params = new ArrayList<>();  // 存储查询参数
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql = "UPDATE ShipOrder SET shipping_company = ?, tracking_number = ?, notes = ?, " +
                    "ship_date = ?, ship_order_status = ? WHERE ship_order_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数值

            // 设置参数值
            params.add(shipOrder.getShippingCompany());
            params.add(shipOrder.getTrackingNumber());
            params.add(shipOrder.getNotes());
            params.add(shipOrder.getShipDate());
            params.add(ShipOrderStatus.getInt(shipOrder.getShipOrderStatus().getValue()));           // address
            params.add(shipOrder.getShipOrderID());


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



    // 映射 ResultSet 到 ShipOrder
    private ShipOrder mapResultSetToShipOrder(ResultSet resultSet) throws Exception {
        ShipOrder shipOrder = new ShipOrder();

        shipOrder.setShipOrderID(resultSet.getInt("ship_order_id"));
        shipOrder.setProductID(resultSet.getInt("product_id"));
        shipOrder.setProductName(resultSet.getString("product_name"));
        shipOrder.setUnitPrice(resultSet.getBigDecimal("unit_price"));
        shipOrder.setQuantity(resultSet.getInt("quantity"));
        shipOrder.setPurchaseListID(resultSet.getInt("purchase_list_id"));
        shipOrder.setPurchaseListItemID(resultSet.getInt("purchase_list_item_id"));
        shipOrder.setShipOrderStatus(ShipOrderStatus.fromString(resultSet.getString("ship_order_status")));
        shipOrder.setShipDate(resultSet.getTimestamp("ship_date"));
        shipOrder.setCustomerID(resultSet.getInt("customer_id"));
        shipOrder.setCustomerName(resultSet.getString("customer_name"));
        shipOrder.setRecipientName(resultSet.getString("recipient_name"));
        shipOrder.setShippingAddress(resultSet.getString("shipping_address"));
        shipOrder.setRecipientPhone(resultSet.getString("recipient_phone"));
        shipOrder.setShippingCompany(resultSet.getString("shipping_company"));
        shipOrder.setTrackingNumber(resultSet.getString("tracking_number"));
        shipOrder.setTotalAmount(resultSet.getBigDecimal("total_amount"));
        shipOrder.setShippedBy(resultSet.getString("shipped_by"));
        shipOrder.setNotes(resultSet.getString("notes"));
        shipOrder.setCreatedAt(resultSet.getTimestamp("created_at"));
        shipOrder.setUpdatedAt(resultSet.getTimestamp("updated_at"));

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
