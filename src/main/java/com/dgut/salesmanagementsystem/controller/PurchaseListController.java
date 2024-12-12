package com.dgut.salesmanagementsystem.controller;

import com.dgut.salesmanagementsystem.pojo.*;
import com.dgut.salesmanagementsystem.service.ContractService;
import com.dgut.salesmanagementsystem.service.CustomerService;
import com.dgut.salesmanagementsystem.service.PurchaseListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/PurchaseListController")
public class PurchaseListController extends HttpServlet {

    private final PurchaseListService purchaseListService = new PurchaseListService();
    private final ContractService contractService = new ContractService();
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
        } else if("pay".equals(action)) {
            payForPurchaseList(req, resp);
        } else if("details".equals(action)) {

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

    private void addPurchaseList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int contractID;
        try {
            contractID = Integer.parseInt(req.getParameter("contractID"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Map<String, String[]> parameterMap = req.getParameterMap();
        List<PurchaseListItem> purchaseListItems = new ArrayList<>();
        Map<Integer, Integer> indexMap = new HashMap<>(); // 存储 index 与在 purchaseListItems 中的下标映射
        // 遍历所有参数，提取商品数据
        parameterMap.forEach((key, value) -> {
            if (key.startsWith("products[")) { // 检查是否是商品参数
                // 提取索引
                String indexStr = key.substring(key.indexOf("[") + 1, key.indexOf("]"));
                int index = Integer.parseInt(indexStr);
                // 如果此 index 没有出现过，扩充 contractItems 的容量并更新 indexMap
                if (!indexMap.containsKey(index)) {
                    indexMap.put(index, purchaseListItems.size());
                    purchaseListItems.add(new PurchaseListItem()); // 添加新的 ContractItem
                }

                PurchaseListItem purchaseListItem = purchaseListItems.get(indexMap.get(index));

                // 填充商品属性
                if (key.endsWith(".productID")) {
                    try {
                        purchaseListItem.setProductID(Integer.parseInt(value[0]));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else if (key.endsWith(".productName")) {
                    purchaseListItem.setProductName(value[0]);
                } else if (key.endsWith(".quantity")) {
                    try {
                        purchaseListItem.setQuantity(Integer.parseInt(value[0]));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else if (key.endsWith(".unitPrice")) {
                    purchaseListItem.setUnitPrice(new BigDecimal(value[0]));
                }
            }
        });
        purchaseListService.addPurchaseList(contractID, purchaseListItems);
        String contractName = contractService.getContractName(contractID);
        resp.sendRedirect("../PurchaseListController?PageNum=1&contractID=" + contractID + "&contractName=" + contractName);
    }

    // 传入pageNum和contractID属性，需要contractID, contractName, purchaseLists, pageSize, currentPage 和 totalPages 传进session里面
    void getPurchaseListsByContractID(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int pageNum = req.getParameter("pageNum") == null ? 1 : Integer.parseInt(req.getParameter("pageNum"));
        // 传递一下contractName用于告诉用户这是哪个合同的采购清单
        int contractID;
        try {
            contractID = Integer.parseInt(req.getParameter("contractID"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String contractName = contractService.getContractName(contractID);
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

    // 为采购清单付款
    private void payForPurchaseList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int purchaseListID;
        try {
            purchaseListID = Integer.parseInt(req.getParameter("purchaseListID"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // 传递一下contractName用于告诉用户这是哪个合同的采购清单
        HttpSession session = req.getSession();
        String contractName = (String) session.getAttribute("contractName");
        int contractID = (int) session.getAttribute("contractID");
        int pageNum = (int) session.getAttribute("currentPage");


        purchaseListService.payForPurchaseList(purchaseListID);
        resp.sendRedirect("PurchaseListController?" + "contractID=" + contractID + "&pageNum=" + pageNum);
    }
}
