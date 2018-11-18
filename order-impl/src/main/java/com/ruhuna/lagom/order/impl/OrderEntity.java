package com.ruhuna.lagom.order.impl;

import akka.Done;
import com.ruhuna.lagom.order.impl.states.OrderStates;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.ruhuna.lagom.order.impl.command.OrderCommand;
import com.ruhuna.lagom.order.impl.events.OrderEvent;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by Kavinda on 11/18/2018.
 */
public class OrderEntity extends PersistentEntity<OrderCommand, OrderEvent, OrderStates> {
    @Override
    public Behavior initialBehavior(Optional<OrderStates> snapshotState) {
        BehaviorBuilder behaviorBuilder = newBehaviorBuilder(
                OrderStates.builder().order(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );


        behaviorBuilder.setCommandHandler(OrderCommand.CreateOrder.class, (cmd, ctx) ->
                ctx.thenPersist(OrderEvent.OrderCreated.builder().order(cmd.getOrder())
                        .entityId(entityId()).build(), evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(OrderEvent.OrderCreated.class, evt ->
                OrderStates.builder().order(Optional.of(evt.getOrder()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setReadOnlyCommandHandler(OrderCommand.CurrentUserStatus.class, (cmd, ctx) ->
                ctx.reply(Optional.of(state().getOrder().get().getId()))
        );

        return behaviorBuilder.build();
    }
}
