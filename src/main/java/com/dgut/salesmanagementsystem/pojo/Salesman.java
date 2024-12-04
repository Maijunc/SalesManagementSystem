package com.dgut.salesmanagementsystem.pojo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Salesman {

    /*
    salesperson_id（销售人员ID）：唯一标识销售人员的ID，通常为一个数字或字符串。
    name（姓名）：销售人员的名字。
    contact_info（联系方式）：包括电子邮件、电话等联系方式。
    contracts（签订的合同列表）：销售人员签订的所有合同，通常是一个合同对象的集合。可以与Contract实体类建立一对多关系。
    total_sales（总销售额）：销售人员在一定时间段内的总销售额（可以计算或者动态生成）。
    performance_stats（销售业绩统计）：销售人员的销售业绩数据（比如，按月、季度、年度统计的销售额）。
    commission（佣金）：销售人员的佣金，通常根据销售额来计算。
     */
    private int salesmanID;            // 唯一标识销售人员的ID
    private String name;                  // 销售人员的姓名
    private ContactInfo contactInfo;  // 销售人员的联系方式，存储为Map，适应JSON格式
    private List<Contract> contracts;
    private BigDecimal totalSales;        // 总销售额
    private PerformanceStats performanceStats; // 销售业绩统计，存储为Map，适应JSON格式
    private BigDecimal commission;        // 销售人员佣金

    private String ContactInfoJson; //Json形式的联系方式

    private String PerformanceStatsJson; //Json形式的联系方式

    public String getName() {
        return name;
    }



    public List<Contract> getContracts() {
        return contracts;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public BigDecimal getCommission() {
        return commission;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public PerformanceStats getPerformanceStats() {
        return performanceStats;
    }

    public void setPerformanceStats(PerformanceStats performanceStats) {
        this.performanceStats = performanceStats;
    }

    public int getSalesmanID() {
        return salesmanID;
    }

    public void setSalesmanID(int salesmanID) {
        this.salesmanID = salesmanID;
    }

    public String getContactInfoJson() {
        return ContactInfoJson;
    }

    public void setContactInfoJson(String contactInfoJson) {
        ContactInfoJson = contactInfoJson;
    }

    public String getPerformanceStatsJson() {
        return PerformanceStatsJson;
    }

    public void setPerformanceStatsJson(String performanceStatsJson) {
        PerformanceStatsJson = performanceStatsJson;
    }

    public Salesman() {
    }

    public Salesman(int salesmanID, String name, ContactInfo contactInfo, List<Contract> contracts, BigDecimal totalSales, PerformanceStats performanceStats, BigDecimal commission, String contactInfoJson, String performanceStatsJson) {
        this.salesmanID = salesmanID;
        this.name = name;
        this.contactInfo = contactInfo;
        this.contracts = contracts;
        this.totalSales = totalSales;
        this.performanceStats = performanceStats;
        this.commission = commission;
        ContactInfoJson = contactInfoJson;
        PerformanceStatsJson = performanceStatsJson;
    }



}
