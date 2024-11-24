package com.dgut.salesmanagementsystem.tool;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    // JDBC连接字符串，用户和密码
    private static final String DB_URL = "jdbc:mysql://localhost:3306/SalesManagementSystemDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";
    public static Connection getConnection() throws Exception {
        // 加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 获取连接
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
