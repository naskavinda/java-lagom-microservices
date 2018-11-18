package com.ruhuna.lagom.user.api;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.api.ServiceInfo;
import com.lightbend.lagom.javadsl.client.ServiceClientGuiceSupport;

/**
 * Created by Kavinda on 11/18/2018.
 */
public class Module extends AbstractModule implements ServiceClientGuiceSupport {
    @Override
    protected void configure() {
        bindServiceInfo(ServiceInfo.of("user-service"));
        bindClient(UserService.class);
    }
}
