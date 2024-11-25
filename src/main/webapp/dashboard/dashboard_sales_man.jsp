<%@ page import="com.dgut.salesmanagementsystem.pojo.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"SalesMan".equals(user.getRole().getRole())) {
        response.sendRedirect("../login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>销售人员 - 仪表盘</title>
</head>
<body>
<%--<h1>欢迎, 销售人员 <%= user.getUsername() %></h1>--%>
<h1>欢迎, 销售人员</h1>
<nav>
    <a href="view_sales_performance.jsp">销售业绩</a>
    <a href="view_contracts.jsp">合同执行情况</a>
    <a href="logout.jsp">退出登录</a>
</nav>
</body>
</html>
