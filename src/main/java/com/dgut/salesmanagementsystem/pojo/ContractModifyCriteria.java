package com.dgut.salesmanagementsystem.pojo;

import java.math.BigDecimal;
import java.util.List;

public class ContractModifyCriteria {
    private String contractName;
    private Integer contractID;
    private String status;
    private String startDateStr;
    private String endDateStr;
    private List<ContractItem> contractItemList;
    private Integer salesmanID;
    private Integer customerID;
    private BigDecimal totalPrice; //总价

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Integer getContractID() {
        return contractID;
    }

    public void setContractID(Integer contractID) {
        this.contractID = contractID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public List<ContractItem> getContractItemList() {
        return contractItemList;
    }

    public void setContractItemList(List<ContractItem> contractItemList) {
        this.contractItemList = contractItemList;
    }

    public Integer getSalesmanID() {
        return salesmanID;
    }

    public void setSalesmanID(Integer salesmanID) {
        this.salesmanID = salesmanID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
