package com.ruhuna.lagom.order.impl.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import com.ruhuna.lagom.order.impl.OrderModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Created by Kavinda on 11/18/2018.
 */
public interface OrderEvent  extends Jsonable, AggregateEvent<OrderEvent> {
    @Override
    default AggregateEventTagger<OrderEvent> aggregateTag(){
        return OrderEventTag.INSTANCE;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class OrderCreated implements OrderEvent, CompressedJsonable {
        OrderModel order;
        String entityId;
    }
}
