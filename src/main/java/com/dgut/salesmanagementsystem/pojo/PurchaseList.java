package com.dgut.salesmanagementsystem.pojo;

import java.math.BigDecimal;
import java.util.List;

// 采购清单
public class PurchaseList {
    // 客户付款采购清单，一个清单应该包含多个product
    private Integer purchaseListID;
    private Integer customerID;
    private List<PurchaseListItem> productID;
    private BigDecimal totalPrice; //总价
    private String paymentStatus; //客户支付状态
}
