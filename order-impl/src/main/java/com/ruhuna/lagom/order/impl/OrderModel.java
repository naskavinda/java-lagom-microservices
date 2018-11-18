package com.ruhuna.lagom.order.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.annotation.concurrent.Immutable;
import java.util.List;

/**
 * Created by Kavinda on 11/18/2018.
 */
@Value
@Builder
@Immutable
@JsonDeserialize
@AllArgsConstructor
public class OrderModel {
    String id;
    String userId;
    List<String> productIds;
    String status;
}
