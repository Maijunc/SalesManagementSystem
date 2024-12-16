<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dgut.salesmanagementsystem.pojo.ShipOrder" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.ShipOrderStatus" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.User" %>
<%@ page import="com.dgut.salesmanagementsystem.service.ShipOrderService" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.Product" %>
<%@ page import="com.dgut.salesmanagementsystem.service.ProductService" %>
<%@ page import="com.dgut.salesmanagementsystem.service.PurchaseOrderService" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"WarehouseManager".equals(user.getRole().getRole())) {
        response.sendRedirect("../login.jsp");
        return;
    }

    int shipOrderID = Integer.parseInt(request.getParameter("shipOrderID"));

    ShipOrderService shipOrderService = new ShipOrderService();
    ProductService productService = new ProductService();
    ShipOrder shipOrder = shipOrderService.getShipOrderById(shipOrderID);

    if (shipOrder == null) {
        out.print("<script>alert('无法加载发货单详情，请重试！'); history.back();</script>");
        return;
    }

    boolean canShip = true;
    String errorMessage = null;

    if(shipOrder.getShipOrderStatus() == ShipOrderStatus.PENDING) {
        // 检查库存是否满足条件
        Product product = productService.getProductByID(shipOrder.getProductID());
        int availableStock = product.getStockQuantity();
        int requiredQuantity = shipOrder.getQuantity();
        int lowStockThreshold = product.getLowStockThreshold();

        if (availableStock < requiredQuantity || (availableStock - requiredQuantity) < lowStockThreshold) {
            canShip = false;

            // 检查是否已经生成过进货单
            if (!shipOrder.isPurchaseOrderGenerated()) {
                // 生成进货单
                PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
                purchaseOrderService.checkAndCreatePurchaseOrder(shipOrder.getProductID(), requiredQuantity);

                // 更新发货单的状态为已生成进货单
                shipOrder.setPurchaseOrderGenerated(true);
                shipOrderService.updateShipOrder(shipOrder);
            }

            // 设置提示信息
            errorMessage = "库存不足，已生成进货单，请先完成进货再确认发货。";
        }
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>发货单详情</title>
    <link rel="stylesheet" href="../css/ship-order-view.css">
</head>
<body>
<header class="page-header">
    <a href="../ShipOrderController?pageNum=1" class="back-link">返回</a>
    <h1>发货单详情</h1>
</header>

<main class="main-content">
    <!-- 发货单基本信息 -->
    <section class="ship-order-info">
        <h2>发货单信息</h2>
        <table class="detail-table">
            <tr>
                <th>发货单编号</th>
                <td><%= shipOrder.getShipOrderID() %></td>
            </tr>
            <tr>
                <th>商品名称</th>
                <td><%= shipOrder.getProductName() %></td>
            </tr>
            <tr>
                <th>商品单价</th>
                <td><%= shipOrder.getUnitPrice() %></td>
            </tr>
            <tr>
                <th>数量</th>
                <td><%= shipOrder.getQuantity() %></td>
            </tr>
            <tr>
                <th>总金额</th>
                <td><%= shipOrder.getTotalAmount() %></td>
            </tr>
            <tr>
                <th>发货状态</th>
                <td><%= ShipOrderStatus.getChineseStr(shipOrder.getShipOrderStatus().getValue()) %></td>
            </tr>
            <tr>
                <th>发货日期</th>
                <td><%= shipOrder.getShipDate() != null ? sdf.format(shipOrder.getShipDate()) : "未发货" %></td>
            </tr>
            <tr>
                <th>客户名称</th>
                <td><%= shipOrder.getCustomerName() %></td>
            </tr>
            <tr>
                <th>收货人</th>
                <td><%= shipOrder.getRecipientName() %></td>
            </tr>
            <tr>
                <th>收货地址</th>
                <td><%= shipOrder.getShippingAddress() %></td>
            </tr>
            <tr>
                <th>收货人电话</th>
                <td><%= shipOrder.getRecipientPhone() %></td>
            </tr>
        </table>
    </section>

    <!-- 物流信息表单 -->
    <section class="logistics-info">
        <h2>物流信息</h2>
        <form action="../ShipOrderController" method="post">
            <input type="hidden" name="action" value="updateLogistics">
            <input type="hidden" name="shipOrderID" value="<%= shipOrder.getShipOrderID() %>">
            <input type="hidden" name="shippedBy" value="<%=user.getUserName()%>">
            <table class="form-table">
                <tr>
                    <th>物流公司</th>
                    <td><input type="text" name="shippingCompany" value="<%= shipOrder.getShippingCompany() != null ? shipOrder.getShippingCompany() : "" %>" required></td>
                </tr>
                <tr>
                    <th>物流单号</th>
                    <td><input type="text" name="trackingNumber" value="<%= shipOrder.getTrackingNumber() != null ? shipOrder.getTrackingNumber() : "" %>" required></td>
                </tr>
                <tr>
                    <th>备注</th>
                    <td><textarea name="notes" rows="3"><%= shipOrder.getNotes() != null ? shipOrder.getNotes() : "" %></textarea></td>
                </tr>
            </table>
            <div class="form-actions">
                <% if (canShip && ShipOrderStatus.PENDING.equals(shipOrder.getShipOrderStatus())) { %>
                <button type="submit" onclick="return confirm('确认更新物流信息并完成发货吗？');">确认发货</button>
                <% } else { %>
                <button type="button" disabled><%= errorMessage != null ? "库存不足" : "已发货" %></button>
                <% } %>
            </div>

            <% if (errorMessage != null) { %>
            <div class="error-message">
                <%= errorMessage %>
            </div>
            <% } %>


        </form>
    </section>
</main>

</body>
</html>
