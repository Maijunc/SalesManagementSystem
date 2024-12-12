package com.dgut.salesmanagementsystem.pojo;

import java.math.BigDecimal;

public class PurchaseListItem {
    private int purchaseListItemID; // 唯一标识
    private int purchaseListID;     // 采购清单ID（外键）
    private int productID;          // 商品ID（外键）
    private String productName;     // 商品名称
    private int quantity;           // 购买的商品数量
    private BigDecimal unitPrice; //单价

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

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}

