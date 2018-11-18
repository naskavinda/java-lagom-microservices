package com.ruhuna.lagom.user.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.annotation.concurrent.Immutable;

/**
 * Created by Kavinda on 11/17/2018.
 */
@Value
@Builder
@Immutable
@JsonDeserialize
@AllArgsConstructor
public class UserResponse {
    String id;
    String name;
    String email;
    String userName;
}
