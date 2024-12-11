package com.dgut.salesmanagementsystem.controller;

import com.dgut.salesmanagementsystem.pojo.*;
import com.dgut.salesmanagementsystem.service.PurchaseListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/PurchaseListController")
public class PurchaseListController extends HttpServlet {

    private final PurchaseListService purchaseListService = new PurchaseListService();

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
            searchRemainingProductsForAjax(req, resp);
        }
        else
            getPurchaseListsByContractID(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if("add".equals(action)) {
            addPurchaseList(req, resp);
        }
    }

    private void addPurchaseList(HttpServletRequest req, HttpServletResponse resp) {
        int contractID;
        try {
            contractID = Integer.parseInt(req.getParameter("contractID"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    // 传入pageNum和contractID属性，需要contractID, contractName, purchaseLists, pageSize, currentPage 和 totalPages 传进session里面
    void getPurchaseListsByContractID(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int pageNum = req.getParameter("pageNum") == null ? 1 : Integer.parseInt(req.getParameter("pageNum"));
        // 传递一下contractName用于告诉用户这是哪个合同的采购清单
        String contractName = req.getParameter("contractName");
        int contractID;
        try {
            contractID = Integer.parseInt(req.getParameter("contractID"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        List<PurchaseList> purchaseLists = purchaseListService.getPurchaseListsByContractID(contractID, pageNum, pageSize);
        // 获取总页数
        int totalPages = purchaseListService.getTotalPages(contractID, pageSize);
        // 设置分页相关属性
        HttpSession session = req.getSession();
        session.setAttribute("contractID", contractID);
        session.setAttribute("contractName", contractName);
        session.setAttribute("purchaseLists", purchaseLists);
        session.setAttribute("currentPage", pageNum);
        session.setAttribute("totalPages", totalPages);
        session.setAttribute("pageSize", pageSize);

        resp.sendRedirect("contract/purchaseList_management.jsp");
    }

    private void searchRemainingProductsForAjax(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String searchKeyword = req.getParameter("searchKeyword");
        String pageParam = req.getParameter("pageNum");
        int curPage = (pageParam != null) ? Integer.parseInt(pageParam) : 1;
        if(searchKeyword == null)
            searchKeyword = "";
        int pageNum = req.getParameter("pageNum") == null ? 1 : Integer.parseInt(req.getParameter("pageNum"));
        // 合同ID
        int contractID;
        try {
            contractID = Integer.parseInt(req.getParameter("contractID"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        List<RemainingProduct> remainingProductList = purchaseListService.searchRemainingProducts(searchKeyword, pageNum, pageSize, contractID);
        PaginatedResult<RemainingProduct> result = purchaseListService.getPaginatedResult(curPage, pageSize, remainingProductList, contractID);

        // 将结果转换为JSON返回给前端
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = mapper.writeValueAsString(result);
        resp.getWriter().write(jsonResult);

        System.out.println(jsonResult);
    }
}
