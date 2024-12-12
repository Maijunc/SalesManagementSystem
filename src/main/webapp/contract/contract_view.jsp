<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>查看和编辑合同</title>
    <link rel="stylesheet" href="../css/add-contract.css">
    <style>
        .editable {
            background-color: #e6f7ff;
        }
    </style>
</head>
<body>

<div class="contract-details">
    <h2>合同详情</h2>
    <form id=contractForm action="../ContractController" method="post" onsubmit="return validateForm()">
        <input type="hidden" name="action" value="edit">
        <!-- 隐藏字段，用于提交合同ID -->
        <input type="hidden" id="contractID" name="contractID">

        <!-- 合同基本信息 -->
        <div>
            <label for="contractName">合同名称：</label>
            <input type="text" id="contractName" name="contractName" disabled required>
        </div>
        <div>
            <label for="startDate">合同开始日期：</label>
            <input type="date" id="startDate" name="startDate" disabled required>
        </div>
        <div>
            <label for="endDate">合同结束日期：</label>
            <input type="date" id="endDate" name="endDate" disabled required>
        </div>
        <div>
            <label for="contractStatus">合同状态：</label>
            <select id="contractStatus" name="contractStatus" disabled required>
                <option value="1">未开始</option>
                <option value="2">进行中</option>
                <option value="3">已完成</option>
            </select>
        </div>

        <!-- 客户选择 -->
        <div class="form-group">
            <label for="salesmanInfo">客户:</label>
            <input type="text" id="customerInfo" name="customerInfo" readonly disabled onclick="editCustomerInfo()"
                   placeholder="点击选择客户">
        </div>
        <!-- 隐藏字段，用于提交客户ID -->
        <input type="hidden" id="customerID" name="customerID">

        <!-- 销售人员选择 -->
        <div class="form-group">
            <label for="salesmanInfo">销售人员:</label>
            <input type="text" id="salesmanInfo" name="salesmanInfo" readonly disabled onclick="editSalesmanInfo()"
                   placeholder="点击选择销售人员">
        </div>
        <!-- 隐藏字段，用于提交销售人员ID -->
        <input type="hidden" id="salesmanID" name="salesmanID">

        <!-- 商品列表 -->
        <h3>商品列表</h3>
        <button id="selectProductButton" type="button" onclick="openProductModal()" style="display: none">选择商品</button>
        <table id="productTable">
            <thead>
            <tr>
                <th>商品ID</th>
                <th>商品名称</th>
                <th>数量</th>
                <th>单价</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <!-- 动态填充 -->
            </tbody>
        </table>
        <!-- 空列表提示 -->
        <p id="emptyMessage" style="display: none; text-align: center; color: #777;">当前没有选择任何商品。</p>

        <!-- 清空按钮 -->
        <div style="text-align: center; margin-top: 20px;">
            <button id="clearButton" type="button" onclick="clearAllProducts()" style="background-color: #ff4d4f; color: white; display: none">清空所有商品</button>
        </div>

        <!-- 编辑按钮 -->
        <button type="button" id="editButton" onclick="enableEdit()">编辑</button>
        <!-- 保存按钮 -->
        <button type="submit" id="saveButton" style="display: none;">保存</button>
        <!-- 返回按钮 -->
        <button type="button" onclick="back()" style="margin-top: 20px">返回</button>
    </form>
</div>

<!-- 商品选择模态框 -->
<div id="productModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeProductModal()">&times;</span>
        <h2>选择商品</h2>
        <form method="GET" id="searchProductForm">
            <input type="text" id="productSearchKeyword" placeholder="请输入商品名称或ID">
            <button type="button" onclick="searchProduct()" class="modal-search-button">查询</button>
        </form>
        <table id="productTableModal">
            <thead>
            <tr>
                <th>商品ID</th>
                <th>商品名称</th>
                <th>库存</th>
                <th>数量</th>
                <th>单价</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <!-- 动态填充 -->
            </tbody>
        </table>
        <!-- 分页控件 -->
        <div class="pagination" id="productPagination"></div>
    </div>
</div>


<%--销售人员选择模态框--%>
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

<%--客户选择模态框--%>
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
</body>
</html>

<script>
    function loadContractData(contractID) {
        // 使用AJAX进行请求
        fetch(`../ContractController?contractID=\${contractID}&action=ajax`)
            .then(response => response.json())
            .then(data => {
                // console.log(data);
                // 填充合同详情
                document.getElementById('contractName').value = data.contractName;
                // 创建一个新的 Date 对象，传入时间戳
                var startDate = new Date(data.startDate);
                var endDate = new Date(data.endDate);
                // 格式化为 YYYY-MM-DD 格式
                var startDateFormatted = startDate.toISOString().split('T')[0]; // 获取 ISO 字符串并分割，获取日期部分
                var endDateFormatted = endDate.toISOString().split('T')[0]; // 获取 ISO 字符串并分割，获取日期部分
                document.getElementById('startDate').value = startDateFormatted;
                document.getElementById('endDate').value = endDateFormatted;
                document.getElementById('contractStatus').value = data.contractStatusInt;
                document.getElementById('salesmanInfo').value = data.salesmanName;
                document.getElementById('customerInfo').value = data.customerName;
                document.getElementById('salesmanID').value = data.salesmanID;
                document.getElementById('customerID').value = data.customerID;
                document.getElementById('contractID').value = contractID;
                // 填充商品列表
                if(data.contractStatusInt != 1) {
                    document.getElementById('editButton').setAttribute('style', 'color: #A0A0A0; background-color: #E0E0E0; border: 1px solid #D0D0D0;');
                }

                const table = document.getElementById('productTable').querySelector('tbody');

                data.contractItemList.forEach(product => {
                    const row = document.createElement('tr');
                    // console.log(product);
                    // row.dataset.productID = productID; // 存储产品ID
                    row.innerHTML = `
                    <td>\${product.productID}<input type="hidden" name="products[\${product.productID}].productID" value="\${product.productID}"></td>
                    <td>\${product.productName}<input type="hidden" name="products[\${product.productID}].productName" value="\${product.productName}"></td>
                    <td contenteditable=false class="editable quantity-input" oninput="updateProductListSummary(); updateInputValue(this)" id="td-products[\${product.productID}].quantity">\${product.quantity}
                        <input type="hidden" name="products[\${product.productID}].quantity">
                    </td>
                    <td contenteditable=false class="editable unit-price-input" oninput="updateProductListSummary(); updateInputValue(this)" id="td-products[\${product.productID}].unitPrice">\${product.unitPrice}
                        <input type="hidden" name="products[\${product.productID}].unitPrice">
                    </td>
                    <td><button type="button" onclick="removeProduct(this)" style="pointer-events: none; color: #A0A0A0; background-color: #E0E0E0; border: 1px solid #D0D0D0;">删除</button></td>
                    `;
                    table.appendChild(row);

                    updateInputValue(document.getElementById(`td-products[\${product.productID}].quantity`));
                    updateInputValue(document.getElementById(`td-products[\${product.productID}].unitPrice`));
                });
                updateProductListSummary();
            })
            .catch(error => console.error('获取合同数据失败:', error));
    }

    // 启用编辑功能
    function enableEdit() {
        if(document.getElementById('contractStatus').value != 1) {
            alert("合同只有在履行前可以修改！");
            return;
        }
        // 启用所有输入字段
        const formElements = document.querySelectorAll('#contractForm input, #contractForm select');
        formElements.forEach(element => {
            element.disabled = false;
        });

        const formTdInputElements = document.querySelectorAll('#contractForm td.editable.quantity-input, #contractForm td.editable.unit-price-input');
        formTdInputElements.forEach(element => {
            element.setAttribute('contenteditable', 'true');  // 启用编辑
        });

        const formTdDeleteElements = document.querySelectorAll('button[type="button"][onclick="removeProduct(this)"]');
        formTdDeleteElements.forEach(element => {
            element.style = "";
        })


        searchCustomer();
        searchSalesman();
        searchProduct();

        // 显示保存按钮，隐藏编辑按钮
        document.getElementById('saveButton').style.display = 'inline-block';
        document.getElementById('editButton').style.display = 'none';
        document.getElementById('selectProductButton').style.display = 'inline-block';
        document.getElementById('clearButton').style.display = 'inline-block';
    }

    /*  ----------------销售人员选择----------------  */
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
                // console.log(data)
                const table = document.getElementById('salesmanTable');
                table.innerHTML = '';
                if (data.elementList && data.elementList.length > 0) {
                    data.elementList.forEach(salesman => {
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
                searchSalesman(1);
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
                searchSalesman(i);
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
                searchSalesman(totalPages);
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
            跳转到: <input type="number" min="1" max="\${totalPages}" value="\${currentPage}" id="jumpToPage" oninput="if(value>\${totalPages})value=\${totalPages};if(value<1)value=1;">
            <button onclick="searchSalesman(document.getElementById('jumpToPage').value)" class="modal-jump-button">跳转</button>
        `;
        pagination.appendChild(jumpTo);
    }

    /*  ----------------客户选择----------------  */
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
            跳转到: <input type="number" min="1" max="\${totalPages}" value="\${currentPage}" id="jumpToPage" oninput="if(value>\${totalPages})value=\${totalPages};if(value<1)value=1;">
            <button onclick="searchCustomer(document.getElementById('jumpToPage').value)" class="modal-jump-button">跳转</button>
        `;
        pagination.appendChild(jumpTo);
    }
    /*  ----------------商品选择----------------  */

    // 打开商品选择模态框
    function openProductModal() {
        document.getElementById('productModal').style.display = 'block';
    }

    // 关闭商品选择模态框
    function closeProductModal() {
        document.getElementById('productModal').style.display = 'none';
        updateProductListSummary();
    }

    // 查询商品
    function searchProduct(page = 1) {
        const keyword = document.getElementById('productSearchKeyword').value;
        fetch(`../ProductController?searchKeyword=\${encodeURIComponent(keyword)}&action=ajax&pageNum=\${page}`)
            .then(response => response.json())
            .then(data => {
                const table = document.getElementById('productTableModal').querySelector('tbody');
                // 先清空
                table.innerHTML = '';
                if (data.elementList.length > 0) {
                    data.elementList.forEach(product => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                        <td>\${product.productID}</td>
                        <td>\${product.productName}</td>
                        <td>\${product.stockQuantity}</td>
                        <td><input type="number" min="1" max="\${product.stock}" value="1" id="quantity-\${product.productID}"></td>
                        <td><input type="number" min="0" step="0.01" value="100" id="unitPrice-\${product.productID}"></td>
                        <td><button onclick="selectProduct('\${product.productID}', '\${product.productName}')">选择</button></td>
                    `;
                        table.appendChild(row);
                    });
                } else {
                    table.innerHTML = '<tr><td colspan="6">暂无商品信息</td></tr>';
                }
                updateProductPagination(data);
            })
            .catch(error => console.error('查询失败:', error));
    }
    // 选择商品
    function selectProduct(productID, productName) {
        // 动态绑定
        const quantityInput = document.getElementById(`quantity-\${productID}`);
        const unitPriceInput = document.getElementById(`unitPrice-\${productID}`);
        const quantity = parseInt(quantityInput.value, 10);
        const unitPrice = parseFloat(unitPriceInput.value);

        if (isNaN(quantity) || quantity <= 0 || isNaN(unitPrice) || unitPrice < 0) {
            alert('请输入有效的数量和单价！');
            return;
        }

        const table = document.getElementById('productTable').querySelector('tbody');
        // 在table中寻找单价
        const existingRow = Array.from(table.rows).find(row => row.cells[0].textContent === productID);

        if (existingRow) {
            alert('商品已存在，请直接修改数量或单价！');
        } else {
            const row = document.createElement('tr');
            row.dataset.productID = productID; // 存储产品ID
            row.innerHTML = `
        <td>\${productID}<input type="hidden" name="products[\${productID}].productID" value="\${productID}"></td>
        <td>\${productName}<input type="hidden" name="products[\${productID}].productName" value="\${productName}"></td>
        <td contenteditable="true" class="editable quantity-input" oninput="updateProductListSummary(); updateInputValue(this)" id="td-products[\${productID}].quantity" >\${quantity}
            <input type="hidden" name="products[\${productID}].quantity">
        </td>
        <td contenteditable="true" class="editable unit-price-input" oninput="updateProductListSummary(); updateInputValue(this)" id="td-products[\${productID}].unitPrice">\${unitPrice}
            <input type="hidden" name="products[\${productID}].unitPrice">
        </td>
        <td><button type="button" onclick="removeProduct(this)">删除</button></td>
    `;
            table.appendChild(row);
        }
        updateInputValue(document.getElementById(`td-products[\${productID}].quantity`));
        updateInputValue(document.getElementById(`td-products[\${productID}].unitPrice`));

        closeProductModal();
    }

    function updateProductPagination(data) {
        const pagination = document.getElementById('productPagination');
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
            if (currentPage > 1) searchProduct(currentPage - 1);
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

        const nextButton = document.createElement('a');
        nextButton.href = '#';
        nextButton.textContent = '下一页';
        nextButton.className = currentPage === totalPages ? 'disabled' : '';
        nextButton.onclick = (e) => {
            e.preventDefault();
            if (currentPage < totalPages) searchProduct(currentPage + 1);
        };
        pagination.appendChild(nextButton);

        const jumpTo = document.createElement('span');
        jumpTo.innerHTML = `
            跳转到: <input type="number" min="1" max="\${totalPages}" value="\${currentPage}" id="jumpToPage" oninput="if(value>\${totalPages})value=\${totalPages};if(value<1)value=1;">
            <button onclick="searchProduct(document.getElementById('jumpToPage').value)" class="modal-jump-button">跳转</button>
        `;
        pagination.appendChild(jumpTo);
    }

    // 删除商品
    function removeProduct(button) {
        const row = button.parentElement.parentElement;
        row.parentElement.removeChild(row);
    }

    // 清空所有产品
    function clearAllProducts() {
        if (confirm("确认要清空所有商品吗？")) {
            // 获取表格体并清空内容
            const productTableBody = document.getElementById("productTable").querySelector("tbody");
            productTableBody.innerHTML = '';
            updateProductListSummary();
        }
    }

    // 更新产品列表汇总信息（如总计金额等，可以扩展）
    function updateProductListSummary() {
        const productTableBody = document.getElementById("productTable").querySelector("tbody");
        if (productTableBody.children.length === 0) {
            document.getElementById("emptyMessage").style.display = "block";
        } else {
            document.getElementById("emptyMessage").style.display = "none";
        }

        // console.log("productTableBody.children.length = " + productTableBody.children.length);
    }

    document.addEventListener("DOMContentLoaded", () => {
        const productTable = document.getElementById("productTable");

        // 为数量和单价单元格添加提示
        productTable.addEventListener("focusin", (event) => {
            const target = event.target;
            if (target.classList.contains("editable")) {
                target.style.backgroundColor = "#e6f7ff";
                target.title = "点击后可编辑";
            }
        });

        // 失去焦点时保存修改并恢复样式
        productTable.addEventListener("focusout", (event) => {
            const target = event.target;
            if (target.classList.contains("editable")) {
                target.style.backgroundColor = "";
            }
        });
    });

    function updateInputValue(td) {
        // 获取 td 中的内容
        // console.log(td.innerText);
        // console.log(td.textContent);

        const newValue = td.innerText.trim();

        // 获取 td 下的 input 元素
        const input = td.querySelector('input[type="hidden"]');

        // 如果 input 存在，更新其值
        if (input) {
            input.value = newValue;
        }

        // console.log(input.value);
    }

    function validateForm() {
        // 验证合同名称
        const contractName = document.getElementById("contractName");
        if (contractName.value.trim() === "") {
            alert("合同名称不能为空！");
            contractName.focus();
            return false; // 阻止表单提交
        }

        // 验证合同开始日期和结束日期
        const startDate = new Date(document.getElementById("startDate").value);
        const endDate = new Date(document.getElementById("endDate").value);

        if (startDate > endDate) {
            alert("合同开始日期不能晚于结束日期！");
            return false; // 阻止表单提交
        }

        // 验证合同状态
        const contractStatus = document.getElementById("contractStatus");
        if (contractStatus.value === "") {
            alert("请选择合同状态！");
            contractStatus.focus();
            return false; // 阻止表单提交
        }

        // 验证客户和销售人员信息
        const customerID = document.getElementById("customerID").value;
        const salesmanID = document.getElementById("salesmanID").value;

        if (customerID === "") {
            alert("请选择客户！");
            return false; // 阻止表单提交
        }

        if (salesmanID === "") {
            alert("请选择销售人员！");
            return false; // 阻止表单提交
        }
        // console.log("customerID = " + customerID);
        // console.log("salesmanID = " + salesmanID);
        // console.log("contractID = " + contractID);

        // 验证商品列表
        const productTable = document.getElementById("productTable").getElementsByTagName("tbody")[0];
        const rows = productTable.getElementsByTagName("tr");
        let hasInvalidProducts = false;

        for (let i = 0; i < rows.length; i++) {
            const quantity = parseFloat(rows[i].cells[2].innerText.trim());
            const unitPrice = parseFloat(rows[i].cells[3].innerText.trim());

            // 验证 quantity 是否为整数
            if (isNaN(quantity) || quantity < 0 || !Number.isInteger(quantity)) {
                alert("商品数量必须是一个不小于 0 的整数！");
                hasInvalidProducts = true;
                break; // 跳出循环
            }

            // 验证单价
            if (isNaN(unitPrice) || unitPrice < 0) {
                alert("商品单价必须是一个不小于 0 的数字！");
                hasInvalidProducts = true;
                break; // 跳出循环
            }
        }

        if (hasInvalidProducts) {
            return false; // 阻止表单提交
        }

        // 如果所有验证都通过，返回 true 允许提交
        return true;
    }

    function back() {
        window.location.href = "../ContractController?pageNum=1";
    }

    // 初始化合同数据
    window.onload = function() {
        var contractID = <%= request.getParameter("contractID") %>; // 直接将 contractID 插入到 JavaScript 变量中
        loadContractData(contractID);
    };
</script>
</body>
</html>
