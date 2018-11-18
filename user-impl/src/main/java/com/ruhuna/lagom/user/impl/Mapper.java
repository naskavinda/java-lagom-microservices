package com.ruhuna.lagom.user.impl;

import com.ruhuna.lagom.user.api.User;
import com.ruhuna.lagom.user.api.UserResponse;

import java.util.Optional;

/**
 * Created by Kavinda on 11/17/2018.
 */
public class Mapper {
    public static UserResponse userToUserResponse(Optional<User> user) {
        return user.isPresent() ?
                UserResponse.builder().id(user.get().getId())
                        .name(user.get().getName())
                        .email(user.get().getEmail())
                        .userName(user.get().getUserName()).build() : UserResponse.builder().build();
    }
}
