package com.simple.blog.backend.core.gateway;

public interface IPasswordService {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}
