package com.ruhuna.lagom.user.impl.events;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;

/**
 * Created by Kavinda on 11/17/2018.
 */
public class UserEventTag {
    public static final AggregateEventTag<UserEvent> INSTANCE = AggregateEventTag.of(UserEvent.class);
}
