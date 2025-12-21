package com.simple.blog.backend.core.gateway;

public interface ILoggerService {
    void header(String message);
    void footer(String message);
    void info(String message);
    void info(String message, Throwable throwable);
    void warn(String message);
    void warn(String message, Throwable throwable);
    void error(String message);
    void error(String message, Throwable throwable);
}
