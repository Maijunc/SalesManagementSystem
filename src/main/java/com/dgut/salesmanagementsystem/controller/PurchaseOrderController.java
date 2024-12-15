package com.dgut.salesmanagementsystem.controller;

import com.dgut.salesmanagementsystem.pojo.PurchaseOrder;
import com.dgut.salesmanagementsystem.pojo.PurchaseOrderStatus;
import com.dgut.salesmanagementsystem.pojo.ShipOrder;
import com.dgut.salesmanagementsystem.service.PurchaseOrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/PurchaseOrderController")
public class PurchaseOrderController extends HttpServlet {
    private final PurchaseOrderService purchaseOrderService = new PurchaseOrderService();

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

        getPurchaseOrderListByPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if("confirm".equals(action)) {
            confirmPurchaseOrder(req, resp);
        }
    }

    private void confirmPurchaseOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        // 获取表单数据
        int purchaseOrderID = Integer.parseInt(req.getParameter("purchaseOrderID"));
        int actualQuantity = Integer.parseInt(req.getParameter("actualQuantity"));
        String purchaseOrderStatusStr = req.getParameter("purchaseOrderStatus");
        String supplierName = req.getParameter("supplierName");
        String notes = req.getParameter("notes");

        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderByID(purchaseOrderID);

        if (purchaseOrder == null) {
            System.err.println("confirmPurchaseOrder: 采购单不存在");
            return;
        }

        if ("completed".equals(purchaseOrder.getPurchaseOrderStatus().getValue())) {
            System.err.println("confirmPurchaseOrder: 采购单已完成, 无法再次确认");
            return;
        }

        purchaseOrder.setPurchaseOrderID(purchaseOrderID);
        purchaseOrder.setActualQuantity(actualQuantity);
        purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.COMPLETED);
        purchaseOrder.setSupplierName(supplierName);
        purchaseOrder.setNotes(notes);

        // 调用服务层更新采购单
        boolean updated = purchaseOrderService.confirmPurchaseOrder(purchaseOrder);
        if (!updated) {
            System.err.println("采购单更新失败");
        }

        resp.sendRedirect("../PurchaseOrderController?pageNum=1");
    }

    private void getPurchaseOrderListByPage(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        int pageNum = req.getParameter("pageNum") == null ? 1 : Integer.parseInt(req.getParameter("pageNum"));

        List<PurchaseOrder> purchaseOrderList = purchaseOrderService.getPurchaseOrdersByPage(pageNum, pageSize);
        // 获取总页数
        int totalPages = purchaseOrderService.getTotalPages(pageSize);
        // 设置分页相关属性
        HttpSession session = req.getSession();

        session.setAttribute("purchaseOrderList", purchaseOrderList);
        session.setAttribute("currentPage", pageNum);
        session.setAttribute("totalPages", totalPages);
        session.setAttribute("pageSize", pageSize);

        resp.sendRedirect("purchase_order/purchase_order_list.jsp");
    }

}
