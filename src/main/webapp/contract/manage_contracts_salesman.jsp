<!-- contract_manage.jsp -->
<%@ page import="java.util.List" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.Contract" %>
<%@ page import="com.dgut.salesmanagementsystem.controller.SalesmanController" %>
<%@ page import="com.dgut.salesmanagementsystem.controller.CustomerController" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.ContractStatus" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.User" %>
<%@ page import="com.dgut.salesmanagementsystem.service.SalesmanService" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.Salesman" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  User user = (User) session.getAttribute("user");
  if (user == null || !"SalesMan".equals(user.getRole().getRole())) {
    response.sendRedirect("../login.jsp");
    return;
  }

  SalesmanService salesmanService = new SalesmanService();
  Salesman salesman = salesmanService.getSalesmanByName(user.getUserName());

  int salesmanID = salesman.getSalesmanID();

%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>合同管理</title>
  <link rel="stylesheet" type="text/css" href="../css/contract-manage.css">
</head>
<body>
<div class="container">
  <h1>合同管理</h1>

  <!-- 操作按钮 -->
  <div class="actions">
    <button onclick="goBack()">返回</button>
    <button onclick="logout()">退出登录</button>
  </div>

  <!-- 合同列表 -->
  <table>
    <thead>
    <tr>
      <th>序号</th>
      <th>合同名称</th>
      <th>合同编号</th>
      <th>客户</th>
      <th>销售人员</th>
      <th>开始时间</th>
      <th>合同状态</th>
      <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <%
      Integer pageSizeObj = (Integer) session.getAttribute("pageSize");
      Integer currentPageObj = (Integer) session.getAttribute("currentPage");
      Integer totalPagesObj = (Integer) session.getAttribute("totalPages");
      CustomerController customerController = new CustomerController();
      SalesmanController salesmanController = new SalesmanController();

      int currentPage = (currentPageObj != null) ? currentPageObj : 1;
      int totalPages = (totalPagesObj != null) ? totalPagesObj : 1;
      if(totalPagesObj == null) {
        System.err.println("ERROR!!! have not parameter:\"totalPagesObj\"");
      }
      int pageSize = (pageSizeObj != null) ? pageSizeObj : 6;
      List<Contract> contractList;
      contractList = (List<Contract>) session.getAttribute("contractList");
      if (contractList == null || contractList.isEmpty()) {
    %>
    <tr>
      <td colspan="8" style="text-align:center; color: black; font-size: 1.2em; padding: 20px;">
        暂无合同信息
      </td>
    </tr>
    <%
    } else {
      int cnt = 0;
      for (Contract contract : contractList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = contract.getStartDate() != null ? sdf.format(contract.getStartDate()) : "未开始";
        cnt++;
        String contractStatus = ContractStatus.getChineseStr(contract.getContractStatus().getValue());
    %>
    <tr>
      <td><%= (currentPage - 1) * 6 + cnt %></td>
      <td><%= contract.getContractName() %></td>
      <td><%= contract.getContractID() %></td>
      <td><%= customerController.getCustomerById(contract.getCustomerID()).getCustomerName() %></td>
      <td><%= salesmanController.getSalesmanById(contract.getSalesmanID()).getName()%></td>
      <td><%= startTime %></td>
      <td class="status-tag">
        <% if("未开始".equals(contractStatus))
          out.print("<div class=\"status-tag-blue\">" + contractStatus + "</div>");
        else if("已完成".equals(contractStatus))
          out.print("<div class=\"status-tag-green\">" + contractStatus + "</div>");
        else
          out.print("<div class=\"status-tag-yellow\">" + contractStatus + "</div>");%>
      </td>
      <td class="action-buttons" style="text-align: center">
        <a href="contract_view.jsp?contractID=<%= contract.getContractID() %>">查看</a>
        <a href="../PurchaseListController?pageNum=1<%= "&contractID=" + contract.getContractID() %>"
           class="purchaseList">采购清单</a>
      </td>
    </tr>
    <%
        }
      }
    %>
    </tbody>
  </table>

  <!-- 分页 -->
  <div class="pagination">
    <a href="<%= "../ContractController?pageNum=" + (currentPage - 1) %>&action=salesman&salesmanID=<%=salesmanID%>" <%= (currentPage == 1) ? "style='pointer-events: none; color: gray;'" : "" %>>上一页</a>
    第 <%= currentPage %> 页 / 共 <%= totalPages %> 页
    <a href="<%= "../ContractController?pageNum=" + (currentPage + 1) %>&action=salesman&salesmanID=<%=salesmanID%>" <%= (currentPage == totalPages) ? "style='pointer-events: none; color: gray;'" : "" %>>下一页</a>
  </div>
</div>

<script>
  function goBack() {
    window.location.href="../dashboard/dashboard_sales_man.jsp"
  }

  function logout() {
    // 创建一个隐藏的表单
    var form = document.createElement("form");
    form.action = "../logout.jsp";  // 指向注销处理的 JSP 页面
    form.method = "POST";

    // 将表单添加到 body 中
    document.body.appendChild(form);

    // 提交表单
    form.submit();
  }
</script>
</body>
</html>
