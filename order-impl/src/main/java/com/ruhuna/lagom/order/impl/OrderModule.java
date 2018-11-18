package com.ruhuna.lagom.order.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.ruhuna.lagom.user.api.UserService;
import com.ruhuna.lagom.order.api.OrderService;

/**
 * The module that binds the UserService so that it can be served.
 */
public class OrderModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(OrderService.class, OrderServiceImpl.class);// bind the order-api and order-impl
        bindClient(UserService.class);// bind the user-api client
    }
}
