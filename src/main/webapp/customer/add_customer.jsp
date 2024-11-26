<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>新增客户</title>
  <link rel="stylesheet" href="../css/customer-inside.css">
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
      <input type="text" id="contactPerson" name="contactPerson" required>
    </div>
    <div class="form-group">
      <label for="phone">电话:</label>
      <input type="text" id="phone" name="phone" required>
    </div>
    <div class="form-group">
      <label for="email">邮箱:</label>
      <input type="email" id="email" name="email" required>
    </div>
    <div class="form-group">
      <label for="address">地址:</label>
      <input type="text" id="address" name="address">
    </div>
    <div class="form-actions">
      <button type="submit" class="btn-primary">保存</button>
      <a href="../customer/customer_management.jsp" class="btn-secondary">返回</a>
    </div>
  </form>
</main>
</body>
</html>
