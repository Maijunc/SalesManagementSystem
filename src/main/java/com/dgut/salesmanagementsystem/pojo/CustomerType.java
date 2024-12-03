package com.dgut.salesmanagementsystem.pojo;

public enum CustomerType {
    INDIVIDUAL("Individual"),
    COMPANY("Company");

    private String value;
    // value 存的是上面括号内的值，比如“Active"
    CustomerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // 根据数字值获取枚举
    public static CustomerType fromInt(int type) {
        switch (type) {
            case 1:
                return INDIVIDUAL;
            case 2:
                return COMPANY;
            default:
                throw new IllegalArgumentException("Invalid type code: " + type);
        }
    }

    public static int getIndexByValue(String value)
    {
        // Java 后端示例
        if ("Individual".equals(value)) {
            return 1;
        } else if ("Company".equals(value)) {
            return 2;
        }
        throw new IllegalArgumentException("Invalid value string: " + value);
    }
}
