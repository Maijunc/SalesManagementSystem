<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.Customer" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.User" %>

<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"SalesManager".equals(user.getRole().getRole())) {
    response.sendRedirect("../login.jsp");
    return;
  }
%>

<!-- 导入 Customer 类 -->
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>客户管理</title>
  <link rel="stylesheet" href="../css/customer-outside.css">
</head>
<body>
<header>
  <h1>客户管理系统</h1>
</header>
<main>
  <!-- 查询客户 -->
  <form method="GET" action="../CustomerController">
    <input type="text" name="searchKeyword" placeholder="请输入客户名称或联系人">
    <button type="submit">查询</button>
    <a href="add_customer.jsp"><button type="button">新增客户</button></a>
  </form>

  <!-- 客户列表 -->
  <table>
    <thead>
    <tr>
      <th>客户ID</th>
      <th>客户名称</th>
      <th>联系人</th>
      <th>电话</th>
      <th>邮箱</th>
      <th>地址</th>
      <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%
      List<Customer> customerList;
      customerList = (List<Customer>) session.getAttribute("customerList");
      Integer currentPageObj = (Integer) session.getAttribute("currentPage");
      Integer totalPagesObj = (Integer) session.getAttribute("totalPages");
      int currentPage = (currentPageObj != null) ? currentPageObj : 1;
      int totalPages = (totalPagesObj != null) ? totalPagesObj : 1;

      if (customerList != null && !customerList.isEmpty()) {
        for (Customer customer : customerList) {
    %>
    <tr>
      <td><%= customer.getCustomerID() %></td>
      <td><%= customer.getCustomerName() %></td>
      <td><%= customer.getContactPerson() %></td>
      <td><%= customer.getPhone() %></td>
      <td><%= customer.getEmail() %></td>
      <td><%= customer.getAddress() %></td>
      <td>
        <a href="edit_customer.jsp?customerID=<%= customer.getCustomerID() %>&pageNum=<%= currentPage%>
        &searchKeyword=<%= session.getAttribute("searchKeyword") %>>">修改</a>
        |
        <a href="../CustomerController?action=delete&customerID=<%= customer.getCustomerID() %>"
           class="delete" onclick="return confirm('确认删除该客户吗？')">删除</a>
      </td>
    </tr>
    <%
      }
    } else {
    %>
    <tr>
      <td colspan="7">暂无客户信息</td>
    </tr>
    <%
      }
    %>
    </tbody>
  </table>

  <!-- 分页导航 -->
  <div class="pagination">
    <a href="../CustomerController?pageNum=<%= currentPage - 1 %>&searchKeyword=<%= session.getAttribute("searchKeyword") %>"
            <%= (currentPage == 1) ? "style='pointer-events: none; color: gray;'" : "" %>>上一页</a>
    第 <%= currentPage %> 页 / 共 <%= totalPages %> 页
    <a href="../CustomerController?pageNum=<%= currentPage + 1 %>&searchKeyword=<%= session.getAttribute("searchKeyword") %>"
            <%= (currentPage == totalPages) ? "style='pointer-events: none; color: gray;'" : "" %>>下一页</a>
  </div>
</main>
</body>
</html>

