package com.dgut.salesmanagementsystem.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ContractDetails {
    private String contractName;
    private Date startDate;
    private Date endDate;
    private Integer contractStatusInt;
    private String salesmanName;
    private String customerName;
    private Integer salesmanID;
    private Integer customerID;
    private List<ContractItem> contractItemList;

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getContractStatusInt() {
        return contractStatusInt;
    }

    public void setContractStatusInt(Integer contractStatusInt) {
        this.contractStatusInt = contractStatusInt;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
}
