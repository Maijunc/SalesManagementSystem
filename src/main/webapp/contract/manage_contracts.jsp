<%@ page import="com.dgut.salesmanagementsystem.pojo.Contract" %>
<%@ page import="java.util.List" %>
<%@ page import="com.dgut.salesmanagementsystem.model.ContractDAO" %>
<%@ page import="com.dgut.salesmanagementsystem.model.CustomerDAO" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.ContractSearchCriteria" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>合同管理</title>

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
    <form action="../ContractController" method="post" id = search-form>
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
            <input type="date" id="start_time" name="start_time"  />
            <span>-</span>
            <input type="date" id="end_time" name="end_time"  />
            <button type="submit">查询</button>
            <button type="reset">重置</button>
        </div>
    </form>

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
            <th>支付状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%
            Integer pageSizeObj = (Integer) request.getAttribute("pageSize");
            List<Contract> contractList;
            if(pageSizeObj == null) {
                ContractDAO contractDAO = new ContractDAO();
                contractList = contractDAO.searchContracts(new ContractSearchCriteria(), 1, 6);
            }
            else {
                contractList = (List<Contract>) request.getAttribute("contractList");
            }
            CustomerDAO customerDAO = new CustomerDAO();
            Integer currentPageObj = (Integer) request.getAttribute("currentPage");
            Integer totalPagesObj = (Integer) request.getAttribute("totalPages");
            int currentPage = (currentPageObj != null) ? currentPageObj : 1;
            int totalPages = (totalPagesObj != null) ? totalPagesObj : 1;
            int pageSize = (pageSizeObj != null) ? pageSizeObj : 6;
            int cnt = 0;
            if (contractList != null && !contractList.isEmpty()) {
                for (Contract contract : contractList) {
                    cnt++;
        %>
        <!-- 示例数据 -->
        <tr>
            <td><%= (currentPage - 1) * pageSize + cnt%></td>
            <td><%= contract.getContractName() %></td>
            <td><%= contract.getContractID() %></td>
            <td><%= customerDAO.getCustomerById(contract.getCustomerID()).getCustomerName() %></td>
            <td>销售人员</td>
            <td><%= contract.getStartDate() %></td>
            <td><%= contract.getContractStatus() %></td>
            <td class="action-buttons">
                <a href="edit_contract.jsp?contractID=<%= contract.getContractID() %>">修改</a>
                <a href="../ContractController?action=delete&contractID=<%= contract.getContractID() %>"
                   class="delete" onclick="return confirm('确认删除该合同吗？')">删除</a>
            </td>
        </tr>
        <!-- 动态数据通过JSP输出 -->
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

    <div>
        <a href="CustomerController?pageNum=<%= currentPage - 1 %>&searchKeyword=<%= request.getParameter("searchKeyword") %>"
                <%= (currentPage == 1) ? "style='pointer-events: none; color: gray;'" : "" %>>上一页</a>
        第 <%= currentPage %> 页 / 共 <%= totalPages %> 页
        <a href="CustomerController?pageNum=<%= currentPage + 1 %>&searchKeyword=<%= request.getParameter("searchKeyword") %>"
                <%= (currentPage == totalPages) ? "style='pointer-events: none; color: gray;'" : "" %>>下一页</a>
    </div>
</div>

<script>
    function goBack() {
        window.history.back();
    }
    function logout() {
        alert("您已成功退出登录");
        window.location.href = "login.jsp";
    }
    function editContract(id) {
        alert("编辑合同 ID：" + id);
        // 跳转到编辑页面
    }
    function deleteContract(id) {
        if (confirm("确认删除合同 ID：" + id + " 吗？")) {
            alert("合同已删除");
            // 实现删除逻辑
        }
    }

    // 获取两个日期输入框
    const startTimeInput = document.getElementById('start_time');
    const endTimeInput = document.getElementById('end_time');

    // 检查日期逻辑
    function validateDates() {
        const startTime = new Date(startTimeInput.value);
        const endTime = new Date(endTimeInput.value);

        // 如果开始时间大于结束时间，弹出警告并清除结束时间
        if (startTime && endTime && startTime >= endTime) {
            alert("开始时间必须早于结束时间！");
            endTimeInput.value = '';  // 清空结束时间输入框
        }
    }

    // 给两个日期输入框添加事件监听器
    startTimeInput.addEventListener('change', validateDates);
    endTimeInput.addEventListener('change', validateDates);

</script>
</body>
</html>
