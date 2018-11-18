package com.ruhuna.lagom.order.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import com.ruhuna.lagom.user.api.UserResponse;
import com.ruhuna.lagom.user.api.UserService;
import com.ruhuna.lagom.order.api.Order;
import com.ruhuna.lagom.order.api.OrderService;
import com.ruhuna.lagom.order.impl.command.OrderCommand;
import com.ruhuna.lagom.order.impl.events.OrderEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by Kavinda on 11/18/2018.
 */
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final PersistentEntityRegistry persistentEntityRegistry;
    private final CassandraSession session;
    private final UserService userService;

    @Inject
    public OrderServiceImpl(PersistentEntityRegistry persistentEntityRegistry, ReadSide readSide, CassandraSession session, UserService userService) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.session = session;
        this.userService = userService;
        persistentEntityRegistry.register(OrderEntity.class);
        readSide.register(OrderEventProcessor.class);
    }

    @Override
    public ServiceCall<Order, Done> placeOrder() {
        LOGGER.info(" inside the placeOrder Method");
        return order -> {
            PersistentEntityRef<OrderCommand> ref = userEntityRef(order);
            try {
                Optional<UserResponse> userResponse = userService.user(order.getUserId()).invoke().toCompletableFuture().get();
                LOGGER.info("E-Mail :- " +userResponse.get().getEmail());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return ref.ask(OrderCommand.CreateOrder.builder().order(Mapper.orderToOrderModel(order,"PENDING")).build());
        };
    }

    @Override
    public ServiceCall<NotUsed, Optional<String>> getOrderStatus(String id) {
        LOGGER.info(" inside the getOrderStatus Method");
        return request ->
                session.selectAll("SELECT * FROM Orders WHERE id = ?", id)
                        .thenApply(rows ->
                                rows.stream()
                                        .map(row -> row.getString("status"))
                                        .findFirst()
                        );
    }

    private PersistentEntityRef<OrderCommand> userEntityRef(Order order) {
        return persistentEntityRegistry.refFor(OrderEntity.class, order.getId());
    }
}
