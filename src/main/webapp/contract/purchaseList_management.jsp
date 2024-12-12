<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.PurchaseList" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.PaymentStatus" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>采购清单管理</title>
    <link rel="stylesheet" href="../css/purchase-list.css">
</head>
<body>
<header class="page-header">
    <a href="manage_contracts.jsp" class="back-link">返回</a> <!-- 返回按钮 -->
    <h1>采购清单管理 - 合同：<%= session.getAttribute("contractName") %></h1>
</header>

<main class="main-content">
    <!-- 操作按钮 -->
    <div class="actions">
        <a href="add_purchase_list.jsp?contractID=<%= session.getAttribute("contractID") %>">
            <button type="button">新增采购清单</button>
        </a>
    </div>

    <!-- 采购清单列表 -->
    <table class="purchase-list-table">
        <thead>
        <tr>
            <th>序号</th>
            <th>创建日期</th>
            <th>总价</th>
            <th>状态</th>
            <th colspan="2">操作</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<PurchaseList> purchaseLists = (List<PurchaseList>) session.getAttribute("purchaseLists");
            Integer pageSizeObj = (Integer) session.getAttribute("pageSize");
            Integer currentPageObj = (Integer) session.getAttribute("currentPage");
            Integer totalPagesObj = (Integer) session.getAttribute("totalPages");
            int currentPage = (currentPageObj != null) ? currentPageObj : 1;
            int totalPages = (totalPagesObj != null) ? totalPagesObj : 1;
            int pageSize = (pageSizeObj != null) ? pageSizeObj : 6;
            int cnt = 0;
            if (purchaseLists != null && !purchaseLists.isEmpty()) {
                for (PurchaseList purchaseList : purchaseLists) {
                    cnt++;
        %>
        <tr>
            <td><%= (currentPage - 1) * pageSize + cnt %></td>
            <td><%= purchaseList.getCreateDate() %></td>
            <td><%= purchaseList.getTotalPrice() %></td>
            <td><%= PaymentStatus.getChineseStr(purchaseList.getPaymentStatus()) %></td>
            <td colspan="2" class="action-buttons">
                <a href="../PurchaseListController?action=details&purchaseListID=<%= purchaseList.getPurchaseListID() %>">详情</a>
                <a href="../PurchaseListController?action=pay&purchaseListID=<%= purchaseList.getPurchaseListID() %>">付款</a>
                <a href="../PurchaseListController?action=addShipOrder&purchaseListID=<%= purchaseList.getPurchaseListID() %>">生成发货单</a>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="6">暂无采购清单信息</td>
        </tr>
        <% } %>
        </tbody>
    </table>

    <!-- 分页 -->
    <div class="pagination">
        <a href="../PurchaseListController?pageNum=<%= currentPage - 1 %>"
                <%= (currentPage == 1) ? "style='pointer-events: none; color: gray;'" : "" %>>上一页</a>
        第 <%= currentPage %> 页 / 共 <%= totalPages %> 页
        <a href="../PurchaseListController?pageNum=<%= currentPage + 1 %>"
                <%= (currentPage == totalPages) ? "style='pointer-events: none; color: gray;'" : "" %>>下一页</a>
    </div>
</main>

</body>
</html>
