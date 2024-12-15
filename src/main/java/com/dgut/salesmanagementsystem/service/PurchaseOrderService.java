package com.dgut.salesmanagementsystem.service;

import com.dgut.salesmanagementsystem.model.ProductDAO;
import com.dgut.salesmanagementsystem.model.PurchaseOrderDAO;
import com.dgut.salesmanagementsystem.pojo.Product;
import com.dgut.salesmanagementsystem.pojo.PurchaseOrder;
import com.dgut.salesmanagementsystem.pojo.PurchaseOrderStatus;

import java.util.List;

public class PurchaseOrderService {
    private PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
    private ProductDAO productDAO = new ProductDAO();

    public void checkAndCreatePurchaseOrder(int productID, int quantityToShip) {
        // 查询商品库存
        Product product = productDAO.getProductByID(productID);

        // 这样进货可以保证发完货后库存等于阀值
        if (product != null) {
            int currentStock = product.getStockQuantity();
            int lowStockThreshold = product.getLowStockThreshold();
            int remainingStock = currentStock - quantityToShip;

            // 如果库存不足或者发货后库存低于阈值，生成进货单
            if (remainingStock < lowStockThreshold) {
                int requiredQuantity = lowStockThreshold - remainingStock;
                PurchaseOrder purchaseOrder = new PurchaseOrder();
                purchaseOrder.setProductID(productID);
                purchaseOrder.setProductName(product.getProductName());
                purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.PENDING);
                purchaseOrder.setRequiredQuantity(requiredQuantity);
                purchaseOrderDAO.addPurchaseOrder(purchaseOrder);
            }
        }
    }

    // 获取分页数据
    public List<PurchaseOrder> getPurchaseOrdersByPage(int pageNum, int pageSize) {
        return purchaseOrderDAO.getPurchaseOrdersByPage(pageNum, pageSize);
    }


    // 获取进货单总页数
    public int getTotalPages(int pageSize) {
        int totalRecords =  purchaseOrderDAO.getPurchaseOrderCount();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        totalPages = totalPages > 0 ? totalPages : 1;
        return totalPages;
    }

    public PurchaseOrder getPurchaseOrderByID(Integer purchaseOrderID) {
        return purchaseOrderDAO.getPurchaseOrderByID(purchaseOrderID);
    }

    public boolean updatePurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderDAO.updatePurchaseOrder(purchaseOrder);
    }

    public boolean confirmPurchaseOrder(PurchaseOrder purchaseOrder) {
        boolean success = false;

        success = purchaseOrderDAO.updatePurchaseOrder(purchaseOrder);

        Product product = productDAO.getProductByID(purchaseOrder.getProductID());
        if (product == null) {
            throw new IllegalArgumentException("商品不存在");
        }

        int currentStock = product.getStockQuantity();
        // 更新库存
        int newStock = currentStock + purchaseOrder.getActualQuantity();
        success = productDAO.updateStock(product.getProductID(), newStock);
        return success;
    }
}
