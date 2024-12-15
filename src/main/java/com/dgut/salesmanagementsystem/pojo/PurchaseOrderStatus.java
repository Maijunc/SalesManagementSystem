package com.dgut.salesmanagementsystem.pojo;

public enum PurchaseOrderStatus {
    PENDING("pending"),  // 待处理
    COMPLETED("completed");  // 已完成

    private String value;

    PurchaseOrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PurchaseOrderStatus fromString(String value) {
        for (PurchaseOrderStatus status : PurchaseOrderStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + value);
    }

    public static String getChineseStr(String value) {
        if ("pending".equalsIgnoreCase(value)) {
            return "待处理";
        } else if ("completed".equalsIgnoreCase(value)) {
            return "已完成";
        }
        throw new IllegalArgumentException("Invalid value string: " + value);
    }

    public static int getInt(String value) {
        if ("pending".equals(value)) {
            return 1;
        } else if ("completed".equals(value)) {
            return 2;
        }

        throw new IllegalArgumentException("Invalid value string: " + value);
    }
}