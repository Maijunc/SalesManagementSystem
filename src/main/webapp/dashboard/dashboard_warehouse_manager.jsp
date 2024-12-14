<%@ page import="com.dgut.salesmanagementsystem.pojo.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"WarehouseManager".equals(user.getRole().getRole())) {
    response.sendRedirect("../login.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title>仓库管理员 - 仪表盘</title>
</head>
<body>
<%--<h1>欢迎, 仓库管理员 <%= user.getUsername() %></h1>--%>
<h1>欢迎, 仓库管理员 </h1>
<nav>
  <a href="view_inventory.jsp">商品库存</a>
  <a href="../ShipOrderController?pageNum=1">发货管理</a>
  <a href="manage_purchase_orders.jsp">进货管理</a>
  <a href="../logout.jsp">退出登录</a>
</nav>
</body>
</html>
