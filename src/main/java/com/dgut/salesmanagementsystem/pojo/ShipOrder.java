package com.dgut.salesmanagementsystem.pojo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

// 发货单 一张发货单只对应一个商品
public class ShipOrder {
    private int shipOrderID; //发货单ID
    private int productID; //商品ID
    private String productName; //商品名称
    private BigDecimal unitPrice; //商品单价
    private int quantity; //发货商品的数量
    private int purchaseListID; //采购清单ID 这张发货单对应哪个采购清单
    private int purchaseListItemID;  // 采购清单商品列表ID， 一个purchaseListItemID 对应唯一一个ShipOrder
    private ShipOrderStatus shipOrderStatus; //发货单状态
    private Timestamp shipDate; //发货时间
    private int customerID; //客户编号
    private String customerName; //客户名称
    private String recipientName; //收货人姓名
    private String shippingAddress; //收货地址
    private String recipientPhone; //收货人电话
    private String shippingCompany; //物流公司
    private String trackingNumber; //物流单号
    private BigDecimal totalAmount; // 总金额
    private String shippedBy; //发货人
    private String notes; //备注
    private boolean PurchaseOrderGenerated; // 是否已生成进货单
    private Timestamp createdAt; //创建时间
    private Timestamp updatedAt; //更新时间

    public int getShipOrderID() {
        return shipOrderID;
    }

    public void setShipOrderID(int shipOrderID) {
        this.shipOrderID = shipOrderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPurchaseListID() {
        return purchaseListID;
    }

    public void setPurchaseListID(int purchaseListID) {
        this.purchaseListID = purchaseListID;
    }

    public ShipOrderStatus getShipOrderStatus() {
        return shipOrderStatus;
    }

    public void setShipOrderStatus(ShipOrderStatus shipOrderStatus) {
        this.shipOrderStatus = shipOrderStatus;
    }

    public Timestamp getShipDate() {
        return shipDate;
    }

    public void setShipDate(Timestamp shipDate) {
        this.shipDate = shipDate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippedBy() {
        return shippedBy;
    }

    public void setShippedBy(String shippedBy) {
        this.shippedBy = shippedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPurchaseListItemID() {
        return purchaseListItemID;
    }

    public void setPurchaseListItemID(int purchaseListItemID) {
        this.purchaseListItemID = purchaseListItemID;
    }

    public boolean isPurchaseOrderGenerated() {
        return PurchaseOrderGenerated;
    }

    public void setPurchaseOrderGenerated(boolean purchaseOrderGenerated) {
        PurchaseOrderGenerated = purchaseOrderGenerated;
    }
}
