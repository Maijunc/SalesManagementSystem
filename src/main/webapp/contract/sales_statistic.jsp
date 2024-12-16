<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>销售统计</title>
</head>
<link rel="stylesheet" href="../css/sales-statistic.css">
<body>
<div class="contract-details">
    <h2>统计详情</h2>
<%--    <form id="contractForm" action="../ContractController" method="post" onsubmit="return validateForm()">--%>
    <form id="contractForm" method="post" onsubmit="return validateForm()">

        <input type="hidden" name="action" value="stat">
        <div>
            <label for="startDate">合同开始日期：</label>
            <input type="date" id="startDate" name="startDate">
        </div>
        <div>
            <label for="endDate">合同结束日期：</label>
            <input type="date" id="endDate" name="endDate">
        </div>
        <!-- 客户选择 -->
        <div class="form-group">
            <label for="customerInfo">客户:</label>
            <input type="text" id="customerInfo" name="customerInfo" readonly onclick="editCustomerInfo()"
                   placeholder="点击选择客户">
        </div>
        <!-- 隐藏字段，用于提交客户ID -->
        <input type="hidden" id="customerID" name="customerID">

        <!-- 商品选择 -->
        <div class="form-group">
            <label for="productInfo">商品:</label>
            <input type="text" id="productInfo" name="productInfo" readonly onclick="editProductInfo()"
                   placeholder="点击选择商品">
        </div>
        <!-- 隐藏字段，用于提交商品ID -->
        <input type="hidden" id="productID" name="productID">

        <!-- 提交按钮 -->
        <button type="submit">统计</button>
        <!-- 清空按钮 -->
        <button type="button" onclick="clearForm()">清空</button>
    </form>
</div>

<%-- 销售统计展示区域 --%>
<div id="salesStatistics" class="contract-details">
    <h3>销售统计结果</h3>
    <div>
        <h4>统计日期范围：</h4>
        <p id="dateRange"></p>  <!-- 显示日期范围 -->
    </div>
    <div>
        <h4>销售总额：</h4>
        <p id="totalSales"></p>
    </div>
    <div>
        <h4>客户销售额：</h4>
        <table id="customerSalesTable">
            <tr><th>客户名称</th><th>销售额</th></tr>
        </table>
    </div>
    <div>
        <h4>商品销售额：</h4>
        <table id="productSalesTable">
            <tr><th>商品名称</th><th>销售额</th></tr>
        </table>
    </div>
</div>

<!-- 通用模态框 -->
<div id="productModal" class="modal">
    <div class="modal-dialog">
        <div class="modal-header">
            <h2>选择商品</h2>
            <button class="close-button" aria-label="关闭模态框" onclick="closeProductModal()">&times;</button>
        </div>
        <div class="modal-body">
            <form id="searchProductForm">
                <div class="search-bar">
                    <input type="text" id="productSearchKeyword" placeholder="请输入商品名称或ID">
                    <button type="button" onclick="searchProduct()" class="modal-search-button">查询</button>
                </div>
            </form>
            <div class="table-container">
                <table id="productTable">
                    <!-- 动态填充 -->
                </table>
            </div>
        </div>
        <div class="modal-footer">
            <div class="pagination" id="productPagination"></div>
            <button type="button" class="close-modal-btn" onclick="closeProductModal()">关闭</button>
        </div>
    </div>
</div>

<div id="customerModal" class="modal">
    <div class="modal-dialog">
        <div class="modal-header">
            <h2>选择客户</h2>
            <button class="close-button" aria-label="关闭模态框" onclick="closeCustomerModal()">&times;</button>
        </div>
        <div class="modal-body">
            <form id="searchCustomerForm">
                <div class="search-bar">
                    <input type="text" id="customerSearchKeyword" placeholder="请输入客户姓名或ID">
                    <button type="button" onclick="searchCustomer()" class="modal-search-button">查询</button>
                </div>
            </form>
            <div class="table-container">
                <table id="customerTable">
                    <!-- 动态填充 -->
                </table>
            </div>
        </div>
        <div class="modal-footer">
            <div class="pagination" id="customerPagination"></div>
            <button type="button" class="close-modal-btn" onclick="closeCustomerModal()">关闭</button>
        </div>
    </div>
</div>

<!-- 返回按钮 -->
<button type="button" onclick="goBack()" class="back-button">返回</button>

</body>
<script>
    function goBack() {
        window.location.href="../dashboard/dashboard_sales_manager.jsp"
    }

    // 清空表单和销售统计内容
    function clearForm() {
        // 清空表单字段
        document.getElementById('contractForm').reset();
        document.getElementById('customerInfo').value = '';
        document.getElementById('productInfo').value = '';
        document.getElementById('customerID').value = '';
        document.getElementById('productID').value = '';

        // 清空销售统计内容
        document.getElementById('totalSales').innerText = '';
        document.getElementById('customerSalesTable').innerHTML = '<tr><th>客户名称</th><th>销售额</th></tr>';
        document.getElementById('productSalesTable').innerHTML = '<tr><th>商品名称</th><th>销售额</th></tr>';
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
                // console.log(data)
                const table = document.getElementById('customerTable');
                table.innerHTML = '';
                if (data.elementList && data.elementList.length > 0) {
                    data.elementList.forEach(customer => {
                        // console.log(customer)
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>\${customer.customerID}</td>
                            <td>\${customer.customerName}</td>
                            <td><button onclick="selectCustomer('\${customer.customerID}', '\${customer.customerName}')">选择</button></td>
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

    function editProductInfo() {
        document.getElementById('productModal').style.display = 'block';
    }

    function closeProductModal() {
        document.getElementById('productModal').style.display = 'none';
    }

    function selectProduct(id, name) {
        document.getElementById('productID').value = id;
        document.getElementById('productInfo').value = name;
        closeProductModal();
    }

    function searchProduct(page = 1) {
        const keyword = document.getElementById('productSearchKeyword').value;
        fetch(`../ProductController?searchKeyword=\${encodeURIComponent(keyword)}&action=ajax&pageNum=\${page}`)
            .then(response => response.json())
            .then(data => {
                const table = document.getElementById('productTable');
                table.innerHTML = '';
                if (data.elementList && data.elementList.length > 0) {
                    data.elementList.forEach(product => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                        <td>\${product.productID}</td>
                        <td>\${product.productName}</td>
                        <td><button onclick="selectProduct('\${product.productID}', '\${product.productName}')">选择</button></td>
                    `;
                        table.appendChild(row);
                    });
                } else {
                    table.innerHTML = '<tr><td colspan="3">暂无商品信息</td></tr>';
                }
                updateProductPagination(data);
            })
            .catch(error => console.error('查询失败:', error));
    }

    function updateProductPagination(data) {
        const pagination = document.getElementById('productPagination');
        pagination.innerHTML = '';

        const totalPages = data.totalPages || 1;
        const currentPage = data.currentPage || 1;

        // 创建上一页按钮
        const prevButton = document.createElement('a');
        prevButton.href = '#';
        prevButton.textContent = '上一页';
        prevButton.className = currentPage === 1 ? 'disabled' : '';
        prevButton.onclick = (e) => {
            e.preventDefault();
            if (currentPage > 1) searchProduct(currentPage - 1);
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
                searchProduct(1);
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
                searchProduct(i);
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
                searchProduct(totalPages);
            };
            pagination.appendChild(lastPage);
        }

        // 创建下一页按钮
        const nextButton = document.createElement('a');
        nextButton.href = '#';
        nextButton.textContent = '下一页';
        nextButton.className = currentPage === totalPages ? 'disabled' : '';
        nextButton.onclick = (e) => {
            e.preventDefault();
            if (currentPage < totalPages) searchProduct(currentPage + 1);
        };
        pagination.appendChild(nextButton);

        // 创建跳转到指定页的控件
        const jumpTo = document.createElement('span');
        jumpTo.innerHTML = `
        跳转到: <input type="number" min="1" max="\${totalPages}" value="\${currentPage}" id="jumpToPage">
        <button onclick="searchProduct(document.getElementById('jumpToPage').value)" class="modal-jump-button">跳转</button>
    `;
        pagination.appendChild(jumpTo);
    }

    /* 页面加载的时候就加载一次 */
    window.onload=function(){
        searchCustomer();
        searchProduct();

        // 表单提交后处理函数
        document.getElementById("contractForm").onsubmit = function(event) {
            event.preventDefault(); // 阻止表单提交
            const formData = new FormData(this);

            // 获取日期范围并显示
            const startDate = document.getElementById('startDate').value;
            const endDate = document.getElementById('endDate').value;
            console.log(startDate);
            console.log(endDate);
            const dateRangeText = `从 \${startDate} 到 \${endDate}`;
            document.getElementById('dateRange').innerText = dateRangeText;  // 显示日期范围

            // 发送统计请求
            fetch('../ContractController', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',  // 明确指定请求内容为 JSON 格式
                },
                body: formData
            })
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                    // 处理返回的销售统计数据
                    document.getElementById("totalSales").innerText = "¥" + data.totalSales;

                    const customerSalesTable = document.getElementById("customerSalesTable");
                    customerSalesTable.innerHTML = "<tr><th>客户名称</th><th>销售额</th></tr>";
                    data.customerSales.forEach(sale => {
                        const row = document.createElement("tr");
                        row.innerHTML = `<td>\${sale.customerName}</td><td>¥\${sale.salesAmount}</td>`;
                        customerSalesTable.appendChild(row);
                    });

                    const productSalesTable = document.getElementById("productSalesTable");
                    productSalesTable.innerHTML = "<tr><th>商品名称</th><th>销售额</th></tr>";
                    data.productSales.forEach(sale => {
                        const row = document.createElement("tr");
                        row.innerHTML = `<td>\${sale.productName}</td><td>¥\${sale.salesAmount}</td>`;
                        productSalesTable.appendChild(row);
                    });
                })
                .catch(error => console.error('请求失败:', error));
        };
    }

</script>
</html>
