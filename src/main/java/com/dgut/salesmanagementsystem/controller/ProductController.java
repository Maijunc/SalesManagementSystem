package com.dgut.salesmanagementsystem.controller;

import com.dgut.salesmanagementsystem.pojo.PaginatedResult;
import com.dgut.salesmanagementsystem.pojo.Product;
import com.dgut.salesmanagementsystem.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/ProductController")
public class ProductController extends HttpServlet {
    private ProductService productService = new ProductService();
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
        if("ajax".equals(action)) {
            searchProductForAjax(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
    private void searchProductForAjax(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String searchKeyword = req.getParameter("searchKeyword");
        String pageParam = req.getParameter("pageNum");
        int curPage = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
        if(searchKeyword == null)
            searchKeyword = "";
        int pageNum = req.getParameter("pageNum") == null ? 1 : Integer.parseInt(req.getParameter("pageNum"));

        List<Product> productList = productService.searchProducts(searchKeyword, pageNum, pageSize);
        PaginatedResult<Product> result = productService.getProductsByPage(searchKeyword, curPage, pageSize, productList);

        // 将结果转换为JSON返回给前端
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = mapper.writeValueAsString(result);
        resp.getWriter().write(jsonResult);

//        System.out.println(jsonResult);
    }
}
