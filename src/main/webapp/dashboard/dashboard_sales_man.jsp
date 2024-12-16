<%@ page import="com.dgut.salesmanagementsystem.pojo.User" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.Salesman" %>
<%@ page import="com.dgut.salesmanagementsystem.service.SalesmanService" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"SalesMan".equals(user.getRole().getRole())) {
        response.sendRedirect("../login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>销售人员 - 仪表盘</title>
    <!-- 引入Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #fafafa;
            margin: 0;
            padding: 0;
        }
        .navbar {
            background-color: #0078D4; /* 修改导航栏背景色 */
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
            color: #212529;
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
            background-color: #0078D4 !important; /* 修改按钮背景色 */
            border-color: #0078D4 !important; /* 修改按钮边框色 */
        }
        .btn-primary:hover {
            background-color: #005A99 !important; /* 修改按钮悬停背景色 */
            border-color: #005A99 !important; /* 修改按钮悬停边框色 */
        }
    </style>
</head>
<body>
<!-- 顶部导航栏 -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid">
        <span class="navbar-brand">销售管理系统</span>
        <div>
            <a href="../logout.jsp">退出登录</a>
        </div>
    </div>
</nav>

<!-- 欢迎信息 -->
<div class="welcome">
    <h1>欢迎, 销售人员 <%= user.getUserName() %></h1>
</div>
<%
    SalesmanService salesmanService = new SalesmanService();
    Salesman salesman = salesmanService.getSalesmanByName(user.getUserName());

    int salesmanID = salesman.getSalesmanID();
%>
<!-- 功能导航 -->
<div class="nav-container">
    <ul class="nav">
        <li class="nav-item">
            <a class="btn btn-primary" href="../SalesmanController?action=salesPerformance&salesmanID=<%=salesmanID%>">销售业绩</a>
        </li>
        <li class="nav-item">
            <a class="btn btn-primary" href="../ContractController?pageNum=1&action=salesman&salesmanID=<%=salesmanID%>">合同执行情况</a>
        </li>
    </ul>
</div>
</body>
</html>
