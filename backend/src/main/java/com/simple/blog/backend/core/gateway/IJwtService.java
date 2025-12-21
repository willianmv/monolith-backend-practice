package com.simple.blog.backend.core.gateway;

import java.util.Map;

public interface IJwtService {

    String generateToken(String email, Map<String, Object> extraClaims);

    String extractSubject(String token);

    boolean isTokenValid(String token, String subject);

}
