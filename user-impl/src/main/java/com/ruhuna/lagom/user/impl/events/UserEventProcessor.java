package com.ruhuna.lagom.user.impl.events;

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
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by Kavinda on 11/17/2018.
 */
public class UserEventProcessor extends ReadSideProcessor<UserEvent>{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventProcessor.class);

    private final CassandraSession session;
    private final CassandraReadSide readSide;



    private PreparedStatement writeUsers;
    private PreparedStatement deleteUseres;

    @Inject
    public UserEventProcessor(CassandraSession session, CassandraReadSide readSide) {
        this.session = session;
        this.readSide = readSide;
    }


    @Override
    public ReadSideHandler<UserEvent> buildHandler() {
        LOGGER.info(" buildHandler method ... ");
        return readSide.<UserEvent>builder("Users_offset")
                .setGlobalPrepare(this::createTable)
                .setPrepare(evtTag -> prepareWriteUser()
                        .thenCombine(prepareDeleteUser(), (d1, d2) -> Done.getInstance())
                )
                .setEventHandler(UserEvent.UserCreated.class, this::processPostAdded)
                .build();
    }

    private CompletionStage<Done> prepareDeleteUser() {
        return session.prepare(
                "DELETE FROM Users WHERE id=?"
        ).thenApply(ps -> {
            setDeleteUsers(ps);
            return Done.getInstance();
        });
    }

    private CompletionStage<Done> prepareWriteUser() {
        return session.prepare(
                "INSERT INTO Users (id, name, email, userName, password) VALUES (?, ?, ?, ?, ?)"
        ).thenApply(ps -> {
            setWriteUsers(ps);
            return Done.getInstance();
        });
    }

    private CompletionStage<Done> createTable() {
        LOGGER.info("Create Users Database Table");
        return session.executeCreateTable(
                "CREATE TABLE IF NOT EXISTS Users ( " +
                        "id TEXT, name TEXT, email TEXT, userName TEXT, password TEXT, PRIMARY KEY(id))"
        );
    }

    @Override
    public PSequence<AggregateEventTag<UserEvent>> aggregateTags() {
        LOGGER.info(" aggregateTags method ... ");
        return TreePVector.singleton(UserEventTag.INSTANCE);
    }


    public void setWriteUsers(PreparedStatement statement) {
        this.writeUsers = statement;
    }

    public void setDeleteUsers(PreparedStatement statement) {
        this.deleteUseres = statement;
    }

    private CompletionStage<List<BoundStatement>> processPostAdded(UserEvent.UserCreated event) {
        BoundStatement bindWriteMovie = writeUsers.bind();
        bindWriteMovie.setString("id", event.getUser().getId());
        bindWriteMovie.setString("name", event.getUser().getName());
        bindWriteMovie.setString("email", event.getUser().getEmail());
        bindWriteMovie.setString("userName", event.getUser().getUserName());
        bindWriteMovie.setString("password", hashPassword(event.getUser().getPassword()));
        return CassandraReadSide.completedStatements(Arrays.asList(bindWriteMovie));
    }

    public static String hashPassword(String passwordPlaintext) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(passwordPlaintext, salt);
    }
}
