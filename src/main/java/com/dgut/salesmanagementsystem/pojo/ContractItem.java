package com.dgut.salesmanagementsystem.pojo;

import java.math.BigDecimal;

public class ContractItem {
    private Integer contractItemID;
    private Integer productID;
    private Integer contractID;
    private String productName;
    private Integer quantity; //数量
    private BigDecimal totalPrice; //总价
    private BigDecimal unitPrice; //单价

    public Integer getContractItemID() {
        return contractItemID;
    }

    public void setContractItemID(Integer contractItemID) {
        this.contractItemID = contractItemID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getContractID() {
        return contractID;
    }

    public void setContractID(Integer contractID) {
        this.contractID = contractID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ContractItem() {
    }

    public ContractItem(Integer contractItemID, Integer productID, Integer contractID, String productName, Integer quantity, BigDecimal totalPrice, BigDecimal unitPrice) {
        this.contractItemID = contractItemID;
        this.productID = productID;
        this.contractID = contractID;
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.unitPrice = unitPrice;
    }
}
