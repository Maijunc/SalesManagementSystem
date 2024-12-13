package com.dgut.salesmanagementsystem.controller;

import com.dgut.salesmanagementsystem.pojo.ShipOrder;
import com.dgut.salesmanagementsystem.service.ShipOrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet("/ShipOrderController")
public class ShipOrderController extends HttpServlet {
    private ShipOrderService shipOrderService = new ShipOrderService();
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

        if("checkExists".equals(action)) {
            checkShipOrderExist(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("create".equals(action)) {
            createShipOrder(req, resp);
        }
    }

    private void createShipOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        // 从表单中获取参数
        String productName = req.getParameter("productName");
        int productID = Integer.parseInt(req.getParameter("productID"));
        int purchaseListID = Integer.parseInt(req.getParameter("purchaseListID"));
        int purchaseListItemID = Integer.parseInt(req.getParameter("purchaseListItemID"));
        BigDecimal unitPrice = new BigDecimal(req.getParameter("unitPrice"));
        int customerID = Integer.parseInt(req.getParameter("customerID"));
        String customerName = req.getParameter("customerName");
        BigDecimal totalAmount = new BigDecimal(req.getParameter("totalAmount"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        // 获取其他用户输入的字段
        String recipientName = req.getParameter("recipientName");
        String recipientPhone = req.getParameter("recipientPhone");
        String shippingAddress = req.getParameter("shippingAddress");
        String notes = req.getParameter("notes");

        // 创建 ShipOrder 对象并设置属性
        ShipOrder shipOrder = new ShipOrder();
        shipOrder.setProductName(productName);
        shipOrder.setProductID(productID);
        shipOrder.setPurchaseListID(purchaseListID);
        shipOrder.setPurchaseListItemID(purchaseListItemID);
        shipOrder.setUnitPrice(unitPrice);
        shipOrder.setCustomerID(customerID);
        shipOrder.setCustomerName(customerName);
        shipOrder.setTotalAmount(totalAmount);
        shipOrder.setQuantity(quantity);
        shipOrder.setRecipientName(recipientName);
        shipOrder.setRecipientPhone(recipientPhone);
        shipOrder.setShippingAddress(shippingAddress);
        shipOrder.setNotes(notes);

        shipOrderService.createShipOrder(shipOrder);

        resp.sendRedirect("contract/purchaseList_view.jsp?purchaseListID=" + purchaseListID);
    }

    private void checkShipOrderExist(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int purchaseListItemID = Integer.parseInt(req.getParameter("purchaseListItemID"));
        boolean exists = shipOrderService.checkIfShipOrderExists(purchaseListItemID);

        // 发送JSON响应
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print("{\"exists\": " + exists + "}");
        out.flush();
    }
}
