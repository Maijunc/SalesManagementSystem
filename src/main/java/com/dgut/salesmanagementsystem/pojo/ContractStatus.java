package com.dgut.salesmanagementsystem.pojo;

public enum ContractStatus {
    NOT_STARTED("not_started"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed");

    private String value;

    ContractStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getChineseStr(String value)
    {
        if ("not_started".equals(value)) {
            return "未开始";
        } else if ("in_progress".equals(value)) {
           return "进行中";
        } else if ("completed".equals(value)) {
            return "已完成";
        }

        throw new IllegalArgumentException("Invalid value string: " + value);
    }

    // 根据数字值获取枚举
    public static ContractStatus fromInt(int status) {
        switch (status) {
            case 1:
                return NOT_STARTED;
            case 2:
                return IN_PROGRESS;
            case 3:
                return COMPLETED;
            default:
                throw new IllegalArgumentException("Invalid status code: " + status);
        }
    }

    // 根据字符串值获取枚举
    public static ContractStatus fromString(String status) {
        for (ContractStatus cs : ContractStatus.values()) {
            if (cs.getValue().equalsIgnoreCase(status)) {
                return cs;
            }
        }
        throw new IllegalArgumentException("Invalid status string: " + status);
    }

    public static int getInt(String value) {
        if ("not_started".equals(value)) {
            return 1;
        } else if ("in_progress".equals(value)) {
            return 2;
        } else if ("completed".equals(value)) {
            return 3;
        }

        throw new IllegalArgumentException("Invalid value string: " + value);
    }
}
