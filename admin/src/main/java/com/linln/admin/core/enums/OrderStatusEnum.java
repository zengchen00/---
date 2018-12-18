package com.linln.admin.core.enums;

import lombok.Getter;

/**
 * 订单状态枚举
 * @author zengchen
 * @date 2018/12/18
 */
@Getter
public enum OrderStatusEnum {

    // 下单
    PLANCE("A", "下单"),
    HANLDED("B", "已发货"),
    FINISH("C", "已完成"),
    CANCEL("D", "已作废");


    private String code;

    private String message;

    OrderStatusEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

