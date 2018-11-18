package com.ruhuna.lagom.user.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.*;
import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.transport.Method.GET;
import static com.lightbend.lagom.javadsl.api.transport.Method.POST;

/**
 * The Hello service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the User.
 */
public interface UserService extends Service {


    ServiceCall<User, Done> createUser();

    ServiceCall<NotUsed, Optional<UserResponse>> user(String id);

    @Override
    default Descriptor descriptor() {
        return named("user").withCalls(
                restCall(POST, "/api/user", this::createUser),
                restCall(GET, "/api/user/:id", this::user)
        ).withAutoAcl(true);
    }
}
