package com.dgut.salesmanagementsystem.service;

import com.dgut.salesmanagementsystem.model.CustomerDAO;
import com.dgut.salesmanagementsystem.pojo.Customer;

import java.util.List;

public class CustomerService {
    private CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }

    public void deleteCustomer(int customerID) {
        customerDAO.deleteCustomer(customerID);
    }

    public List<Customer> searchCustomers(String searchKeyword, int pageNum, int pageSize) {

        return customerDAO.searchCustomers(searchKeyword, pageNum, pageSize);
    }

    public void addCustomer(Customer customer) {
        customerDAO.addCustomer(customer);
    }

    public void updateCustomer(Customer customer) {
        customerDAO.updateCustomer(customer);
    }

    public int getTotalPages(String searchKeyword,int pageSize) {

        int totalRecords = customerDAO.countCustomers(searchKeyword);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        totalPages = totalPages > 0 ? totalPages : 1;
        return totalPages;
    }
}
