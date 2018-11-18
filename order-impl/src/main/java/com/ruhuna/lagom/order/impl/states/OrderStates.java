package com.ruhuna.lagom.order.impl.states;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ruhuna.lagom.order.impl.OrderModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

/**
 * Created by Kavinda on 11/18/2018.
 */
@Value
@Builder
@JsonDeserialize
@AllArgsConstructor
public class OrderStates {
    Optional<OrderModel> order;
    String timestamp;
}
