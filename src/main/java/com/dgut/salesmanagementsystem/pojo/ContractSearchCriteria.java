package com.dgut.salesmanagementsystem.pojo;

public class ContractSearchCriteria {
    private String contractName;
    private Integer contractID;
    private String status;
    private String startDateStr;
    private String endDateStr;

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


    public ContractSearchCriteria(String contractName, Integer contractID, String status, String startDateStr, String endDateStr) {
        this.contractName = contractName;
        this.contractID = contractID;
        this.status = status;
        this.startDateStr = startDateStr;
        this.endDateStr = endDateStr;
    }

    public ContractSearchCriteria() {
    }
}
