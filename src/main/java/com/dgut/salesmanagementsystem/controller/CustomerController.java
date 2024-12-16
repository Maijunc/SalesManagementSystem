package com.dgut.salesmanagementsystem.controller;

import com.dgut.salesmanagementsystem.model.CustomerDAO;
import com.dgut.salesmanagementsystem.pojo.*;
import com.dgut.salesmanagementsystem.service.CustomerService;
import com.dgut.salesmanagementsystem.service.SalesmanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(value = "/CustomerController")
public class CustomerController extends HttpServlet {

    private final CustomerService customerService = new CustomerService();
    private int pageSize;

    @Override
    public void init() throws ServletException {
        super.init();
        // 在这里获取Servlet上下文参数
        this.pageSize = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            deleteCustomer(req, resp);
        } else if("ajax".equals(action)){
            searchCustomerForAjax(req, resp);
        } else {
            searchCustomer(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("add".equals(action)) {
            addCustomer(req, resp);
        } else if ("edit".equals(action)) {
            editCustomer(req, resp);
        }


    }


    public Customer getCustomerById(int customerID) {
        return customerService.getCustomerById(customerID);
    }

    public void searchCustomer(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String searchKeyword = req.getParameter("searchKeyword");
        int pageNum = req.getParameter("pageNum") == null ? 1 : Integer.parseInt(req.getParameter("pageNum"));

        List<Customer> customerList = customerService.searchCustomers(searchKeyword, pageNum, pageSize);
        // 获取总页数
        int totalPages = customerService.getTotalPages(searchKeyword, pageSize);
        // 设置分页相关属性
        HttpSession session = req.getSession();
        session.setAttribute("customerList", customerList);
        session.setAttribute("currentPage", pageNum);
        session.setAttribute("totalPages", totalPages);
        session.setAttribute("searchKeyword", searchKeyword);

        resp.sendRedirect("customer/customer_management.jsp");
        // 不能用getRequestDispatcher，不然地址不会改变，还是在Controller这里
//            req.getRequestDispatcher("/customer/customer_management.jsp").forward(req, resp);
    }

    public void addCustomer(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String customerTypeStr = req.getParameter("customerType");
        String customerStatusStr = req.getParameter("customerStatus");

        Customer customer = new Customer();

        // 这里得到的是序号“1”、“2”
        if(customerTypeStr != null && !customerTypeStr.isEmpty())
            customer.setCustomerType(CustomerType.fromInt(Integer.parseInt(customerTypeStr)).getValue());
        // 这里得到的是序号“1”、“2”、“3”
        if(customerStatusStr != null && !customerStatusStr.isEmpty())
            customer.setCustomerStatus(CustomerStatus.fromInt(Integer.parseInt(customerStatusStr)).getValue());

        customer.setCustomerName(req.getParameter("customerName"));
        customer.setContactPerson(req.getParameter("contactPerson"));
        customer.setPhone(req.getParameter("phone"));
        customer.setEmail(req.getParameter("email"));
        customer.setAddress(req.getParameter("address"));
        customer.setCity(req.getParameter("city"));
        customer.setPostalCode(req.getParameter("postalCode"));
        customer.setCountry(req.getParameter("country"));

        customerService.addCustomer(customer);

        resp.sendRedirect("../CustomerController?pageNum=1&searchKeyword=");
    }

    public void deleteCustomer(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        int customerID = Integer.parseInt(req.getParameter("customerID"));
        customerService.deleteCustomer(customerID);
        resp.sendRedirect("customer/customer_management.jsp");
    }

    public void editCustomer(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        int pageNum = req.getParameter("pageNum") == null ? 1 : Integer.parseInt(req.getParameter("pageNum"));
        String searchKeyword = req.getParameter("searchKeyword");

        Customer customer = new Customer();
        customer.setCustomerID(Integer.parseInt(req.getParameter("customerID")));
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
        if(customerTypeStr != null && !customerTypeStr.isEmpty())
            customer.setCustomerType(CustomerType.fromInt(Integer.parseInt(customerTypeStr)).getValue());
        // 这里得到的是序号“1”、“2”、“3”
        String customerStatusStr = req.getParameter("customerStatus");
        if(customerStatusStr != null && !customerStatusStr.isEmpty())
            customer.setCustomerStatus(CustomerStatus.fromInt(Integer.parseInt(customerStatusStr)).getValue());
        customerService.updateCustomer(customer);

        resp.sendRedirect("../CustomerController?pageNum=" + pageNum + "&searchKeyword=" + searchKeyword);
    }

    public List<Customer> getPageCustomers(String searchKeyword, int pageNum){
        return customerService.searchCustomers(searchKeyword, pageNum, pageSize);
    }

    public void searchCustomerForAjax(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String searchKeyword = request.getParameter("searchKeyword");
        String pageParam = request.getParameter("pageNum");
        int curPage = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
        if(searchKeyword == null)
            searchKeyword = "";
        int pageNum = request.getParameter("pageNum") == null ? 1 : Integer.parseInt(request.getParameter("pageNum"));

        List<Customer> customerList = customerService.searchCustomers(searchKeyword, pageNum, pageSize);
        PaginatedResult<Customer> result = customerService.getCustomersByPage(searchKeyword, curPage, pageSize, customerList);
//        response.setContentType("application/json;charset=UTF-8");
//
//        try (PrintWriter out = response.getWriter()) {
//            out.write(new Gson().toJson(result)); // 使用 Gson 库将对象转为 JSON
//        }
        // 将结果转换为JSON返回给前端
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = mapper.writeValueAsString(result);
        response.getWriter().write(jsonResult);

//        System.out.println(jsonResult);
    }
}
