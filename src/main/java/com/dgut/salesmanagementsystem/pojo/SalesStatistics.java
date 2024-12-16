package com.dgut.salesmanagementsystem.pojo;

import java.math.BigDecimal;
import java.util.List;

public class SalesStatistics {
    private BigDecimal totalSales;
    private List<CustomerSales> customerSales;
    private List<ProductSales> productSales;

    // Getters and Setters

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public List<CustomerSales> getCustomerSales() {
        return customerSales;
    }

    public void setCustomerSales(List<CustomerSales> customerSales) {
        this.customerSales = customerSales;
    }

    public List<ProductSales> getProductSales() {
        return productSales;
    }

    public void setProductSales(List<ProductSales> productSales) {
        this.productSales = productSales;
    }

    @Override
    public String toString() {
        return "SalesStatistics{" +
                "totalSales=" + totalSales +
                ", customerSales=" + customerSales +
                ", productSales=" + productSales +
                '}';
    }
}