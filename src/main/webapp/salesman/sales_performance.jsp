<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.Salesman" %>
<%@ page import="com.dgut.salesmanagementsystem.service.SalesmanService" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"SalesMan".equals(user.getRole().getRole())) {
    response.sendRedirect("../login.jsp");
    return;
  }

  SalesmanService salesmanService = new SalesmanService();
  Salesman salesman = salesmanService.getSalesmanByName(user.getUserName());

  int salesmanID = salesman.getSalesmanID();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>销售业绩查询</title>
  <link rel="stylesheet" href="../css/sales-performance.css">
</head>
<body class="sales-performance-page">
<div class="container">
  <h1 class="page-title">查询销售业绩</h1>

  <form action="../SalesmanController" method="get" onsubmit="return validateForm()" class="sales-performance-form">
    <input type="hidden" name="action" value="salesPerformance">
    <input type="hidden" name="salesmanID" value="<%=salesmanID%>">

    <label for="startDate" class="form-label">开始日期:</label>
    <input type="date" id="startDate" name="startDate" class="form-input">

    <label for="endDate" class="form-label">结束日期:</label>
    <input type="date" id="endDate" name="endDate" class="form-input">

    <button type="submit" class="submit-btn">查询</button>
  </form>

  <%
    // 获取请求参数
    String salesAmount = request.getParameter("salesAmount");

    // 如果salesAmount是字符串 "null"，将其处理为 null
    if ("null".equals(salesAmount)) {
      salesAmount = null;
    }

    // 判断是否有销售额数据
    if (salesAmount != null && !salesAmount.isEmpty()) {
  %>
  <h2 class="sales-amount">销售业绩: <%= salesAmount %></h2>
  <%
  } else {
  %>
  <h2 class="no-data">没有找到相关数据。</h2>
  <%
    }
  %>

  <!-- 返回按钮 -->
  <br><br>
  <button onclick="back();" class="back-button">返回</button>
</div>

<script>
  function validateForm() {
    // 验证合同开始日期和结束日期
    const startDate = new Date(document.getElementById("startDate").value);
    const endDate = new Date(document.getElementById("endDate").value);

    if (startDate > endDate) {
      alert("合同开始日期不能晚于结束日期！");
      return false; // 阻止表单提交
    }

    // 如果所有验证都通过，返回 true 允许提交
    return true;
  }

  function back() {
    // 如果是销售人员，跳转到特定的页面
    window.location.href="../dashboard/dashboard_sales_man.jsp"
  }
</script>
</body>
</html>
