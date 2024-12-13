<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>生成发货单</title>
  <link rel="stylesheet" href="../css/create-ship-order.css">
</head>
<body>
<header class="page-header">
  <h1>生成发货单</h1>
  <a href="purchaseList_view.jsp?purchaseListID=<%= session.getAttribute("purchaseListID") %>" class="back-link">返回</a>
</header>

<div class="purchase-info">
  <h3>发货单基本信息</h3>
  <form action="../ShipOrderController" method="POST">
    <input type="hidden" name="action" value="create">
    <!-- 隐藏字段，用于提交商品ID -->
    <input type="hidden" id="productID" name="productID" value="<%= session.getAttribute("productID")%>">
    <!-- 隐藏字段，用于提交采购清单ID -->
    <input type="hidden" id="purchaseListID" name="purchaseListID" value="<%= session.getAttribute("purchaseListID")%>">
    <!-- 隐藏字段，用于提交采购清单商品ID -->
    <input type="hidden" id="purchaseListItemID" name="purchaseListItemID" value="<%= session.getAttribute("purchaseListItemID")%>">
    <!-- 隐藏字段，用于提交商品单价 -->
    <input type="hidden" id="unitPrice" name="unitPrice" value="<%= session.getAttribute("unitPrice")%>">
    <!-- 隐藏字段，用于提交顾客ID -->
    <input type="hidden" id="customerID" name="customerID" value="<%= session.getAttribute("customerID")%>">
    <!-- 客户名称 -->
    <div class="form-group">
      <label for="customerName">客户名称:</label>
      <input type="text" id="customerName" name="customerName" value="<%= session.getAttribute("customerName") %>" readonly>
    </div>
    <!-- 商品名称 -->
    <div class="form-group">
      <label for="productName">商品名称:</label>
      <input type="text" id="productName" name="productName" value="<%= session.getAttribute("productName") %>" readonly>
    </div>
    <!-- 数量 -->
    <div class="form-group">
      <label for="quantity">商品数量:</label>
      <input type="text" id="quantity" name="quantity" value="<%= session.getAttribute("quantity") %>" readonly>
    </div>
    <!-- 总金额 -->
    <div class="form-group">
      <label for="totalAmount">总金额:</label>
      <input type="text" id="totalAmount" name="totalAmount" value="<%= session.getAttribute("totalAmount") %>" readonly>
    </div>
    <!-- 收货人姓名 -->
    <div class="form-group">
      <label for="recipientName">收货人姓名:</label>
      <input type="text" id="recipientName" name="recipientName" placeholder="请输入收货人姓名" required>
    </div>
    <!-- 收货人电话 -->
    <div class="form-group">
      <label for="recipientPhone">收货人电话:</label>
      <input type="text" id="recipientPhone" name="recipientPhone" placeholder="请输入收货人电话" required>
    </div>
    <!-- 收货地址 -->
    <div class="form-group">
      <label for="shippingAddress">收货地址:</label>
      <input type="text" id="shippingAddress" name="shippingAddress" placeholder="请输入收货地址" required>
    </div>
    <!-- 备注 -->
    <div class="form-group">
      <label for="notes">备注:</label>
      <textarea id="notes" name="notes" placeholder="请输入备注信息"></textarea>
    </div>
    <!-- 提交按钮 -->
    <button type="submit">生成发货单</button>
  </form>
</div>
</body>
</html>
