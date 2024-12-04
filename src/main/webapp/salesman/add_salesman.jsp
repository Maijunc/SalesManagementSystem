<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>新增销售人员</title>
  <link rel="stylesheet" href="../css/edit-inside.css">

</head>
<body>
<header>
  <h1>新增销售人员信息</h1>
</header>
<main>
  <form method="POST" action="../SalesmanController" class="form-container">
    <input type="hidden" name="action" value="add">
    <div class="form-group">
      <label for="name">销售人员姓名:</label>
      <input type="text" id="name" name="name" required>
    </div>

    <!-- 合并邮箱和电话为一个字段 -->
    <div class="form-group">
      <label for="contactInfo">联系方式:</label>
      <input type="text" id="contactInfo" name="contactInfo" readonly onclick="editContactInfo()"
             placeholder="点击编辑联系方式" >
    </div>

    <!-- 隐藏字段，用于提交邮箱和电话 -->
    <input type="hidden" id="email" name="email">
    <input type="hidden" id="phone" name="phone">

    <div class="form-group">
      <label for="totalSales">总销售额:</label>
      <input type="number" step="0.01" id="totalSales" name="totalSales" required>
    </div>
    <div class="form-group">
      <label for="commission">佣金:</label>
      <input type="number" step="0.01" id="commission" name="commission" required>
    </div>
    <div class="form-actions">
      <button type="submit" class="btn-primary">保存</button>
      <a href="salesman_management.jsp" class="btn-secondary">返回</a>
    </div>
  </form>
</main>

<!-- Modal for editing email and phone -->
<div id="contractModal" class="modal">
  <div class="modal-content">
    <span class="close" onclick="closeModal()">&times;</span>
    <h2>修改联系方式</h2>
    <label for="editEmail">邮箱:</label>
    <input type="email" id="editEmail">
    <br>
    <label for="editPhone">电话:</label>
    <input type="text" id="editPhone">
    <br>
    <button type="button" onclick="saveContactInfo()">保存</button>
  </div>
</div>

<script>
  // Function to open the modal and edit contact info
  function editContactInfo() {
    // Get current email and phone (if they were set already)
    document.getElementById('email').value = document.getElementById('editEmail').value;
    document.getElementById('phone').value = document.getElementById('editPhone').value;
    // Open the modal
    document.getElementById('contractModal').style.display = 'block';
  }

  // Function to close the modal
  function closeModal() {
    document.getElementById('contractModal').style.display = 'none';
  }

  // Function to save the updated contact info
  function saveContactInfo() {
    var email = document.getElementById('editEmail').value;
    var phone = document.getElementById('editPhone').value;

    // Update the contactInfo field with the new email and phone
    // document.getElementById('contactInfo').value = email + " | " + phone;

    // Update the hidden email and phone fields to send to the server
    document.getElementById('email').value = email;
    document.getElementById('phone').value = phone;

    // Close the modal
    closeModal();
  }
</script>
</body>
</html>
