<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.Salesman" %>
<%@ page import="com.dgut.salesmanagementsystem.pojo.Customer" %>
<%@ page import="com.dgut.salesmanagementsystem.controller.SalesmanController" %>
<%@ page import="com.dgut.salesmanagementsystem.controller.CustomerController" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新建合同</title>
    <link rel="stylesheet" href="../css/add-contract.css"> <!-- 使用你的样式 -->
    <style>
        /* 分页控件样式 */
        .pagination {
            text-align: center;
            margin: 20px 0;
        }

        .pagination a {
            display: inline-block;
            padding: 8px 12px;
            margin: 0 5px;
            text-decoration: none;
            background-color: #f4f4f9;
            border: 1px solid #ddd;
            color: #0078d4;
            border-radius: 4px;
            transition: background-color 0.3s, color 0.3s;
        }

        .pagination a:hover {
            background-color: #0078d4;
            color: white;
        }

        .pagination a.active {
            background-color: #005a9e;
            color: white;
            pointer-events: none;
        }

        .pagination a.disabled {
            background-color: #ddd;
            color: #bbb;
            pointer-events: none;
        }

        .pagination span {
            margin-left: 10px;
        }

        .pagination input {
            width: 50px;
            padding: 4px;
            text-align: center;
            border: 1px solid #ddd;
            border-radius: 4px;
        }

        .pagination button {
            padding: 6px 12px;
            border: 1px solid #ddd;
            background-color: #0078d4;
            color: white;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .pagination button:hover {
            background-color: #005a9e;
        }
    </style>
</head>
<body>
<h2>新建合同</h2>
<form action="../ContractController" method="post">
    <input type="hidden" name="action" value="add">

    <!-- 合同基本信息 -->
    <div>
        <label for="contractName">合同名称：</label>
        <input type="text" id="contractName" name="contractName" required>
    </div>
    <div>
        <label for="startDate">合同开始日期：</label>
        <input type="date" id="startDate" name="startDate" required>
    </div>
    <div>
        <label for="endDate">合同结束日期：</label>
        <input type="date" id="endDate" name="endDate" required>
    </div>
    <div>
        <label for="contractStatus">合同状态：</label>
        <select id="contractStatus" name="contractStatus" required>
            <option value="1">未开始</option>
            <option value="2">进行中</option>
            <option value="3">已完成</option>
        </select>
    </div>

    <!-- 客户选择 -->
    <div>
        <label for="customerID">客户：</label>
        <select id="customerID" name="customerID" required>
            <option value="">请选择客户</option>
            <%
                CustomerController customerController = new CustomerController();
                List<Customer> customerList = customerController.getPageCustomers("", 1);
                if (customerList != null) {
                    for (Customer customer : customerList) {
            %>
            <option value="<%= customer.getCustomerID() %>"><%= customer.getCustomerName() %></option>
            <%
                    }
                }
            %>
        </select>
    </div>

    <!-- 销售人员选择 -->
    <div class="form-group">
        <label for="salesmanInfo">销售人员:</label>
        <input type="text" id="salesmanInfo" name="salesmanInfo" readonly onclick="editSalesmanInfo()"
               placeholder="点击选择销售人员">
    </div>
    <!-- 隐藏字段，用于提交销售人员ID -->
    <input type="hidden" id="salesmanID" name="salesmanID">

    <!-- 商品选择 -->
    <h3>商品列表</h3>
    <table>
        <thead>
        <tr>
            <th>选择商品</th>
            <th>数量</th>
            <th>单价</th>
            <th>总价</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><input type="checkbox" name="product" value="1"> 商品1</td>
            <td><input type="number" name="quantity" value="1"></td>
            <td><input type="text" name="unitPrice" value="100.00" readonly></td>
            <td><input type="text" name="totalPrice" value="100.00" readonly></td>
        </tr>
        <!-- 其他商品行 -->
        </tbody>
    </table>

    <div>
        <button type="submit">保存合同</button>
    </div>
</form>

<div id="salesmanModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeSalesmanModal()">&times;</span>
        <h2>选择销售人员</h2>
        <form method="GET" id="searchSalesmanForm">
            <input type="text" id="searchKeyword" placeholder="请输入销售人员姓名或ID">
            <button type="button" onclick="searchSalesman()">查询</button>
        </form>
        <table id="salesmanTable">
            <!-- 动态填充 -->
        </table>
        <!-- 分页控件 -->
        <div class="pagination" id="salesmanPagination"></div>
    </div>
</div>

<script>
    function editSalesmanInfo() {
        document.getElementById('salesmanModal').style.display = 'block';
    }

    function closeSalesmanModal() {
        document.getElementById('salesmanModal').style.display = 'none';
    }

    function selectSalesman(id, name) {
        document.getElementById('salesmanID').value = id;
        document.getElementById('salesmanInfo').value = name;
        closeSalesmanModal();
    }

    function searchSalesman(page = 1) {
        const keyword = document.getElementById('searchKeyword').value;
        fetch(`../SalesmanController?searchKeyword=\${encodeURIComponent(keyword)}&action=ajax&pageNum=\${page}`)
            .then(response => response.json())
            .then(data => {
                console.log(data)
                const table = document.getElementById('salesmanTable');
                table.innerHTML = '';
                if (data.elementList && data.elementList.length > 0) {
                    data.elementList.forEach(salesman => {
                        // console.log(salesman)
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>\${salesman.salesmanID}</td>
                            <td>\${salesman.name}</td>
                            <td><button onclick="selectSalesman('\${salesman.salesmanID}', '\${salesman.name}')">选择</button></td>
                        `;
                        table.appendChild(row);
                    });
                } else {
                    table.innerHTML = '<tr><td colspan="3">暂无销售人员信息</td></tr>';
                }
                updatePagination(data);
            })
            .catch(error => console.error('查询失败:', error));
    }

    function updatePagination(data) {
        const pagination = document.getElementById('salesmanPagination');
        pagination.innerHTML = '';

        const totalPages = data.totalPages || 1;
        const currentPage = data.currentPage || 1;

        const prevButton = document.createElement('a');
        prevButton.href = '#';
        prevButton.textContent = '上一页';
        prevButton.className = currentPage === 1 ? 'disabled' : '';
        prevButton.onclick = (e) => {
            e.preventDefault();
            if (currentPage > 1) searchSalesman(currentPage - 1);
        };
        pagination.appendChild(prevButton);

        for (let i = 1; i <= totalPages; i++) {
            const pageLink = document.createElement('a');
            pageLink.href = '#';
            pageLink.textContent = i;
            pageLink.className = i === currentPage ? 'active' : '';
            pageLink.onclick = (e) => {
                e.preventDefault();
                searchSalesman(i);
            };
            pagination.appendChild(pageLink);
        }

        const nextButton = document.createElement('a');
        nextButton.href = '#';
        nextButton.textContent = '下一页';
        nextButton.className = currentPage === totalPages ? 'disabled' : '';
        nextButton.onclick = (e) => {
            e.preventDefault();
            if (currentPage < totalPages) searchSalesman(currentPage + 1);
        };
        pagination.appendChild(nextButton);

        const jumpTo = document.createElement('span');
        jumpTo.innerHTML = `
            跳转到: <input type="number" min="1" max="\${totalPages}" value="\${currentPage}" id="jumpToPage">
            <button onclick="searchSalesman(document.getElementById('jumpToPage').value)">跳转</button>
        `;
        pagination.appendChild(jumpTo);
    }
</script>
</body>
</html>
