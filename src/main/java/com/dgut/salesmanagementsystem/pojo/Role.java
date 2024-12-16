package com.dgut.salesmanagementsystem.pojo;
public enum Role {
    SALES_MANAGER("SalesManager"),
    WAREHOUSE_MANAGER("WarehouseManager"),
    SALES_MAN("SalesMan");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public static int getInt(String value) {
        if ("SalesManager".equals(value)) {
            return 1;
        } else if ("WarehouseManager".equals(value)) {
            return 2;
        } else if ("SalesMan".equals(value)) {
            return 3;
        }

        throw new IllegalArgumentException("Invalid value string: " + value);
    }

    public String getRole() {
        return role;
    }

    // 根据字符串值获取对应的枚举实例
    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.getRole().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Unexpected role: " + role);
    }
}

