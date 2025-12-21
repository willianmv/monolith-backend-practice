package com.simple.blog.backend.core.gateway;

public interface IEventPublisher {

    void publish(Object event);

}
