package com.dgut.salesmanagementsystem.pojo;

import java.util.Date;

public class Customer {
    // 客户标识信息
    private int customerID;             // 客户唯一标识符
    private String customerName;        // 客户名称
    private String contactPerson;       // 联系人姓名
    private String phone;         // 联系人信息（电话）
    private String email;         // 联系人信息（邮箱）


    // 客户地址信息
    private String address;             // 客户详细地址
    private String city;                // 所在城市
    private String postalCode;          // 邮政编码
    private String country;             // 国家/地区

    // 客户状态信息
    private String customerType;        // 客户类型 (Individual 或 Company)
    private String customerStatus;      // 客户状态 (Active, Paused, Blacklisted)
    private Date createdDate;         // 创建日期
    private Date lastModifiedDate;    // 最后修改日期

    // 业务关联信息
    private double totalSalesAmount;    // 累计销售金额
    private String lastPurchaseDate;    // 最后采购日期

    // Getters and Setters (略，为简化可以使用 Lombok 生成)

    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getCustomerType() {
        return customerType;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public double getTotalSalesAmount() {
        return totalSalesAmount;
    }

    public String getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setTotalSalesAmount(double totalSalesAmount) {
        this.totalSalesAmount = totalSalesAmount;
    }

    public void setLastPurchaseDate(String lastPurchaseDate) {
        this.lastPurchaseDate = lastPurchaseDate;
    }
}
