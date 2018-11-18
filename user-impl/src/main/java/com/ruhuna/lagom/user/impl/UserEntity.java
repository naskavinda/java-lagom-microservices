package com.ruhuna.lagom.user.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.ruhuna.lagom.user.impl.command.UserCommand;
import com.ruhuna.lagom.user.impl.events.UserEvent;
import com.ruhuna.lagom.user.impl.states.UserStates;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by Kavinda on 11/17/2018.
 */
public class UserEntity  extends PersistentEntity<UserCommand, UserEvent, UserStates> {
    @Override
    public Behavior initialBehavior(Optional<UserStates> snapshotState) {


        BehaviorBuilder behaviorBuilder = newBehaviorBuilder(
                UserStates.builder().user(Optional.empty())
                        .timestamp(LocalDateTime.now().toString()).build()
        );


        behaviorBuilder.setCommandHandler(UserCommand.CreateUser.class, (cmd, ctx) ->
                ctx.thenPersist(UserEvent.UserCreated.builder().user(cmd.getUser())
                        .entityId(entityId()).build(), evt -> ctx.reply(Done.getInstance()))
        );

        behaviorBuilder.setEventHandler(UserEvent.UserCreated.class, evt ->
                UserStates.builder().user(Optional.of(evt.getUser()))
                        .timestamp(LocalDateTime.now().toString()).build()
        );

        behaviorBuilder.setReadOnlyCommandHandler(UserCommand.CurrentUserStatus.class, (cmd, ctx) ->
                ctx.reply(Optional.of(Mapper.userToUserResponse(state().getUser())))
        );

        return behaviorBuilder.build();
    }
}
