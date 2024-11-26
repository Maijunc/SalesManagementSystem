<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dgut.salesmanagementsystem.pojo.Customer" %>
<%@ page import="com.dgut.salesmanagementsystem.model.CustomerDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>修改客户</title>
  <link rel="stylesheet" href="../css/customer-inside.css">
</head>
<body>
<header>
  <h1>修改客户信息</h1>
</header>
<main>
  <%
    int customerId = Integer.parseInt(request.getParameter("customerId"));
    CustomerDAO customerDAO = new CustomerDAO();
    Customer customer = customerDAO.getCustomerById(customerId);
  %>
  <form method="POST" action="../CustomerController" class="form-container">
    <input type="hidden" name="action" value="edit">
    <input type="hidden" name="customerId" value="<%= customer.getCustomerID() %>">
    <div class="form-group">
      <label for="customerName">客户名称:</label>
      <input type="text" id="customerName" name="customerName" value="<%= customer.getCustomerName() %>" required>
    </div>
    <div class="form-group">
      <label for="contactPerson">联系人:</label>
      <input type="text" id="contactPerson" name="contactPerson" value="<%= customer.getContactPerson() %>" required>
    </div>
    <div class="form-group">
      <label for="phone">电话:</label>
      <input type="text" id="phone" name="phone" value="<%= customer.getPhone() %>" required>
    </div>
    <div class="form-group">
      <label for="email">邮箱:</label>
      <input type="email" id="email" name="email" value="<%= customer.getEmail() %>" required>
    </div>
    <div class="form-group">
      <label for="address">地址:</label>
      <input type="text" id="address" name="address" value="<%= customer.getAddress() %>">
    </div>
    <div class="form-actions">
      <button type="submit" class="btn-primary">保存</button>
      <a href="customer_management.jsp" class="btn-secondary">返回</a>
    </div>
  </form>
</main>
</body>
</html>
