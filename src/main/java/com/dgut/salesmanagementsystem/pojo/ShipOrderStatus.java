package com.dgut.salesmanagementsystem.pojo;

public enum ShipOrderStatus {
    // 待发货
    PENDING("pending"),
    // 已发货
    SHIPPED("shipped"),
    // 已签收
    SIGNED("signed"),
    // 异常
    EXCEPTION("exception");

    private String value;
    // value 存的是上面括号内的值，比如“Active"
    ShipOrderStatus(String value) {
        this.value = value;
    }

    public static String getChineseStr(String value)
    {
        if ("pending".equals(value)) {
            return "待发货";
        } else if ("shipped".equals(value)) {
            return "已发货";
        } else if ("signed".equals(value)) {
            return "已签收";
        } else if ("exception".equals(value)) {
            return "异常";
        }

        throw new IllegalArgumentException("Invalid value string: " + value);
    }

    public String getValue() {
        return value;
    }

    public static ShipOrderStatus fromString(String status) {
        for (ShipOrderStatus sos : ShipOrderStatus.values()) {
            if (sos.getValue().equalsIgnoreCase(status)) {
                return sos;
            }
        }
        throw new IllegalArgumentException("Invalid status string: " + status);
    }

    public static int getInt(String value) {
        if ("pending".equals(value)) {
            return 1;
        } else if ("shipped".equals(value)) {
            return 2;
        } else if ("signed".equals(value)) {
            return 3;
        } else if ("exception".equals(value)){
            return 4;
        }

        throw new IllegalArgumentException("Invalid value string: " + value);
    }
}
//    // 运输中
//    IN_TRANSIT("in_transit"),
//    // 配送失败
//    DELIVER_FAILED("delivery_failed"),
//    // 已取消
//    CANCELLED("cancelled"),
//    // 退货中
//    RETURN_IN_PROGRESS("return_in_progress"),
//    // 退货完成
//    RETURNED("returned"),
