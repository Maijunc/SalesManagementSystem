<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>查看采购清单</title>
  <link rel="stylesheet" type="text/css" href="../css/purchase-list-view.css">
</head>
<body>
<header class="page-header">
  <a href="purchaseList_management.jsp" class="back-link">返回</a> <!-- 返回按钮 -->
  <h1>查看采购清单</h1>
</header>

<!-- 显示采购清单的基本信息 -->
<section class="purchase-info">
  <h3>采购清单基本信息</h3>
  <table class="info-table">
    <tr>
      <th>清单编号：</th>
      <td id="purchaseListID"><%=request.getParameter("purchaseListID")%></td>
    </tr>
    <tr>
      <th>合同编号：</th>
      <td id="contractID"></td>
    </tr>
    <tr>
      <th>创建日期：</th>
      <td id="creationDate"></td>
    </tr>
    <tr>
      <th>总金额：</th>
      <td id="totalAmount"></td>
    </tr>
  </table>
</section>

<!-- 显示商品列表 -->
<section class="product-list">
  <h3>商品列表</h3>
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
  <p id="emptyMessage" style="display: none; text-align: center; color: #777;">当前没有商品。</p>
  <div id="productPagination" class="pagination"></div>
</section>

<script>
  // 页面加载时自动加载采购清单信息
  window.onload = function () {
    const purchaseListID = <%=request.getParameter("purchaseListID")%>;
    fetch(`../PurchaseListController?action=view&purchaseListID=\${purchaseListID}`)
            .then(response => response.json())
            .then(data => {
              // 填充基本信息
              console.log(data);
              document.getElementById('contractID').textContent = data.elementToPass.contractID;
              // 创建一个新的 Date 对象，传入时间戳
              var createDate = new Date(data.elementToPass.createDate);
              // 格式化为 YYYY-MM-DD 格式
              // 使用 toLocaleString 格式化为 YYYY/MM/DD, HH:mm:ss
              let createDateFormatted = createDate.toLocaleString('sv-SE', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit',
                hour12: false  // 24小时制
              });

              // 这里的 'sv-SE'（瑞典）是为了强制输出 YYYY/MM/DD 格式
              createDateFormatted = createDateFormatted.replace(',', ''); // 移除日期和时间之间的逗号
              createDateFormatted = createDateFormatted.replace(/\//g, '-'); // 如果需要 '/'，可以替换
              document.getElementById('creationDate').textContent = createDateFormatted;
              document.getElementById('totalAmount').textContent = data.elementToPass.totalPrice;

              // 填充商品列表
              const productTable = document.getElementById('productTable').querySelector('tbody');
              if (data.elementToPass.purchaseListItems && data.elementToPass.purchaseListItems.length > 0) {
                data.elementToPass.purchaseListItems.forEach(product => {
                  const row = document.createElement('tr');
                  row.innerHTML = `
                            <td>\${product.productID}</td>
                            <td>\${product.productName}</td>
                            <td>\${product.quantity}</td>
                            <td>\${product.unitPrice}</td>
                            <td>
                            <button class="btn-generate-ship-order" onclick="generateShipOrder('<%= request.getParameter("purchaseListID") %>')">生成发货单</button>
                            </td>
                        `;
                  productTable.appendChild(row);
                });
                const pagination = {
                  totalPages : data.totalPages,
                  currentPage : data.currentPage,
                }

                updateProductPagination(pagination);
              } else {
                document.getElementById('emptyMessage').style.display = 'block';
              }
            })
            .catch(error => console.error('加载失败:', error));
  };

  // 更新分页信息
  function updateProductPagination(pagination) {
    const paginationContainer = document.getElementById('productPagination');
    paginationContainer.innerHTML = '';

    const totalPages = pagination.totalPages || 1;
    const currentPage = pagination.currentPage || 1;

    if (currentPage > 1) {
      const prevButton = document.createElement('a');
      prevButton.href = '#';
      prevButton.textContent = '上一页';
      prevButton.onclick = (e) => {
        e.preventDefault();
        loadPage(currentPage - 1);
      };
      paginationContainer.appendChild(prevButton);
    }

    for (let i = 1; i <= totalPages; i++) {
      const pageLink = document.createElement('a');
      pageLink.href = '#';
      pageLink.textContent = i;
      if (i === currentPage) pageLink.classList.add('active');
      pageLink.onclick = (e) => {
        e.preventDefault();
        loadPage(i);
      };
      paginationContainer.appendChild(pageLink);
    }

    if (currentPage < totalPages) {
      const nextButton = document.createElement('a');
      nextButton.href = '#';
      nextButton.textContent = '下一页';
      nextButton.onclick = (e) => {
        e.preventDefault();
        loadPage(currentPage + 1);
      };
      paginationContainer.appendChild(nextButton);
    }
  }

  // 加载指定页的数据
  function loadPage(page) {
    const purchaseListID = <%=request.getParameter("purchaseListID")%>;
    fetch(`../PurchaseListController?action=view&purchaseListID=\${purchaseListID}&pageNum=\${page}`)
            .then(response => response.json())
            .then(data => {
              const productTable = document.getElementById('productTable').querySelector('tbody');
              productTable.innerHTML = '';
              if (data.elementToPass.purchaseListItems && data.elementToPass.purchaseListItems.length > 0) {
                data.elementToPass.purchaseListItems.forEach(product => {
                  const row = document.createElement('tr');
                  row.innerHTML = `
                            <td>\${product.productID}</td>
                            <td>\${product.productName}</td>
                            <td>\${product.quantity}</td>
                            <td>\${product.unitPrice}</td>
                            <td>
                              <button class="btn-generate-ship-order" onclick="generateShipOrder('<%= request.getParameter("purchaseListID") %>')">生成发货单</button>
                            </td>
                            `;
                  productTable.appendChild(row);
                });
              } else {
                document.getElementById('emptyMessage').style.display = 'block';
              }
              const pagination = {
                totalPages : data.totalPages,
                currentPage : data.currentPage,
              }

              updateProductPagination(pagination);
            })
            .catch(error => console.error('加载失败:', error));
  }

  function generateShipOrder(purchaseListID) {
    const confirmed = confirm("确定要生成发货单吗？");
    if (confirmed) {
      window.location.href = `../PurchaseListController?action=addShipOrder&purchaseListID=\${purchaseListID}`;
    }
  }

</script>

</body>
</html>
