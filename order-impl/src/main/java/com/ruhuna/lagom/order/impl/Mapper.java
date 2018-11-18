package com.ruhuna.lagom.order.impl;

import com.ruhuna.lagom.order.api.Order;

/**
 * Created by Kavinda on 11/18/2018.
 */
public class Mapper {
    public static OrderModel orderToOrderModel(Order order, String Status) {
        return OrderModel.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .productIds(order.getProductIds())
                .status(Status)
                .build();
    }
}
