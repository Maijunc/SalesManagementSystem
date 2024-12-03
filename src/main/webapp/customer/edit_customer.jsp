<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dgut.salesmanagementsystem.pojo.Customer" %>
<%@ page import="com.dgut.salesmanagementsystem.model.CustomerDAO" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.CustomerType" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.CustomerStatus" %>
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
    int customerId = Integer.parseInt(request.getParameter("customerID"));
    CustomerDAO customerDAO = new CustomerDAO();
    Customer customer = customerDAO.getCustomerById(customerId);
  %>
  <form method="POST" action="../CustomerController" class="form-container">
    <input type="hidden" name="action" value="edit">
    <input type="hidden" name="pageNum" value=<%= request.getParameter("pageNum")%>>
    <input type="hidden" name="searchKeyword" value=<%= request.getParameter("searchKeyword")%>>
    <input type="hidden" name="customerID" value="<%= customer.getCustomerID() %>">
    <div class="form-group">
      <label for="customerName">客户名称:</label>
      <input type="text" id="customerName" name="customerName" value="<%= customer.getCustomerName() %>" required>
    </div>
    <div class="form-group">
      <label for="contactPerson">联系人:</label>
      <input type="text" id="contactPerson" name="contactPerson" value="<%= customer.getContactPerson() %>" >
    </div>
    <div class="form-group">
      <label for="phone">电话:</label>
      <input type="text" id="phone" name="phone" value="<%= customer.getPhone() %>" >
    </div>
    <div class="form-group">
      <label for="email">邮箱:</label>
      <input type="email" id="email" name="email" value="<%= customer.getEmail() %>" >
    </div>
    <div class="form-group">
      <label for="address">地址:</label>
      <input type="text" id="address" name="address" value="<%= customer.getAddress() %>">
    </div>
    <div class="form-group">
      <label for="city">城市:</label>
      <input type="text" id="city" name="city" value="<%= customer.getCity()%>">
    </div>
    <div class="form-group">
      <label for="postalCode">邮政编码:</label>
      <input type="text" id="postalCode" name="postalCode" value="<%= customer.getPostalCode()%>">
    </div>
    <div class="form-group">
      <label for="country">国家:</label>
      <input type="text" id="country" name="country" value="<%= customer.getCountry()%>">
    </div>
    <div class="form-group">
      <label for="customerType">客户类型：</label>
      <select id="customerType" name="customerType" selectedIndex="">
        <option value="1" <%= CustomerType.getIndexByValue(customer.getCustomerType()) == 1 ? "selected" : "" %>>个人</option>
        <option value="2" <%= CustomerType.getIndexByValue(customer.getCustomerType()) == 2 ? "selected" : "" %>>企业</option>
      </select>
    </div>
    <div class="form-group">
      <label for="customerStatus">客户类型：</label>
      <select id="customerStatus" name="customerStatus">
        <option value="1" <%= CustomerStatus.getIndexByValue(customer.getCustomerStatus()) == 1 ? "selected" : "" %>>活跃</option>
        <option value="2" <%= CustomerStatus.getIndexByValue(customer.getCustomerStatus()) == 2 ? "selected" : "" %>>暂停</option>
        <option value="3" <%= CustomerStatus.getIndexByValue(customer.getCustomerStatus()) == 3 ? "selected" : "" %>>黑名单</option>
      </select>
    </div>
    <div class="form-actions">
      <button type="submit" class="btn-primary">保存</button>
      <a href="customer_management.jsp" class="btn-secondary">返回</a>
    </div>
  </form>
</main>
</body>
</html>
