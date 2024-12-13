<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.Salesman" %>
<%@ page import="com.dgut.salesmanagementsystem.model.SalesmanDAO" %>
<%@ page import="com.dgut.salesmanagementsystem.controller.SalesmanController" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.User" %>
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
    <title>销售人员管理</title>
    <link rel="stylesheet" href="../css/customer-outside.css">
</head>
<body>
<header>
    <h1>销售人员管理系统</h1>
</header>
<main>
    <form method="GET" action="../SalesmanController">
        <input type="text" name="searchKeyword" placeholder="请输入销售人员姓名或ID">
        <button type="submit">查询</button>
        <a href="add_salesman.jsp"><button type="button">新增销售人员</button></a>
    </form>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>姓名</th>
            <th>邮箱</th>
            <th>电话</th>
            <th>总销售额</th>
            <th>佣金</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Salesman> salesmanList;
            salesmanList = (List<Salesman>) session.getAttribute("salesmanList");
            Integer currentPageObj = (Integer) session.getAttribute("currentPage");
            Integer totalPagesObj = (Integer) session.getAttribute("totalPages");
            int currentPage = (currentPageObj != null) ? currentPageObj : 1;
            int totalPages = (totalPagesObj != null) ? totalPagesObj : 1;

            if (salesmanList != null && !salesmanList.isEmpty()) {
                for (Salesman salesman : salesmanList) {
        %>
        <tr>
            <td><%= salesman.getSalesmanID() %></td>
            <td><%= salesman.getName() %></td>
            <td><%= salesman.getContactInfo() != null ? salesman.getContactInfo().getEmail() : "" %></td>
            <td><%= salesman.getContactInfo() != null ? salesman.getContactInfo().getPhone() : "" %></td>
            <td><%= salesman.getTotalSales() %></td>
            <td><%= salesman.getCommission() %></td>
            <td>
                <a href="edit_salesman.jsp?salesmanID=<%= salesman.getSalesmanID() %>&pageNum=<%= currentPage%>
        &searchKeyword=<%= session.getAttribute("searchKeyword") %>>">修改</a>
                |
                <a href="../SalesmanController?action=delete&salesmanID=<%= salesman.getSalesmanID() %>"
                   onclick="return confirm('确认删除该销售人员吗？')">删除</a>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="7">暂无销售人员信息</td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

    <!-- 分页导航 -->
    <div class="pagination">
        <a href="../SalesmanController?pageNum=<%= currentPage - 1 %>&searchKeyword=<%= session.getAttribute("searchKeyword") %>"
                <%= (currentPage == 1) ? "style='pointer-events: none; color: gray;'" : "" %>>上一页</a>
        第 <%= currentPage %> 页 / 共 <%= totalPages %> 页
        <a href="../SalesmanController?pageNum=<%= currentPage + 1 %>&searchKeyword=<%= session.getAttribute("searchKeyword") %>"
                <%= (currentPage == totalPages) ? "style='pointer-events: none; color: gray;'" : "" %>>下一页</a>
    </div>
</main>
</body>
</html>
