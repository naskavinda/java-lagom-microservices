package com.ruhuna.lagom.order.impl.command;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ruhuna.lagom.order.impl.OrderModel;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

/**
 * Created by Kavinda on 11/18/2018.
 */
public interface OrderCommand extends Jsonable {
    @Value
    @Builder
    @AllArgsConstructor
    @JsonDeserialize
    final class CreateOrder implements OrderCommand, PersistentEntity.ReplyType<Done>{
        OrderModel order;
    }

    @JsonDeserialize
    final class CurrentUserStatus implements OrderCommand, PersistentEntity.ReplyType<Optional<String>>{}
}
