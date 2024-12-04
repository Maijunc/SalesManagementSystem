package com.dgut.salesmanagementsystem.controller;

import com.dgut.salesmanagementsystem.pojo.Customer;
import com.dgut.salesmanagementsystem.pojo.Salesman;
import com.dgut.salesmanagementsystem.service.SalesmanService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/SalesmanController")
public class SalesmanController extends HttpServlet {
    private SalesmanService salesService = new SalesmanService();

    private int pageSize;
    @Override
    public void init() throws ServletException {
        pageSize = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addSalesman(request, response);
        } else if ("edit".equals(action)) {
            editSalesman(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            deleteSalesman(request, response);
        } else {
            searchSalesman(request, response);
        }
    }

    public void searchSalesman(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String searchKeyword = request.getParameter("searchKeyword");
        if(searchKeyword == null)
            searchKeyword = "";
        int pageNum = request.getParameter("pageNum") == null ? 1 : Integer.parseInt(request.getParameter("pageNum"));

        List<Salesman> salesmanList = salesService.searchSalesmen(searchKeyword, pageNum, pageSize);
        // 获取总记录数以计算总页数
        int totalPages = salesService.getTotalPages(searchKeyword, pageSize);
        // 设置分页相关属性
        HttpSession session = request.getSession();
        session.setAttribute("salesmanList", salesmanList);
        session.setAttribute("currentPage", pageNum);
        session.setAttribute("totalPages", totalPages);
        session.setAttribute("searchKeyword", searchKeyword);

        response.sendRedirect("salesman/salesman_management.jsp");
    }

    private void addSalesman(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        BigDecimal totalSales = new BigDecimal(request.getParameter("totalSales"));
        BigDecimal commission = new BigDecimal(request.getParameter("commission"));

        salesService.addSalesman(name, email, phone, totalSales, commission);
        response.sendRedirect("SalesmanController?pageNum=1");
    }

    private void editSalesman(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int salesmanID = Integer.parseInt(request.getParameter("salesmanID"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        BigDecimal totalSales = new BigDecimal(request.getParameter("totalSales"));
        BigDecimal commission = new BigDecimal(request.getParameter("commission"));

        salesService.updateSalesman(salesmanID, name, email, phone, totalSales, commission);
        int pageNum = request.getParameter("pageNum") == null ? 1 : Integer.parseInt(request.getParameter("pageNum"));
        String searchKeyword = request.getParameter("searchKeyword");
        response.sendRedirect("SalesmanController?pageNum=" + pageNum + "&searchKeyword=" + searchKeyword);
    }

    private void deleteSalesman(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int salesmanID = Integer.parseInt(request.getParameter("salesmanID"));
        salesService.deleteSalesman(salesmanID);
        response.sendRedirect("salesman/salesman_management.jsp");
    }

    public Salesman getSalesmanById(int salesmanID) {
        return salesService.getSalesmanById(salesmanID);
    }

    public List<Salesman> getAllSalesmen() {
        return salesService.getAllSalesmen();
    }
}
