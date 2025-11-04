package com.simple.blog.backend.core.gateway.service;

public interface IEventPublisher {

    void publish(Object event);

}
