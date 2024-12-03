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

    public static int getIndexByValue(String value)
    {
        // Java 后端示例
        if ("Active".equals(value)) {
            return 1;
        } else if ("Paused".equals(value)) {
            return 2;
        } else if ("Blacklisted".equals(value)) {
            return 3;
        }
        throw new IllegalArgumentException("Invalid value string: " + value);
    }
}
