package com.dgut.salesmanagementsystem.model;

import com.dgut.salesmanagementsystem.pojo.Role;
import com.dgut.salesmanagementsystem.pojo.User;
import com.dgut.salesmanagementsystem.tool.DatabaseConnection;

import java.sql.*;

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
            // 关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return user;
    }
}
