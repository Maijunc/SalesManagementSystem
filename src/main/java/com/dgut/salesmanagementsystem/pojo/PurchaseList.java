package com.dgut.salesmanagementsystem.pojo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

// 采购清单
public class PurchaseList {
    private int purchaseListID;  // 采购清单唯一标识
    private int contractID;      // 合同ID，外键
    private BigDecimal totalPrice;  // 总价格
    private String paymentStatus; // 付款状态
    private Timestamp createDate;     // 生成日期
    private List<PurchaseListItem> purchaseListItems;

    public int getPurchaseListID() {
        return purchaseListID;
    }

    public void setPurchaseListID(int purchaseListID) {
        this.purchaseListID = purchaseListID;
    }

    public int getContractID() {
        return contractID;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public List<PurchaseListItem> getPurchaseListItems() {
        return purchaseListItems;
    }

    public void setPurchaseListItems(List<PurchaseListItem> purchaseListItems) {
        this.purchaseListItems = purchaseListItems;
    }
}
