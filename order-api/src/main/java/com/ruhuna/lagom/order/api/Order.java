package com.ruhuna.lagom.order.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.annotation.concurrent.Immutable;
import java.util.List;

/**
 * Created by Kavinda on 11/17/2018.
 */
@Value
@Builder
@Immutable
@JsonDeserialize
@AllArgsConstructor
public class Order {
    String id;
    String userId;
    List<String> productIds;
}
