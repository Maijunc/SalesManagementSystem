<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>新增采购清单</title>
    <link rel="stylesheet" type="text/css" href="../css/add-purchase-list.css">
</head>
<body>
<header class="page-header">
    <a href="purchaseList_management.jsp" class="back-link">返回</a> <!-- 返回按钮 -->
    <h1>新增采购清单</h1>
</header>


<form action="../PurchaseListController" method="post" onsubmit="return validateForm()">
    <input type="hidden" name="action" value="add">

    <!-- 商品列表 -->
    <h3>商品列表</h3>
    <button type="button" onclick="openProductModal()">选择商品</button>
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
        <button type="button" onclick="clearAllProducts()" style="background-color: #ff4d4f; color: white;">清空所有商品</button>
    </div>

    <div>
        <button type="submit">保存采购清单</button>
    </div>
</form>

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
                <th>数量上限</th>
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

<script>
    let productData = {}; // 用于存储商品的 ID 和剩余数量
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
        fetch(`../PurchaseListController?searchKeyword=\${encodeURIComponent(keyword)}&action=ajax&pageNum=\${page}&contractID=<%= request.getParameter("contractID") %>`)
            .then(response => response.json())
            .then(data => {
                const table = document.getElementById('productTableModal').querySelector('tbody');
                // 先清空
                table.innerHTML = '';
                if (data.elementList.length > 0) {
                    data.elementList.forEach(product => {
                        // 存储商品数据
                        productData[product.productID] = {
                            remainingQuantity: product.remainingQuantity,
                            originalRemainingQuantity: product.remainingQuantity, // 用于计算恢复
                        };

                        const row = document.createElement('tr');
                        row.innerHTML = `
                        <td>\${product.productID}</td>
                        <td>\${product.productName}</td>
                        <td>\${product.stockQuantity}</td>
                        <td id="remaining-\${product.productID}">\${product.remainingQuantity}</td>
                        <td><input type="number" min="1" max="\${product.remainingQuantity}" value="1" id="quantity-\${product.productID}" oninput="validateQuantity(this)" </td>
                        <td>\${product.unitPrice}<input type="number" min="0" step="0.01" value="\${product.unitPrice}" id="unitPrice-\${product.productID}" style="display: none"</td>
                        <td><button onclick="selectProduct('\${product.productID}', '\${product.productName}')">选择</button></td>
                    `;
                        table.appendChild(row);
                    });
                } else {
                    table.innerHTML = '<tr><td colspan="7">暂无商品信息</td></tr>';
                }
                updateProductPagination(data);
            })
            .catch(error => console.error('查询失败:', error));
    }

    // 选择商品
    function selectProduct(productID, productName) {
        // 动态绑定
        const quantityInput = document.getElementById(`quantity-\${productID}`);
        const quantity = parseInt(quantityInput.value, 10);

        if (quantity > productData[productID].remainingQuantity) {
            alert('超出剩余数量！');
            return;
        }

        const unitPriceInput = document.getElementById(`unitPrice-\${productID}`);
        const maxQuantity = parseInt(quantityInput.max, 10); // 从 input 属性获取最大值
        const unitPrice = parseFloat(unitPriceInput.value);

        if (isNaN(quantity) || quantity <= 0 || quantity > maxQuantity) {
            alert('\`请输入有效的数量（1-\${maxQuantity}\`');
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
        <td contenteditable="true" class="editable quantity-input" oninput="updateProductListSummary(); updateInputValue(this)" id="td-products[\${productID}].quantity">\${quantity}
            <input type="hidden" name="products[\${productID}].quantity">
        </td>
        <td contenteditable="false" class="unit-price-input" oninput="updateProductListSummary(); updateInputValue(this)" id="td-products[\${productID}].unitPrice">\${unitPrice}
            <input type="hidden" name="products[\${productID}].unitPrice">
        </td>
        <td><button type="button" onclick="removeProduct(this, \${productID})">删除</button></td>
    `;
            table.appendChild(row);

            // 更新剩余数量
            productData[productID].remainingQuantity -= quantity;
            document.getElementById(`remaining-\${productID}`).textContent = productData[productID].remainingQuantity;
            // document.getElementById('quantity-\${productID}').max = parseInt(productData[productID].remainingQuantity);

        }
        updateInputValue(document.getElementById(`td-products[\${productID}].quantity`));
        updateInputValue(document.getElementById(`td-products[\${productID}].unitPrice`));

        closeProductModal();
    }

    // 如果进行了查询要修改切换页面的相关组件
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
            跳转到: <input type="number" min="1" max="\${totalPages}" value="\${currentPage}" id="jumpToPage" class="jumpToPage" oninput="if(value>\${totalPages})value=\${totalPages};if(value<1)value=1;">
            <button onclick="searchProduct(document.getElementById('jumpToPage').value)" class="modal-jump-button">跳转</button>
        `;
        pagination.appendChild(jumpTo);
    }

    // 删除商品
    function removeProduct(button, productID) {
        const row = button.parentElement.parentElement;
        const quantity = parseInt(row.cells[2].textContent, 10);

        // 恢复剩余数量
        productData[productID].remainingQuantity += quantity;
        document.getElementById(`remaining-\${productID}`).textContent = productData[productID].remainingQuantity;
        // document.getElementById('quantity-\${productID}').max = parseInt(productData[productID].remainingQuantity);
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
    }

    function updateInputValue(td) {
        const newValue = td.innerText.trim();

        // 获取 td 下的 input 元素
        const input = td.querySelector('input[type="hidden"]');

        // 如果 input 存在，更新其值
        if (input) {
            input.value = newValue;
        }
    }

    function validateForm() {
        const productTableBody = document.getElementById("productTable").querySelector("tbody");
        if (productTableBody.children.length === 0) {
            alert("采购清单的商品不能为空");
            return false;
        }

        // 验证商品列表
        const productTable = document.getElementById("productTable").getElementsByTagName("tbody")[0];
        const rows = productTable.getElementsByTagName("tr");
        let hasInvalidProducts = false;

        for (let i = 0; i < rows.length; i++) {
            const quantityText = rows[i].cells[2].innerText.trim();
            console.log('rows[i].cells[2].innerText.trim() = ' + quantityText);

            // 先用 parseInt 转换，再用 isNaN 判断转换是否失败
            const quantity = parseFloat(quantityText);

            // 验证数量
            if (isNaN(quantity) || quantity < 0 || !Number.isInteger(quantity)) {
                alert("商品数量必须是一个不小于 0 的整数！");
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

    function validateQuantity(input) {
        const max = parseInt(input.max, 10);
        const value = parseInt(input.value, 10);

        if (value > max) {
            input.value = max; // 如果超出范围，自动调整为最大值
            alert("数量不能超过最大值：" + max);
        } else if (value < 1 || isNaN(value)) {
            input.value = 1; // 如果小于最小值，自动调整为 1
        }
    }

    /* 页面加载的时候就加载一次 */
    window.onload=function(){
        searchProduct();
    }

</script>

</body>
</html>
