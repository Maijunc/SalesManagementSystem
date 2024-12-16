package com.dgut.salesmanagementsystem.pojo;

import java.math.BigDecimal;

public class CustomerSales {
    private String customerName;
    private BigDecimal salesAmount;

    // Constructor, Getters and Setters

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount) {
        this.salesAmount = salesAmount;
    }

    public CustomerSales(String customerName, BigDecimal salesAmount) {
        this.customerName = customerName;
        this.salesAmount = salesAmount;
    }

    public CustomerSales() {
    }

    @Override
    public String toString() {
        return "CustomerSales{" +
                "customerName='" + customerName + '\'' +
                ", salesAmount=" + salesAmount +
                '}';
    }
}