package com.ruhuna.lagom.user.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;

import com.lightbend.lagom.javadsl.persistence.ReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import com.ruhuna.lagom.user.api.User;
import com.ruhuna.lagom.user.api.UserResponse;
import com.ruhuna.lagom.user.api.UserService;
import com.ruhuna.lagom.user.impl.command.UserCommand;
import com.ruhuna.lagom.user.impl.events.UserEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * Implementation of the UserService.
 */
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final PersistentEntityRegistry persistentEntityRegistry;
    private final CassandraSession session;

    @Inject
    public UserServiceImpl(PersistentEntityRegistry persistentEntityRegistry, ReadSide readSide, CassandraSession session) {
        this.persistentEntityRegistry = persistentEntityRegistry;
        this.session = session;
        persistentEntityRegistry.register(UserEntity.class);
        readSide.register(UserEventProcessor.class);
    }


    @Override
    public ServiceCall<User, Done> createUser() {
        LOGGER.info(" inside the CreateUser Method");
        return user -> {
            PersistentEntityRef<UserCommand> ref = userEntityRef(user);
            return ref.ask(UserCommand.CreateUser.builder().user(user).build());
        };
    }

    @Override
    public ServiceCall<NotUsed, Optional<UserResponse>> user(String id) {
        LOGGER.info(" inside the User Method");
        return request -> {
            CompletionStage<Optional<UserResponse>> movieFuture =
                    session.selectAll("SELECT * FROM Users WHERE id = ?", id)
                            .thenApply(rows -> {
                                Optional<User> user = rows.stream()
                                        .map(row -> User.builder()
                                                .id(row.getString("id"))
                                                .name(row.getString("name"))
                                                .email(row.getString("email"))
                                                .userName(row.getString("userName"))
                                                .build()
                                        ).findFirst();
                                return Optional.of(Mapper.userToUserResponse(user));
                            });
            return movieFuture;
        };
    }

    private PersistentEntityRef<UserCommand> userEntityRef(User user) {
        return persistentEntityRegistry.refFor(UserEntity.class, user.getId());
    }
}
