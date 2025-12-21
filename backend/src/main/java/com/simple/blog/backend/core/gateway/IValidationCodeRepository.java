package com.simple.blog.backend.core.gateway;

import com.simple.blog.backend.core.domain.ValidationCode;

public interface IValidationCodeRepository {

    void save(ValidationCode code);

    ValidationCode findByCode(String code);

    boolean existsValidByUserId(long userId);

    ValidationCode findLatestByUserId(long userId);

}
