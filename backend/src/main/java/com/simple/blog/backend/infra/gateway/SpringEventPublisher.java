package com.simple.blog.backend.infra.gateway;

import com.simple.blog.backend.core.gateway.IEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

public class SpringEventPublisher implements IEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(Object event) {
        publisher.publishEvent(event);
    }
}
