package com.ruhuna.lagom.order.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.util.Optional;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.transport.Method.GET;
import static com.lightbend.lagom.javadsl.api.transport.Method.POST;

/**
 * Created by Kavinda on 11/17/2018.
 */
public interface OrderService extends Service {

    ServiceCall<Order, Done> placeOrder();

    ServiceCall<NotUsed, Optional<String>> getOrderStatus(String id);

    @Override
    default Descriptor descriptor(){
        return named("order").withCalls(
                restCall(POST, "/api/order", this::placeOrder),
                restCall(GET, "/api/order/:id", this::getOrderStatus)
        ).withAutoAcl(true);
    }
}
