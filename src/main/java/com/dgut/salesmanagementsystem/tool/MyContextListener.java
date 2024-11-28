package com.dgut.salesmanagementsystem.tool;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.sql.DriverManager;
import java.sql.SQLException;

/*
解决这个警告：
警告 [main] org.apache.catalina.loader.WebappClassLoaderBase.clearReferencesJdbc
 Web应用程序 [ROOT] 注册了JDBC驱动程序 [com.mysql.cj.jdbc.Driver]，但在Web应用程序停止时无法注销它。
 为防止内存泄漏，JDBC驱动程序已被强制取消注册。
警告 [main] org.apache.catalina.loader.WebappClassLoaderBase.clearReferencesThreads
 Web应用程序[ROOT]似乎启动了一个名为[mysql-cj-abandoned-connection-cleanup]的线程，但未能停止它。
 这很可能会造成内存泄漏。
*/
public class MyContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        System.out.println("MyContextListener: webService start");
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        System.out.println("MyContextListener: webService stop");
        try {
            while(DriverManager.getDrivers().hasMoreElements()) {
                DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
            }
            System.out.println("MyContextListener: jdbc Driver close");
            AbandonedConnectionCleanupThread.checkedShutdown();
            System.out.println("MyContextListener: clean thread success");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}