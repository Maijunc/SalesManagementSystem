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

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            // 构建 SQL 查询语句
            String sql = "SELECT * FROM Customer WHERE customer_name LIKE ? " +
                    "OR contact_person LIKE ?" +
                    " LIMIT ? OFFSET ?";
            preparedStatement = connection.prepareStatement(sql);

            params.add("%" + searchKeyword + "%"); // 模糊查询
            params.add("%" + searchKeyword + "%"); // 模糊查询
            params.add(pageSize);  // 每页显示条数
            params.add((pageNum - 1) * pageSize); // 偏移量

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

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

            // 动态构建查询条件
            List<Object> params = new ArrayList<>();  // 存储查询参数

            String sql = "INSERT INTO Customer (customer_name, contact_person, phone, " +
                    "email, address, city, postal_code, country, " +
                    "customer_type, customer_status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数值
            params.add(customer.getCustomerName());
            params.add(customer.getContactPerson());
            params.add(customer.getPhone());
            params.add(customer.getEmail());
            params.add(customer.getAddress());
            params.add(customer.getCity());
            params.add(customer.getPostalCode());
            params.add(customer.getCountry());
            params.add(customer.getCustomerType());
            params.add(customer.getCustomerStatus());

            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

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

        // 动态构建查询条件
        List<Object> params = new ArrayList<>();  // 存储查询参数
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
                    "customer_type = ?, " +
                    "customer_status = ? " +
                    "WHERE customer_id = ?;";
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数值

            // 设置参数值
            params.add(customer.getCustomerName());      // customer_name
            params.add(customer.getContactPerson());     // contact_person
            params.add(customer.getPhone());             // phone
            params.add(customer.getEmail());             // email
            params.add(customer.getAddress());           // address
            params.add(customer.getCity());              // city
            params.add(customer.getPostalCode());        // postal_code
            params.add(customer.getCountry());           // country
            params.add(customer.getCustomerType());      // customer_type
            params.add(customer.getCustomerStatus());    // customer_status
            params.add(customer.getCustomerID());        // customer_id (WHERE condition)


            // 设置查询参数
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));  // 使用 setObject 来动态设置参数
            }

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
