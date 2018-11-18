package com.ruhuna.lagom.user.impl.command;

import akka.Done;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import com.ruhuna.lagom.user.api.User;
import com.ruhuna.lagom.user.api.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

/**
 * Created by Kavinda on 11/17/2018.
 */
public interface UserCommand extends Jsonable{

    @Value
    @Builder
    @AllArgsConstructor
    @JsonDeserialize
    final class CreateUser implements UserCommand, PersistentEntity.ReplyType<Done>{
        User user;
    }

    @JsonDeserialize
    final class CurrentUserStatus implements UserCommand, PersistentEntity.ReplyType<Optional<UserResponse>>{}
}
