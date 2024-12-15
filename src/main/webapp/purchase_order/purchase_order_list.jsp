<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.PurchaseOrder" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.User" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"WarehouseManager".equals(user.getRole().getRole())) {
    response.sendRedirect("../login.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>进货单管理</title>
  <link rel="stylesheet" href="../css/purchase-order-list.css">
</head>
<body>
<header class="page-header">
  <a href="../dashboard/dashboard_warehouse_manager.jsp" class="back-link">返回</a>
  <h1>进货单管理</h1>
</header>

<main class="main-content">

  <!-- 进货单列表 -->
  <table class="purchase-order-table">
    <thead>
    <tr>
      <th>序号</th>
      <th>创建时间</th>
      <th>商品名称</th>
      <th>需要数量</th>
      <th>实际到货数量</th>
      <th>状态</th>
      <th>供应商</th>
      <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%
      List<PurchaseOrder> purchaseOrderList = (List<PurchaseOrder>) session.getAttribute("purchaseOrderList");

      Integer pageSizeObj = (Integer) session.getAttribute("pageSize");
      Integer currentPageObj = (Integer) session.getAttribute("currentPage");
      Integer totalPagesObj = (Integer) session.getAttribute("totalPages");
      int currentPage = (currentPageObj != null) ? currentPageObj : 1;
      int totalPages = (totalPagesObj != null) ? totalPagesObj : 1;
      int pageSize = (pageSizeObj != null) ? pageSizeObj : 6;
      int cnt = 0;
      if (purchaseOrderList != null && !purchaseOrderList.isEmpty()) {
        for (PurchaseOrder purchaseOrder : purchaseOrderList) {
          Timestamp createdAt = purchaseOrder.getCreatedAt();
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String formattedDate = (createdAt != null) ? sdf.format(createdAt) : "未知";
          cnt++;
    %>
    <tr>
      <td><%= (currentPage - 1) * pageSize + cnt %></td>
      <td><%= formattedDate %></td>
      <td><%= purchaseOrder.getProductName() %></td>
      <td><%= purchaseOrder.getRequiredQuantity() %></td>
      <td><%= purchaseOrder.getActualQuantity() %></td>
      <td><%= purchaseOrder.getPurchaseOrderStatus().getValue().equals("pending") ? "待处理" : "已完成" %></td>
      <td><%= purchaseOrder.getSupplierName() %></td>
      <td class="action-buttons">
        <a href="purchase_order_view.jsp?purchaseOrderID=<%= purchaseOrder.getPurchaseOrderID() %>">详情</a>
      </td>
    </tr>
    <%
      }
    } else {
    %>
    <tr>
      <td colspan="8">暂无进货单信息</td>
    </tr>
    <% } %>
    </tbody>
  </table>

  <!-- 分页 -->
  <div class="pagination">
    <a href="../PurchaseOrderController?pageNum=<%= currentPage - 1 %>"
            <%= (currentPage == 1) ? "style='pointer-events: none; color: gray;'" : "" %>>上一页</a>
    第 <%= currentPage %> 页 / 共 <%= totalPages %> 页
    <a href="../PurchaseOrderController?pageNum=<%= currentPage + 1 %>"
            <%= (currentPage == totalPages) ? "style='pointer-events: none; color: gray;'" : "" %>>下一页</a>
  </div>
</main>

</body>
</html>
