<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.User" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.PurchaseOrder" %>
<%@ page import="com.dgut.salesmanagementsystem.service.PurchaseOrderService" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.PurchaseOrderStatus" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"WarehouseManager".equals(user.getRole().getRole())) {
    response.sendRedirect("../login.jsp");
    return;
  }

  PurchaseOrderService purchaseOrderService = new PurchaseOrderService();

  int purchaseOrderID = Integer.parseInt(request.getParameter("purchaseOrderID"));
  PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrderByID(purchaseOrderID);
  if (purchaseOrder == null) {
    response.sendRedirect("purchase_order_list.jsp");
    return;
  }

  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  String createdAt = (purchaseOrder.getCreatedAt() != null) ? sdf.format(purchaseOrder.getCreatedAt()) : "未知";
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>进货单详情</title>
  <link rel="stylesheet" href="../css/purchase-order-view.css">
</head>
<body>
<header class="page-header">
  <a href="../PurchaseOrderController?pageNum=1" class="back-link">返回</a>
  <h1>进货单详情</h1>
</header>

<main class="main-content">
  <form action="../PurchaseOrderController" method="post" onsubmit="return validateForm()">
    <input type="hidden" name="purchaseOrderID" value="<%= purchaseOrder.getPurchaseOrderID() %>">

    <table class="purchase-order-details">
      <tr>
        <th>进货单编号</th>
        <td><%= purchaseOrder.getPurchaseOrderID() %></td>
      </tr>
      <tr>
        <th>创建时间</th>
        <td><%= createdAt %></td>
      </tr>
      <tr>
        <th>商品名称</th>
        <td><%= purchaseOrder.getProductName() %></td>
      </tr>
      <tr>
        <th>需要进货数量</th>
        <td><%= purchaseOrder.getRequiredQuantity() %></td>
      </tr>
      <tr>
        <th>实际到货数量</th>
        <td>
          <input type="number" name="actualQuantity" value="<%= purchaseOrder.getActualQuantity() %>"
                 min="0" required <%= "completed".equals(purchaseOrder.getPurchaseOrderStatus().getValue()) ? "readonly" : "" %>>
        </td>
      </tr>
      <tr>
        <th>状态</th>
        <td><%=PurchaseOrderStatus.getChineseStr(purchaseOrder.getPurchaseOrderStatus().getValue()) %></td>
      </tr>
      <tr>
        <th>供应商</th>
        <td><input type="text" name="supplierName" value="<%= purchaseOrder.getSupplierName() %>"></td>
      </tr>
      <tr>
        <th>备注</th>
        <td><textarea name="notes"><%= purchaseOrder.getNotes() != null ? purchaseOrder.getNotes() : "" %></textarea></td>
      </tr>
    </table>

    <div class="form-actions">
      <button type="submit" name="action" value="confirm"
              <%= "completed".equals(purchaseOrder.getPurchaseOrderStatus().getValue()) ? "disabled" : "" %>>更新</button>
      <a href="purchase_order_list.jsp" class="cancel-button">取消</a>
    </div>
  </form>
</main>
<script>
  function validateForm() {
    var requiredQuantity = parseInt(document.querySelector("[name='requiredQuantity']").value);
    var actualQuantity = parseInt(document.querySelector("[name='actualQuantity']").value);

    // 验证实际到货数量是否小于需要进货数量
    if (actualQuantity < requiredQuantity) {
      alert("实际到货数量不能小于需要进货数量！");
      return false; // 阻止表单提交
    }

    return true; // 表单可以提交
  }
</script>

</body>
</html>
