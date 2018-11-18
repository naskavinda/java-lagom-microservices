package com.ruhuna.lagom.user.impl.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import com.ruhuna.lagom.user.api.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Created by Kavinda on 11/17/2018.
 */
public interface UserEvent extends Jsonable, AggregateEvent<UserEvent> {
    @Override
    default AggregateEventTagger<UserEvent> aggregateTag(){
        return UserEventTag.INSTANCE;
    }

    @Value
    @Builder
    @JsonDeserialize
    @AllArgsConstructor
    final class UserCreated implements UserEvent, CompressedJsonable{
        User user;
        String entityId;
    }
}
