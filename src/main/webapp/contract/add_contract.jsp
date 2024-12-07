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
    <div class="form-group">
        <label for="salesmanInfo">客户:</label>
        <input type="text" id="customerInfo" name="customerInfo" readonly onclick="editCustomerInfo()"
               placeholder="点击选择客户">
    </div>
    <!-- 隐藏字段，用于提交客户ID -->
    <input type="hidden" id="customerID" name="customerID">

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
            <input type="text" id="SalesmanSearchKeyword" placeholder="请输入销售人员姓名或ID">
            <button type="button" onclick="searchSalesman()" class="modal-search-button">查询</button>
        </form>
        <table id="salesmanTable">
            <!-- 动态填充 -->
        </table>
        <!-- 分页控件 -->
        <div class="pagination" id="salesmanPagination"></div>
    </div>
</div>

<div id="customerModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeCustomerModal()">&times;</span>
        <h2>选择客户</h2>
        <form method="GET" id="searchCustomerForm">
            <input type="text" id="customerSearchKeyword" placeholder="请输入客户姓名或ID">
            <button type="button" onclick="searchCustomer()" class="modal-search-button">查询</button>
        </form>
        <table id="customerTable">
            <!-- 动态填充 -->
        </table>
        <!-- 分页控件 -->
        <div class="pagination" id="customerPagination"></div>
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
        const keyword = document.getElementById('SalesmanSearchKeyword').value;
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
                updateSalesmanPagination(data);
            })
            .catch(error => console.error('查询失败:', error));
    }

    function updateSalesmanPagination(data) {
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

        // 控制显示页码的范围
        const pageRange = 5; // 显示前后最多 5 页
        let startPage = Math.max(currentPage - pageRange, 1); // 当前页之前显示的页码
        let endPage = Math.min(currentPage + pageRange, totalPages); // 当前页之后显示的页码

        // 如果页码数量很多，显示 "省略号" 来简化分页显示
        if (startPage > 1) {
            const firstPage = document.createElement('a');
            firstPage.href = '#';
            firstPage.textContent = '1';
            firstPage.onclick = (e) => {
                e.preventDefault();
                searchCustomer(1);
            };
            pagination.appendChild(firstPage);
            if (startPage > 2) {
                const ellipsis = document.createElement('span');
                ellipsis.textContent = '...';
                pagination.appendChild(ellipsis);
            }
        }

        // 添加中间的页码链接
        for (let i = startPage; i <= endPage; i++) {
            const pageLink = document.createElement('a');
            pageLink.href = '#';
            pageLink.textContent = i;
            pageLink.className = i === currentPage ? 'active' : '';
            pageLink.onclick = (e) => {
                e.preventDefault();
                searchCustomer(i);
            };
            pagination.appendChild(pageLink);
        }

        // 如果有更多页，显示 "省略号" 和最后一页
        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                const ellipsis = document.createElement('span');
                ellipsis.textContent = '...';
                pagination.appendChild(ellipsis);
            }
            const lastPage = document.createElement('a');
            lastPage.href = '#';
            lastPage.textContent = totalPages;
            lastPage.onclick = (e) => {
                e.preventDefault();
                searchCustomer(totalPages);
            };
            pagination.appendChild(lastPage);
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
            <button onclick="searchSalesman(document.getElementById('jumpToPage').value)" class="modal-jump-button">跳转</button>
        `;
        pagination.appendChild(jumpTo);
    }

    function editCustomerInfo() {
        document.getElementById('customerModal').style.display = 'block';
    }

    function closeCustomerModal() {
        document.getElementById('customerModal').style.display = 'none';
    }

    function selectCustomer(id, name) {
        document.getElementById('customerID').value = id;
        document.getElementById('customerInfo').value = name;
        closeCustomerModal();
    }

    function searchCustomer(page = 1) {
        const keyword = document.getElementById('customerSearchKeyword').value;
        fetch(`../CustomerController?searchKeyword=\${encodeURIComponent(keyword)}&action=ajax&pageNum=\${page}`)
            .then(response => response.json())
            .then(data => {
                console.log(data)
                const table = document.getElementById('customerTable');
                table.innerHTML = '';
                if (data.elementList && data.elementList.length > 0) {
                    data.elementList.forEach(customer => {
                        console.log(customer)
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>\${customer.customerID}</td>
                            <td>\${customer.customerName}</td>
                            <td><button onclick="selectCustomer('\${customer.salesmanID}', '\${customer.customerName}')">选择</button></td>
                        `;
                        table.appendChild(row);
                    });
                } else {
                    table.innerHTML = '<tr><td colspan="3">暂无客户信息</td></tr>';
                }
                updateCustomerPagination(data);
            })
            .catch(error => console.error('查询失败:', error));
    }

    function updateCustomerPagination(data) {
        const pagination = document.getElementById('customerPagination');
        pagination.innerHTML = '';

        const totalPages = data.totalPages || 1;
        const currentPage = data.currentPage || 1;

        // 创建一个 <a> 标签并设置它的href 为 #，使其成为一个链接按钮。
        const prevButton = document.createElement('a');
        prevButton.href = '#';
        prevButton.textContent = '上一页';
        prevButton.className = currentPage === 1 ? 'disabled' : '';
        // 为按钮绑定 onclick 事件
        prevButton.onclick = (e) => {
            e.preventDefault();
            if (currentPage > 1) searchCustomer(currentPage - 1);
        };
        // 将这个按钮添加到 pagination 容器中。
        pagination.appendChild(prevButton);

        // 控制显示页码的范围
        const pageRange = 5; // 显示前后最多 5 页
        let startPage = Math.max(currentPage - pageRange, 1); // 当前页之前显示的页码
        let endPage = Math.min(currentPage + pageRange, totalPages); // 当前页之后显示的页码

        // 如果页码数量很多，显示 "省略号" 来简化分页显示
        if (startPage > 1) {
            const firstPage = document.createElement('a');
            firstPage.href = '#';
            firstPage.textContent = '1';
            firstPage.onclick = (e) => {
                e.preventDefault();
                searchCustomer(1);
            };
            pagination.appendChild(firstPage);
            if (startPage > 2) {
                const ellipsis = document.createElement('span');
                ellipsis.textContent = '...';
                pagination.appendChild(ellipsis);
            }
        }

        // 添加中间的页码链接
        for (let i = startPage; i <= endPage; i++) {
            const pageLink = document.createElement('a');
            pageLink.href = '#';
            pageLink.textContent = i;
            pageLink.className = i === currentPage ? 'active' : '';
            pageLink.onclick = (e) => {
                e.preventDefault();
                searchCustomer(i);
            };
            pagination.appendChild(pageLink);
        }

        // 如果有更多页，显示 "省略号" 和最后一页
        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                const ellipsis = document.createElement('span');
                ellipsis.textContent = '...';
                pagination.appendChild(ellipsis);
            }
            const lastPage = document.createElement('a');
            lastPage.href = '#';
            lastPage.textContent = totalPages;
            lastPage.onclick = (e) => {
                e.preventDefault();
                searchCustomer(totalPages);
            };
            pagination.appendChild(lastPage);
        }

        const nextButton = document.createElement('a');
        nextButton.href = '#';
        nextButton.textContent = '下一页';
        nextButton.className = currentPage === totalPages ? 'disabled' : '';
        nextButton.onclick = (e) => {
            e.preventDefault();
            if (currentPage < totalPages) searchCustomer(currentPage + 1);
        };
        pagination.appendChild(nextButton);

        const jumpTo = document.createElement('span');
        jumpTo.innerHTML = `
            跳转到: <input type="number" min="1" max="\${totalPages}" value="\${currentPage}" id="jumpToPage">
            <button onclick="searchCustomer(document.getElementById('jumpToPage').value)" class="modal-jump-button">跳转</button>
        `;
        pagination.appendChild(jumpTo);
    }

    /* 页面加载的时候就加载一次 */
    window.onload=function(){
        searchCustomer();
        searchSalesman()
    }
</script>
</body>
</html>
