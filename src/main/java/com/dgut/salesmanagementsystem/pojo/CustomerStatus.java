package com.dgut.salesmanagementsystem.pojo;

public enum CustomerStatus {
    ACTIVE("Active"),
    PAUSED("Paused"),
    BLACKLISTED("Blacklisted");

    private String value;
    // value 存的是上面括号内的值，比如“Active"
    CustomerStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // 根据数字值获取枚举
    public static CustomerStatus fromInt(int status) {
        switch (status) {
            case 1:
                return ACTIVE;
            case 2:
                return PAUSED;
            case 3:
                return BLACKLISTED;
            default:
                throw new IllegalArgumentException("Invalid status code: " + status);
        }
    }
}
