package com.dgut.salesmanagementsystem.model;

import com.dgut.salesmanagementsystem.pojo.Role;
import com.dgut.salesmanagementsystem.pojo.User;
import com.dgut.salesmanagementsystem.tool.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public User validateUser(String username, String password) {
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM Users WHERE user_name = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password); // 假设密码未加密

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setUserID(resultSet.getInt("user_id"));
                user.setUserName(resultSet.getString("user_name"));
                String roleFromDb = resultSet.getString("role");
                user.setRole(Role.fromString(roleFromDb));
                // 注意：密码不应返回给客户端
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, resultSet);
        }

        return user;
    }

    public void addUser(String name, String password, Role role) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO Users (user_name, password, role) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            params.add(name);
            params.add(password);
            params.add(Role.getInt(role.getRole()));

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(connection, preparedStatement, null);
        }
    }
}
