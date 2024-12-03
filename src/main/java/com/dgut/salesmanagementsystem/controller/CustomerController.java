package com.dgut.salesmanagementsystem.controller;

import com.dgut.salesmanagementsystem.model.CustomerDAO;
import com.dgut.salesmanagementsystem.pojo.Customer;
import com.dgut.salesmanagementsystem.pojo.CustomerStatus;
import com.dgut.salesmanagementsystem.pojo.CustomerType;
import com.dgut.salesmanagementsystem.service.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/CustomerController")
public class CustomerController extends HttpServlet {

    private CustomerService customerService;

    private int pageSize;

    @Override
    public void init() throws ServletException {
        customerService = new CustomerService();
        pageSize = 6;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            int customerID = Integer.parseInt(req.getParameter("customerID"));
            customerService.deleteCustomer(customerID);
            resp.sendRedirect("customer/customer_management.jsp");
        } else {
            String searchKeyword = req.getParameter("searchKeyword");
            int pageNum = req.getParameter("pageNum") == null ? 1 : Integer.parseInt(req.getParameter("pageNum"));

            List<Customer> customerList = customerService.searchCustomers(searchKeyword, pageNum, pageSize);
            // 获取总记录数以计算总页数
            int totalPages = customerService.getTotalPage(searchKeyword, pageSize);
            // 设置分页相关属性
            HttpSession session = req.getSession();
            session.setAttribute("customerList", customerList);
            session.setAttribute("currentPage", pageNum);
            session.setAttribute("totalPages", totalPages);
            session.setAttribute("searchKeyword", searchKeyword);

            resp.sendRedirect("customer/customer_management.jsp");
            // 不能用getRequestDispatcher，不然地址不会改变，还是在Controller这里
//            req.getRequestDispatcher("/customer/customer_management.jsp").forward(req, resp);
            System.out.println("Controller Called");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("add".equals(action)) {
            Customer customer = new Customer();
            customer.setCustomerName(req.getParameter("customerName"));
            customer.setContactPerson(req.getParameter("contactPerson"));
            customer.setPhone(req.getParameter("phone"));
            customer.setEmail(req.getParameter("email"));
            customer.setAddress(req.getParameter("address"));
            customer.setCity(req.getParameter("city"));
            customer.setPostalCode(req.getParameter("postalCode"));
            customer.setCountry(req.getParameter("country"));
            // 这里得到的是序号“1”、“2”
            String customerTypeStr = req.getParameter("customerType");
            customer.setCustomerType(CustomerType.fromInt(Integer.parseInt(customerTypeStr)).getValue());
            // 这里得到的是序号“1”、“2”、“3”
            String customerStatusStr = req.getParameter("customerStatus");
            customer.setCustomerStatus(CustomerStatus.fromInt(Integer.parseInt(customerStatusStr)).getValue());
            customerService.addCustomer(customer);
        } else if ("edit".equals(action)) {
            Customer customer = new Customer();
            customer.setCustomerID(Integer.parseInt(req.getParameter("customerId")));
            customer.setCustomerName(req.getParameter("customerName"));
            customer.setContactPerson(req.getParameter("contactPerson"));
            customer.setPhone(req.getParameter("phone"));
            customer.setEmail(req.getParameter("email"));
            customer.setAddress(req.getParameter("address"));
            customerService.updateCustomer(customer);
        }
        resp.sendRedirect("customer/customer_management.jsp");
    }
}
