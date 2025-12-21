package com.simple.blog.backend.infra.gateway;

import com.simple.blog.backend.core.gateway.ICodeService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CodeService implements ICodeService {

    private static final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {
        int code = random.nextInt(900_000) + 100_000;
        return String.valueOf(code);
    }
}
