package com.ruhuna.lagom.user.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.ruhuna.lagom.user.api.UserService;

/**
 * The module that binds the UserService so that it can be served.
 */
public class UserModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(UserService.class, UserServiceImpl.class);
    }
}
