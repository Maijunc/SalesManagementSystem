package com.dgut.salesmanagementsystem.controller;

import com.dgut.salesmanagementsystem.pojo.*;
import com.dgut.salesmanagementsystem.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/ShipOrderController")
public class ShipOrderController extends HttpServlet {
    private ShipOrderService shipOrderService = new ShipOrderService();
    private PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
    private PurchaseListService purchaseListService = new PurchaseListService();
    private ProductService productService = new ProductService();
    private ContractService contractService = new ContractService();
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
        } else {
            getShipOrderListByPage(req, resp);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("create".equals(action)) {
            createShipOrder(req, resp);
        } else if("updateLogistics".equals(action)) {
            updateLogistics(req, resp);
        }
    }

    // 确认发货 此前已经检查过库存情况
    private void updateLogistics(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取请求参数
        int shipOrderID = Integer.parseInt(request.getParameter("shipOrderID"));
        String shippingCompany = request.getParameter("shippingCompany");
        String trackingNumber = request.getParameter("trackingNumber");
        String notes = request.getParameter("notes");
        String shippedBy = request.getParameter("shippedBy");

        // 检查参数合法性
        if (shippingCompany == null || shippingCompany.trim().isEmpty() ||
                trackingNumber == null || trackingNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("物流公司和物流单号不能为空");
        }

        // 更新数据库中的发货单
        ShipOrder shipOrder = shipOrderService.getShipOrderById(shipOrderID);
        if (shipOrder == null) {
            throw new IllegalArgumentException("找不到指定的发货单");
        }

        // 获取商品信息
        Product product = productService.getProductByID(shipOrder.getProductID());
        if (product == null) {
            throw new IllegalArgumentException("商品不存在");
        }

        int availableStock = product.getStockQuantity();
        int requiredQuantity = shipOrder.getQuantity();

        // 检查库存是否足够
        if (availableStock < requiredQuantity) {
            response.getWriter().write("<script>alert('库存不足，无法完成发货，请先补充库存！'); history.back();</script>");
            return;
        }

        // 减少库存
        boolean stockReduced = productService.reduceStock(product.getProductID(), requiredQuantity);
        if (!stockReduced) {
            throw new IllegalStateException("更新库存失败，发货未完成");
        }

        // 设置更新的值
        shipOrder.setShippingCompany(shippingCompany);
        shipOrder.setTrackingNumber(trackingNumber);
        shipOrder.setNotes(notes);
        shipOrder.setShipDate(new Timestamp(System.currentTimeMillis()));
        shipOrder.setShipOrderStatus(ShipOrderStatus.SHIPPED);
        shipOrder.setShippedBy(shippedBy);

        // 更新数据库中的发货单
        boolean success = shipOrderService.updateShipOrder(shipOrder);
        if (!success) {
            System.err.println("更新发货单失败");
        }

        // 更新合同履行状态
        PurchaseList purchaseList = purchaseListService.getPurchaseListByID(shipOrder.getPurchaseListID());

        Contract contract = contractService.getContractByID(purchaseList.getContractID());
        if (contract != null) {
            // 检查合同下所有商品的发货情况
            boolean allShipped = contractService.checkIfAllItemsShipped(contract.getContractID());
            // 如果商品都已发货完毕
            if (allShipped && contract.getPaidAmount().compareTo(contract.getTotalAmount()) == 0) {
                // 如果所有商品都已发货完毕，更新合同的履行状态为 "completed"
                boolean contractUpdated = contractService.updateContractStatus(contract.getContractID(), ContractStatus.COMPLETED);
                contractUpdated = contractUpdated & contractService.setEndDate(contract.getContractID(), LocalDateTime.now());
                if (!contractUpdated) {
                    System.err.println("更新合同履行状态失败");
                }
            }
        }

        // 更新成功，重定向到发货单详情页面
        response.sendRedirect("ship_order/ship_order_view.jsp?shipOrderID=" + shipOrderID);
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
        shipOrder.setShipOrderStatus(ShipOrderStatus.PENDING);
        shipOrder.setNotes(notes);

        // 检查库存是否满足条件
        Product product = productService.getProductByID(shipOrder.getProductID());
        int availableStock = product.getStockQuantity();
        int requiredQuantity = shipOrder.getQuantity();
        int lowStockThreshold = product.getLowStockThreshold();

        if (availableStock < requiredQuantity || (availableStock - requiredQuantity) < lowStockThreshold) {

            // 检查是否已经生成过进货单
            if (!shipOrder.isPurchaseOrderGenerated()) {
                // 生成进货单
                PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
                purchaseOrderService.checkAndCreatePurchaseOrder(shipOrder.getProductID(), requiredQuantity);

                // 更新发货单的状态为已生成进货单
                shipOrder.setPurchaseOrderGenerated(true);
                shipOrderService.updateShipOrder(shipOrder);
            }
        }

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

    private void getShipOrderListByPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int pageNum = req.getParameter("pageNum") == null ? 1 : Integer.parseInt(req.getParameter("pageNum"));

        List<ShipOrder> shipOrderList = shipOrderService.getShipOrderListByPage(pageNum, pageSize);
        // 获取总页数
        int totalPages = shipOrderService.getTotalPages(pageSize);
        // 设置分页相关属性
        HttpSession session = req.getSession();

        session.setAttribute("shipOrderList", shipOrderList);
        session.setAttribute("currentPage", pageNum);
        session.setAttribute("totalPages", totalPages);
        session.setAttribute("pageSize", pageSize);

        resp.sendRedirect("ship_order/ship_order_list.jsp");
    }
}
