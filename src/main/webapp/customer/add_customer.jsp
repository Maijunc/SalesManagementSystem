<%@ page import="com.dgut.salesmanagementsystem.pojo.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"SalesManager".equals(user.getRole().getRole())) {
    response.sendRedirect("../login.jsp");
    return;
  }
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>新增客户</title>
  <link rel="stylesheet" href="../css/edit-inside.css">
</head>
<body>
<header>
  <h1>新增客户信息</h1>
</header>
<main>
  <form method="POST" action="../CustomerController" class="form-container">
    <input type="hidden" name="action" value="add">
    <div class="form-group">
      <label for="customerName">客户名称:</label>
      <input type="text" id="customerName" name="customerName" required>
    </div>
    <div class="form-group">
      <label for="contactPerson">联系人:</label>
      <input type="text" id="contactPerson" name="contactPerson">
    </div>
    <div class="form-group">
      <label for="phone">电话:</label>
      <input type="text" id="phone" name="phone">
    </div>
    <div class="form-group">
      <label for="email">邮箱:</label>
      <input type="email" id="email" name="email">
    </div>
    <div class="form-group">
      <label for="address">地址:</label>
      <input type="text" id="address" name="address">
    </div>
    <div class="form-group">
      <label for="city">城市:</label>
      <input type="text" id="city" name="city">
    </div>
    <div class="form-group">
      <label for="postalCode">邮政编码:</label>
      <input type="text" id="postalCode" name="postalCode">
    </div>
    <div class="form-group">
      <label for="country">国家:</label>
      <input type="text" id="country" name="country">
    </div>
    <div class="form-group">
      <label for="customerType">客户类型：</label>
      <select id="customerType" name="customerType">
        <option value="1">个人</option>
        <option value="2">企业</option>
      </select>
    </div>
    <div class="form-group">
      <label for="customerStatus">客户类型：</label>
      <select id="customerStatus" name="customerStatus">
        <option value="1">活跃</option>
        <option value="2">暂停</option>
        <option value="3">黑名单</option>
      </select>
    </div>
    <div class="form-actions">
      <button type="submit" class="btn-primary">保存</button>
      <a href="../customer/customer_management.jsp" class="btn-secondary">返回</a>
    </div>
  </form>
</main>
</body>
</html>
