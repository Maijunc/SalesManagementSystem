package com.dgut.salesmanagementsystem.pojo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Contract {

    /*  BigDecimal在进行入库时, 数据库选择decimal类型, 长度可以自定义, 如18;
     小数点我们项目中用的是2, 保留2位小数. 此外还要注意的就是默认值,
     一定写成0.00, 不要用默认的NULL, 否则在进行加减排序等操作时, 会带来转换的麻烦!
    */
    private Integer contractID;  // 合同ID
    private String contractName; // 合同名称
    private Timestamp contractDate;   // 合同签订日期
    private Timestamp startDate;      // 合同开始日期
    private Timestamp endDate;        // 合同结束日期
    private ContractStatus contractStatus; // 合同状态
    private BigDecimal totalAmount; // 合同总金额
    private BigDecimal paidAmount;  // 已付款金额
    private BigDecimal remainingAmount; // 未付款金额
    private Integer customerID;    // 客户ID
    private Integer salesmanID;    // 销售人员ID
    private List<ContractItem> contractItemList; //合同中涉及的商品列表

    public Integer getContractID() {
        return contractID;
    }

    public void setContractID(Integer contractID) {
        this.contractID = contractID;
    }

    public Timestamp getContractDate() {
        return contractDate;
    }

    public void setContractDate(Timestamp contractDate) {
        this.contractDate = contractDate;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Integer getSalesmanID() {
        return salesmanID;
    }

    public void setSalesmanID(Integer salesmanID) {
        this.salesmanID = salesmanID;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public List<ContractItem> getContractItemList() {
        return contractItemList;
    }

    public void setContractItemList(List<ContractItem> contractItemList) {
        this.contractItemList = contractItemList;
    }
}