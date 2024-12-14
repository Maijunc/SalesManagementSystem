<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.ShipOrder" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.ShipOrderStatus" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>
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
  <title>发货单管理</title>
  <link rel="stylesheet" href="../css/ship-order.css">
</head>
<body>
<header class="page-header">
  <a href="dashboard_warehouse_manager.jsp" class="back-link">返回</a>
  <h1>发货单管理</h1>
</header>

<main class="main-content">
  <!-- 操作按钮 -->
  <div class="actions">
    <a href="add_ship_order.jsp">
      <button type="button">新增发货单</button>
    </a>
  </div>

  <!-- 发货单列表 -->
  <table class="ship-order-table">
    <thead>
    <tr>
      <th>序号</th>
      <th>发货日期</th>
      <th>商品名称</th>
      <th>数量</th>
      <th>总金额</th>
      <th>状态</th>
      <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%
      List<ShipOrder> shipOrderList = (List<ShipOrder>) session.getAttribute("shipOrderList");

      Integer pageSizeObj = (Integer) session.getAttribute("pageSize");
      Integer currentPageObj = (Integer) session.getAttribute("currentPage");
      Integer totalPagesObj = (Integer) session.getAttribute("totalPages");
      int currentPage = (currentPageObj != null) ? currentPageObj : 1;
      int totalPages = (totalPagesObj != null) ? totalPagesObj : 1;
      int pageSize = (pageSizeObj != null) ? pageSizeObj : 6;
      int cnt = 0;
      if (shipOrderList != null && !shipOrderList.isEmpty()) {
        for (ShipOrder shipOrder : shipOrderList) {
          Timestamp shipDate = shipOrder.getShipDate();
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String formattedDate = (shipDate != null) ? sdf.format(shipDate) : "未发货";
          cnt++;
    %>
    <tr>
      <td><%= (currentPage - 1) * pageSize + cnt %></td>
      <td><%= formattedDate %></td>
      <td><%= shipOrder.getProductName() %></td>
      <td><%= shipOrder.getQuantity() %></td>
      <td><%= shipOrder.getTotalAmount() %></td>
      <td><%= ShipOrderStatus.getChineseStr(shipOrder.getShipOrderStatus().getValue()) %></td>
      <td class="action-buttons">
        <a href="ship_order_view.jsp?shipOrderID=<%= shipOrder.getShipOrderID() %>">详情</a>
      </td>
    </tr>
    <%
      }
    } else {
    %>
    <tr>
      <td colspan="7">暂无发货单信息</td>
    </tr>
    <% } %>
    </tbody>
  </table>

  <!-- 分页 -->
  <div class="pagination">
    <a href="../ShipOrderController?pageNum=<%= currentPage - 1 %>"
            <%= (currentPage == 1) ? "style='pointer-events: none; color: gray;'" : "" %>>上一页</a>
    第 <%= currentPage %> 页 / 共 <%= totalPages %> 页
    <a href="../ShipOrderController?pageNum=<%= currentPage + 1 %>"
            <%= (currentPage == totalPages) ? "style='pointer-events: none; color: gray;'" : "" %>>下一页</a>
  </div>
</main>

</body>
</html>
