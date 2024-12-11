package com.dgut.salesmanagementsystem.pojo;

public enum PaymentStatus {
    PENDING("Pending"),
    PAID("Paid");

    private String value;
    // value 存的是上面括号内的值，比如“Active"
    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
