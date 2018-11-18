package com.ruhuna.lagom.order.impl.events;

import akka.Done;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by Kavinda on 11/18/2018.
 */
public class OrderEventProcessor extends ReadSideProcessor<OrderEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventProcessor.class);

    private final CassandraSession session;
    private final CassandraReadSide readSide;



    private PreparedStatement writeOrders;
    private PreparedStatement deleteOrders;

    @Inject
    public OrderEventProcessor(CassandraSession session, CassandraReadSide readSide) {
        this.session = session;
        this.readSide = readSide;
    }

    @Override
    public ReadSideHandler<OrderEvent> buildHandler() {
        LOGGER.info(" buildHandler method ... ");
        return readSide.<OrderEvent>builder("Orders_offset")
                .setGlobalPrepare(this::createTable)
                .setPrepare(evtTag -> prepareWriteOrder()
                        .thenCombine(prepareDeleteOrder(), (d1, d2) -> Done.getInstance())
                )
                .setEventHandler(OrderEvent.OrderCreated.class, this::processPostAdded)
                .build();
    }

    private CompletionStage<Done> createTable() {
        LOGGER.info("Create Orders Database Table");
        return session.executeCreateTable(
                "CREATE TABLE IF NOT EXISTS Orders ( " +
                        "id TEXT, userId TEXT, productIds list<TEXT>, status TEXT, PRIMARY KEY(id))"
        );
    }

    private CompletionStage<Done> prepareWriteOrder() {
        return session.prepare(
                "INSERT INTO Orders (id, userId, productIds, status) VALUES (?, ?, ?, ?)"
        ).thenApply(ps -> {
            setWriteOrders(ps);
            return Done.getInstance();
        });
    }

    public void setWriteOrders(PreparedStatement statement) {
        this.writeOrders = statement;
    }

    private CompletionStage<Done> prepareDeleteOrder() {
        return session.prepare(
                "DELETE FROM Orders WHERE id=?"
        ).thenApply(ps -> {
            setDeleteOrder(ps);
            return Done.getInstance();
        });
    }

    public void setDeleteOrder(PreparedStatement statement) {
        this.deleteOrders = statement;
    }

    private CompletionStage<List<BoundStatement>> processPostAdded(OrderEvent.OrderCreated event) {
        BoundStatement bindWriteMovie = writeOrders.bind();
        bindWriteMovie.setString("id", event.getOrder().getId());
        bindWriteMovie.setString("userId", event.getOrder().getUserId());
        bindWriteMovie.setList("productIds", event.getOrder().getProductIds());
        bindWriteMovie.setString("status", event.getOrder().getStatus());
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteMovie));
    }

    @Override
    public PSequence<AggregateEventTag<OrderEvent>> aggregateTags() {
        LOGGER.info(" aggregateTags method ... ");
        return TreePVector.singleton(OrderEventTag.INSTANCE);
    }
}
