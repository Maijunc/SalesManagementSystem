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
    private int salespersonId;            // 唯一标识销售人员的ID
    private String name;                  // 销售人员的姓名
    private Map<String, Object> contactInfo;  // 销售人员的联系方式，存储为Map，适应JSON格式
    private List<Contract> contracts;
    private BigDecimal totalSales;        // 总销售额
    private Map<String, Object> performanceStats; // 销售业绩统计，存储为Map，适应JSON格式
    private BigDecimal commission;        // 销售人员佣金

    public int getSalespersonId() {
        return salespersonId;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getContactInfo() {
        return contactInfo;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public Map<String, Object> getPerformanceStats() {
        return performanceStats;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setSalespersonId(int salespersonId) {
        this.salespersonId = salespersonId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactInfo(Map<String, Object> contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public void setPerformanceStats(Map<String, Object> performanceStats) {
        this.performanceStats = performanceStats;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }
}
