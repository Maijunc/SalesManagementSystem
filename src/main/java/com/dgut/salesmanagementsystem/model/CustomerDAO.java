package com.dgut.salesmanagementsystem.model;

import com.dgut.salesmanagementsystem.pojo.Customer;
import com.dgut.salesmanagementsystem.pojo.Role;
import com.dgut.salesmanagementsystem.pojo.User;
import com.dgut.salesmanagementsystem.tool.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public void deleteCustomer(int customerID) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql = "DELETE FROM Customer WHERE customer_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerID);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 关键字是客户名称和联系人
    public List<Customer> searchCustomers(String searchKeyword, int pageNum, int pageSize) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<Customer> ret = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            connection = DatabaseConnection.getConnection();

            // 构建 SQL 查询语句
            String sql = "SELECT * FROM Customer WHERE customer_name LIKE ? " +
                    "OR contact_person LIKE ?" +
                    " LIMIT ? OFFSET ?";
            preparedStatement = connection.prepareStatement(sql);

            // 设置参数
            preparedStatement.setString(1, "%" + searchKeyword + "%"); // 模糊查询
            preparedStatement.setString(2, "%" + searchKeyword + "%"); // 模糊查询
            preparedStatement.setInt(3, pageSize);                    // 每页显示条数
            preparedStatement.setInt(4, (pageNum - 1) * pageSize);    // 偏移量

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerID(resultSet.getInt("customer_id"));         // 设置 customer_id
                customer.setCustomerName(resultSet.getString("customer_name"));   // 设置 customer_name
                customer.setContactPerson(resultSet.getString("contact_person")); // 设置 contact_person
                customer.setPhone(resultSet.getString("phone"));                 // 设置 phone
                customer.setEmail(resultSet.getString("email"));                 // 设置 email
                customer.setAddress(resultSet.getString("address"));             // 设置 address
                customer.setCity(resultSet.getString("city"));                   // 设置 city
                customer.setPostalCode(resultSet.getString("postal_code"));      // 设置 postal_code
                customer.setCountry(resultSet.getString("country"));             // 设置 country
                customer.setSalesRep(resultSet.getString("sales_rep"));          // 设置 sales_rep
                customer.setCustomerType(resultSet.getString("customer_type"));  // 设置 customer_type
                customer.setCustomerStatus(resultSet.getString("customer_status")); // 设置 customer_status
                customer.setCreatedDate(resultSet.getTimestamp("created_date")); // 设置 created_date
                customer.setLastModifiedDate(resultSet.getTimestamp("last_modified_date")); // 设置 last_modified_date
                ret.add(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


    public void addCustomer(Customer customer) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql = "INSERT INTO Customer (customer_name, contact_person, phone, " +
                    "email, address, city, postal_code, country, " +
                    "sales_rep, customer_type, customer_status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数值
            preparedStatement.setString(1, customer.getCustomerName());         // customer_name
            preparedStatement.setString(2, customer.getContactPerson());        // contact_person
            preparedStatement.setString(3, customer.getPhone());                // phone
            preparedStatement.setString(4, customer.getEmail());                // email
            preparedStatement.setString(5, customer.getAddress());              // address
            preparedStatement.setString(6, customer.getCity());                 // city
            preparedStatement.setString(7, customer.getPostalCode());           // postal_code
            preparedStatement.setString(8, customer.getCountry());              // country
            preparedStatement.setString(9, customer.getSalesRep());             // sales_rep
            preparedStatement.setString(10, customer.getCustomerType());       // customer_type
            preparedStatement.setString(11, customer.getCustomerStatus());     // customer_status

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateCustomer(Customer customer) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql = "UPDATE Customer " +
                    "SET customer_name = ?, " +
                    "contact_person = ?, " +
                    "phone = ?, " +
                    "email = ?, " +
                    "address = ?, " +
                    "city = ?, " +
                    "postal_code = ?, " +
                    "country = ?, " +
                    "sales_rep = ?, " +
                    "customer_type = ?, " +
                    "customer_status = ? " +
                    "WHERE customer_id = ?;";
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数值
            preparedStatement.setString(1, customer.getCustomerName());         // customer_name
            preparedStatement.setString(2, customer.getContactPerson());        // contact_person
            preparedStatement.setString(3, customer.getPhone());                // phone
            preparedStatement.setString(4, customer.getEmail());                // email
            preparedStatement.setString(5, customer.getAddress());              // address
            preparedStatement.setString(6, customer.getCity());                 // city
            preparedStatement.setString(7, customer.getPostalCode());           // postal_code
            preparedStatement.setString(8, customer.getCountry());              // country
            preparedStatement.setString(9, customer.getSalesRep());             // sales_rep
            preparedStatement.setString(10, customer.getCustomerType());       // customer_type
            preparedStatement.setString(11, customer.getCustomerStatus());     // customer_status
            preparedStatement.setInt(12, customer.getCustomerID());            // customer_id (WHERE condition)

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Customer getCustomerById(int customerID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Customer customer = null;
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            connection = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM Customer WHERE customer_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerID);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                customer = new Customer();
                customer.setCustomerID(customerID);         // 设置 customer_id
                customer.setCustomerName(resultSet.getString("customer_name"));   // 设置 customer_name
                customer.setContactPerson(resultSet.getString("contact_person")); // 设置 contact_person
                customer.setPhone(resultSet.getString("phone"));                 // 设置 phone
                customer.setEmail(resultSet.getString("email"));                 // 设置 email
                customer.setAddress(resultSet.getString("address"));             // 设置 address
                customer.setCity(resultSet.getString("city"));                   // 设置 city
                customer.setPostalCode(resultSet.getString("postal_code"));      // 设置 postal_code
                customer.setCountry(resultSet.getString("country"));             // 设置 country
                customer.setSalesRep(resultSet.getString("sales_rep"));          // 设置 sales_rep
                customer.setCustomerType(resultSet.getString("customer_type"));  // 设置 customer_type
                customer.setCustomerStatus(resultSet.getString("customer_status")); // 设置 customer_status
                customer.setCreatedDate(resultSet.getTimestamp("created_date")); // 设置 created_date
                customer.setLastModifiedDate(resultSet.getTimestamp("last_modified_date")); // 设置 last_modified_date
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

        return customer;
    }

    public int countCustomers(String searchKeyword) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int totalRecords = 0;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM Customer WHERE customer_name LIKE ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + searchKeyword + "%");

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalRecords = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return totalRecords;
    }

}
