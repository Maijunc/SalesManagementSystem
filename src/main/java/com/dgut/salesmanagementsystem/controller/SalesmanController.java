package com.dgut.salesmanagementsystem.controller;

import com.dgut.salesmanagementsystem.pojo.PaginatedResult;
import com.dgut.salesmanagementsystem.pojo.Role;
import com.dgut.salesmanagementsystem.pojo.Salesman;
import com.dgut.salesmanagementsystem.service.SalesmanService;
import com.dgut.salesmanagementsystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(value = "/SalesmanController")
public class SalesmanController extends HttpServlet {
    private final SalesmanService salesmanService = new SalesmanService();
    private final UserService userService = new UserService();
    private int pageSize;

    @Override
    public void init() throws ServletException {
        super.init();
        // 在这里获取Servlet上下文参数
        this.pageSize = Integer.parseInt(getServletContext().getInitParameter("pageSize"));
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
        } else if("ajax".equals(action)){
            searchSalesmanForAjax(request, response);
        } else if("salesPerformance".equals(action)) {
            getSalesPerformance(request, response);
        }else {
            searchSalesman(request, response);
        }
    }

    private void getSalesPerformance(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        int salesmanID = Integer.parseInt(request.getParameter("salesmanID"));
        String startTimeStr = request.getParameter("startDate");
        String endTimeStr = request.getParameter("endDate");

        BigDecimal salesAmount = salesmanService.getSalesAmountBySalesmanAndDateRange(salesmanID, startTimeStr, endTimeStr);

        // 将销售额传递到JSP
        String salesAmountStr = null;
        if(salesAmount != null)
            salesAmountStr = salesAmount.toString();

        response.sendRedirect("salesman/sales_performance.jsp?salesAmount=" + salesAmountStr);
    }

    public void searchSalesmanForAjax(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String searchKeyword = request.getParameter("searchKeyword");
        String pageParam = request.getParameter("pageNum");
        int curPage = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
        if(searchKeyword == null)
            searchKeyword = "";
        int pageNum = request.getParameter("pageNum") == null ? 1 : Integer.parseInt(request.getParameter("pageNum"));

        List<Salesman> salesmanList = salesmanService.searchSalesmen(searchKeyword, pageNum, pageSize);
        PaginatedResult<Salesman> result = salesmanService.getSalesmenByPage(searchKeyword, curPage, pageSize, salesmanList);
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

    public void searchSalesman(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String searchKeyword = request.getParameter("searchKeyword");
        if(searchKeyword == null)
            searchKeyword = "";
        int pageNum = request.getParameter("pageNum") == null ? 1 : Integer.parseInt(request.getParameter("pageNum"));

        List<Salesman> salesmanList = salesmanService.searchSalesmen(searchKeyword, pageNum, pageSize);
        // 获取总页数
        int totalPages = salesmanService.getTotalPages(searchKeyword, pageSize);
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

        String defaultPassword = "123456";
        // 添加一个名字为xxx的user
        userService.addUser(name, defaultPassword, Role.SALES_MAN);

        salesmanService.addSalesman(name, email, phone, totalSales, commission);

        response.sendRedirect("SalesmanController?pageNum=1");
    }

    private void editSalesman(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int salesmanID = Integer.parseInt(request.getParameter("salesmanID"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        BigDecimal totalSales = new BigDecimal(request.getParameter("totalSales"));
        BigDecimal commission = new BigDecimal(request.getParameter("commission"));

        salesmanService.updateSalesman(salesmanID, name, email, phone, totalSales, commission);
        int pageNum = request.getParameter("pageNum") == null ? 1 : Integer.parseInt(request.getParameter("pageNum"));
        String searchKeyword = request.getParameter("searchKeyword");
        response.sendRedirect("SalesmanController?pageNum=" + pageNum + "&searchKeyword=" + searchKeyword);
    }

    private void deleteSalesman(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int salesmanID = Integer.parseInt(request.getParameter("salesmanID"));
        salesmanService.deleteSalesman(salesmanID);
        response.sendRedirect("salesman/salesman_management.jsp");
    }

    public Salesman getSalesmanById(int salesmanID) {
        return salesmanService.getSalesmanById(salesmanID);
    }

    private List<Salesman> getAllSalesmen() {
        return salesmanService.getAllSalesmen();
    }

    public List<Salesman> getPageSalesmen(String searchKeyword, int pageNum){
        return salesmanService.searchSalesmen(searchKeyword, pageNum, pageSize);
    }
}
