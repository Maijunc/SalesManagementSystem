<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dgut.salesmanagementsystem.pojo.Customer" %>
<%@ page import="com.dgut.salesmanagementsystem.model.CustomerDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>修改客户</title>
</head>
<body>
<%
  int customerId = Integer.parseInt(request.getParameter("customerId"));
  CustomerDAO customerDAO = new CustomerDAO();
  Customer customer = customerDAO.getCustomerById(customerId);
%>
<h2>修改客户信息</h2>
<form method="POST" action="../CustomerController">
  <input type="hidden" name="action" value="edit">
  <input type="hidden" name="customerId" value="<%= customer.getCustomerID() %>">
  <label>客户名称:</label>
  <input type="text" name="customerName" value="<%= customer.getCustomerName() %>" required><br>
  <label>联系人:</label>
  <input type="text" name="contactPerson" value="<%= customer.getContactPerson() %>" required><br>
  <label>电话:</label>
  <input type="text" name="phone" value="<%= customer.getPhone() %>"><br>
  <label>邮箱:</label>
  <input type="text" name="email" value="<%= customer.getEmail() %>"><br>
  <label>地址:</label>
  <input type="text" name="address" value="<%= customer.getAddress() %>"><br>
  <button type="submit">保存</button>
  <a href="customer_management.jsp"><button type="button">返回</button></a>
</form>
</body>
</html>
