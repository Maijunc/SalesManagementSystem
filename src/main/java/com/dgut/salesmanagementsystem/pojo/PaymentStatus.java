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

    public static String getChineseStr(String value)
    {
        if ("Pending".equals(value)) {
            return "待付款";
        } else if ("Paid".equals(value)) {
            return "已付款";
        }

        throw new IllegalArgumentException("Invalid value string: " + value);
    }

    public static int getInt(String value) {
        if ("Pending".equals(value)) {
            return 1;
        } else if ("Paid".equals(value)) {
            return 2;
        }

        throw new IllegalArgumentException("Invalid value string: " + value);
    }

}
