package com.dgut.salesmanagementsystem.pojo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

// 发货单 一张发货单只对应一个商品
public class ShipOrder {
    private int shipOrderID; //发货单ID
    private int productID; //商品ID
    private int purchaseListID; //采购清单ID 这张发货单对应哪个采购清单
    private ShipOrderStatus shipOrderStatus; //发货单状态
    private Timestamp shipDate; //发货时间
    private int customerID; //客户编号
    private String customerName; //客户名称
    private String recipientName; //收货人姓名
    private String shippingAddress; //收货地址
    private String recipientPhone; //收货人电话
    private String shippingCompany; //物流公司
    private String trackingNumber; //物流单号
    private BigDecimal totalAmount; // 总金额
    private String shippedBy; //发货人
    private String notes; //备注
    private Timestamp createdAt; //创建时间
    private Timestamp updatedAt; //更新时间

}
