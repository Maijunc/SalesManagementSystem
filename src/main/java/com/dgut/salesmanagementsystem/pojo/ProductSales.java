package com.dgut.salesmanagementsystem.pojo;

import java.math.BigDecimal;

public class ProductSales {
    private String productName;
    private BigDecimal salesAmount;

    // Constructor, Getters and Setters

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount) {
        this.salesAmount = salesAmount;
    }

    public ProductSales(String productName, BigDecimal salesAmount) {
        this.productName = productName;
        this.salesAmount = salesAmount;
    }

    public ProductSales() {
    }

    @Override
    public String toString() {
        return "ProductSales{" +
                "productName='" + productName + '\'' +
                ", salesAmount=" + salesAmount +
                '}';
    }
}
