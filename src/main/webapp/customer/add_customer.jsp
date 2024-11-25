<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>新增客户</title>
</head>
<body>
<h2>新增客户信息</h2>
<form method="POST" action="../CustomerController">
  <input type="hidden" name="action" value="add">
  <label>客户名称:</label>
  <input type="text" name="customerName" required><br>
  <label>联系人:</label>
  <input type="text" name="contactPerson" required><br>
  <label>电话:</label>
  <input type="text" name="phone" required><br>
  <label>邮箱:</label>
  <input type="text" name="email" required><br>
  <label>地址:</label>
  <input type="text" name="address"><br>
  <button type="submit">保存</button>
  <a href="../customer/customer_management.jsp"><button type="button">返回</button></a>
</form>
</body>
</html>
