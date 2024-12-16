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
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>仓库管理员 - 仪表盘</title>
  <!-- 引入Bootstrap CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f8f9fa;
      margin: 0;
      padding: 0;
    }
    .navbar {
      background-color: #4CAF50;
    }
    .navbar a {
      color: white;
      text-decoration: none;
      margin: 0 10px;
      font-size: 18px;
    }
    .navbar a:hover {
      text-decoration: underline;
    }
    .welcome {
      margin: 20px;
      text-align: center;
      color: #343a40;
    }
    .nav-container {
      display: flex;
      justify-content: center;
      margin-top: 20px;
    }
    .nav-item {
      list-style: none;
      margin: 0 10px;
    }
    .btn-primary {
      background-color: #4CAF50 !important;
      border-color: #4CAF50 !important;
    }
    .btn-primary:hover {
      background-color: #388E3C !important;
      border-color: #388E3C !important;
    }
  </style>
</head>
<body>
<!-- 顶部导航栏 -->
<nav class="navbar navbar-expand-lg navbar-dark">
  <div class="container-fluid">
    <span class="navbar-brand">仓库管理系统</span>
    <div>
      <a href="../logout.jsp">退出登录</a>
    </div>
  </div>
</nav>

<!-- 欢迎信息 -->
<div class="welcome">
  <h1>欢迎, 仓库管理员 <%= user.getUserName() %></h1>
</div>

<!-- 功能导航 -->
<div class="nav-container">
  <ul class="nav">
    <li class="nav-item">
      <a class="btn btn-primary" href="../ShipOrderController?pageNum=1">发货管理</a>
    </li>
    <li class="nav-item">
      <a class="btn btn-primary" href="../PurchaseOrderController?pageNum=1">进货管理</a>
    </li>
    <li class="nav-item">
      <a class="btn btn-primary" href="view_inventory.jsp" hidden>商品库存</a>
    </li>
  </ul>
</div>
</body>
</html>
