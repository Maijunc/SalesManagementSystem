<%@ page import="com.dgut.salesmanagementsystem.pojo.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"SalesManager".equals(user.getRole().getRole())) {
    response.sendRedirect("../login.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html>
<head>
  <title>销售管理员 - 仪表盘</title>
</head>
<body>
<%--<h1>欢迎, 销售管理员 <%= user.getUsername() %></h1>--%>
<h1>欢迎, 销售管理员</h1>
<nav>
  <a href="../CustomerController?pageNum=1&searchKeyword=">客户管理</a>
  <a href="../ContractController?pageNum=1">合同管理</a>
  <a href="../contract/manage_contracts.jsp">销售人员管理</a>
  <a href="view_sales_statistics.jsp">销售统计</a>
  <a href="../logout.jsp">退出登录</a>
</nav>
</body>

</html>
