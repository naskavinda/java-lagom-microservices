package com.ruhuna.lagom.user.impl.states;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ruhuna.lagom.user.api.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

/**
 * Created by Kavinda on 11/17/2018.
 */
@Value
@Builder
@JsonDeserialize
@AllArgsConstructor
public class UserStates {
    Optional<User> user;
    String timestamp;
}
