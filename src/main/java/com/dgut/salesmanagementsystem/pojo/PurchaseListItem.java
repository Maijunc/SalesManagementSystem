package com.dgut.salesmanagementsystem.pojo;

public class PurchaseListItem {
    private int purchaseListItemID; // 唯一标识
    private int purchaseListID;     // 采购清单ID（外键）
    private int productId;          // 商品ID（外键）
    private int quantity;           // 购买的商品数量

    public int getPurchaseListItemID() {
        return purchaseListItemID;
    }

    public void setPurchaseListItemID(int purchaseListItemID) {
        this.purchaseListItemID = purchaseListItemID;
    }

    public int getPurchaseListID() {
        return purchaseListID;
    }

    public void setPurchaseListID(int purchaseListID) {
        this.purchaseListID = purchaseListID;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

