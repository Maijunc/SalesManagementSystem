<%@ page import="com.dgut.salesmanagementsystem.pojo.Contract" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dgut.salesmanagementsystem.model.ContractDAO" %>
<%@ page import="com.dgut.salesmanagementsystem.model.CustomerDAO" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.ContractSearchCriteria" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.ContractStatus" %>
<%@ page import="com.dgut.salesmanagementsystem.controller.CustomerController" %>
<%@ page import="com.dgut.salesmanagementsystem.controller.SalesmanController" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"SalesManager".equals(user.getRole().getRole())){
        response.sendRedirect("../login.jsp");
        return;
    }
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

    <!-- 搜索表单 -->
    <form action="../ContractController" method="get" id="search-form">
        <div class="form-group">
            <label for="contractName">合同名称：</label>
            <input type="text" id="contractName" name="contractName">
        </div>
        <div class="form-group">
            <label for="contractID">合同编号：</label>
            <input type="text" id="contractID" name="contractID">
        </div>
        <div class="form-group">
            <label for="status">合同状态：</label>
            <select id="status" name="status">
                <option value=""></option>
                <option value="1">未开始</option>
                <option value="2">进行中</option>
                <option value="3">已完成</option>
            </select>
        </div>
        <div class="form-group">
            <label for="start_time">开始时间：</label>
            <input type="date" id="start_time" name="start_time">
            <span class="separator">-</span>
            <input type="date" id="end_time" name="end_time">
        </div>
        <div class = actions>
            <button type="submit">查询</button>
            <button type="submit" onclick="resetForm()">重置</button>

        </div>
    </form>
    <a href="add_contract.jsp" class="add-button"><button type="button">合同录入</button></a>

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
            List<Contract> contractList;
            contractList = (List<Contract>) session.getAttribute("contractList");
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
            int cnt = 0;
            if (contractList != null && !contractList.isEmpty()) {
                for (Contract contract : contractList) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String startTime = contract.getStartDate() != null ? sdf.format(contract.getStartDate()) : "未开始";
                    cnt++;
                    String contractStatus = "";
                    try {
                        contractStatus = ContractStatus.getChineseStr(contract.getContractStatus().getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        %>
        <tr>
            <td><%= (currentPage - 1) * pageSize + cnt %></td>
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
<%--            <td><%= contractStatus %></td>--%>
            <td class="action-buttons" style="text-align: center">
                <a href="contract_view.jsp?contractID=<%= contract.getContractID() %>">查看</a>
                <a href="../PurchaseListController?pageNum=1<%= "&contractID=" + contract.getContractID() %>"
                   class="purchaseList">采购清单</a>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="8">暂无客户信息</td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

    <!-- 分页 -->
    <div class="pagination">
        <%
            StringBuilder url = new StringBuilder("../ContractController?pageNum=");
            url.append(currentPage - 1);

            // 这些是筛选条件
            if (session.getAttribute("contractNameFilter") != null) {
                url.append("&contractName=").append(session.getAttribute("contractNameFilter"));
            }
            if (session.getAttribute("contractIDFilter") != null) {
                url.append("&contractID=").append(session.getAttribute("contractIDFilter"));
            }
            if (session.getAttribute("statusFilter") != null) {
                url.append("&status=").append(session.getAttribute("statusFilter"));
            }
            if (session.getAttribute("startTimeFilter") != null) {
                url.append("&start_time=").append(session.getAttribute("startTimeFilter"));
            }
            if (session.getAttribute("endTimeFilter") != null) {
                url.append("&end_time=").append(session.getAttribute("endTimeFilter"));
            }

            String urlString = url.toString();
        %>
        <a href="<%= urlString %>" <%= (currentPage == 1) ? "style='pointer-events: none; color: gray;'" : "" %>>上一页</a>
        第 <%= currentPage %> 页 / 共 <%= totalPages %> 页
        <%
            url = new StringBuilder("../ContractController?pageNum=");
            url.append(currentPage + 1);

            // 这些是筛选条件
            if (session.getAttribute("contractNameFilter") != null) {
                url.append("&contractName=").append(session.getAttribute("contractNameFilter"));
            }
            if (session.getAttribute("contractIDFilter") != null) {
                url.append("&contractID=").append(session.getAttribute("contractIDFilter"));
            }
            if (session.getAttribute("statusFilter") != null) {
                url.append("&status=").append(session.getAttribute("statusFilter"));
            }
            if (session.getAttribute("startTimeFilter") != null) {
                url.append("&start_time=").append(session.getAttribute("startTimeFilter"));
            }
            if (session.getAttribute("endTimeFilter") != null) {
                url.append("&end_time=").append(session.getAttribute("endTimeFilter"));
            }

            urlString = url.toString();
        %>
        <a href="<%= urlString %>" <%= (currentPage == totalPages) ? "style='pointer-events: none; color: gray;'" : "" %>>下一页</a>
    </div>
</div>

<script>
    function goBack() {
        window.location.href="../dashboard/dashboard_sales_manager.jsp"
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

    // 日期验证
    const startTimeInput = document.getElementById('start_time');
    const endTimeInput = document.getElementById('end_time');

    function validateDates() {
        const startTime = new Date(startTimeInput.value);
        const endTime = new Date(endTimeInput.value);
        if (startTime && endTime && startTime >= endTime) {
            alert("开始时间必须早于结束时间！");
            endTimeInput.value = '';
        }
    }

    startTimeInput.addEventListener('change', validateDates);
    endTimeInput.addEventListener('change', validateDates);

    // 清空表单的函数
    function resetForm() {
        // 获取表单元素
        var form = document.getElementById('search-form');

        // 使用reset()方法来重置表单的所有内容
        form.reset();
    }
</script>
</body>
</html>
