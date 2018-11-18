package com.ruhuna.lagom.order.impl.events;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

/**
 * Created by Kavinda on 11/18/2018.
 */
public class OrderEventTag {
    public static final AggregateEventTag<OrderEvent> INSTANCE = AggregateEventTag.of(OrderEvent.class);
}
