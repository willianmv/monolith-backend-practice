package com.simple.blog.backend.infra.gateway.repository;

import com.simple.blog.backend.core.domain.ValidationCode;
import com.simple.blog.backend.core.gateway.repository.IValidationCodeRepository;
import com.simple.blog.backend.infra.persistence.mapper.ValidationCodeMapper;
import com.simple.blog.backend.infra.persistence.entity.ValidationCodeEntity;
import com.simple.blog.backend.infra.persistence.repository.ValidationCodeEntityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ValidationCodeRepository implements IValidationCodeRepository {

    private final ValidationCodeEntityRepository validationCodeEntityRepository;

    public ValidationCodeRepository(ValidationCodeEntityRepository validationCodeEntityRepository) {
        this.validationCodeEntityRepository = validationCodeEntityRepository;
    }

    @Override
    public void save(ValidationCode code) {
        ValidationCodeEntity validationCodeEntity = ValidationCodeMapper.toJpaEntity(code);
        this.validationCodeEntityRepository.save(validationCodeEntity);
    }

    @Override
    public ValidationCode findByCode(String code) {
        return this.validationCodeEntityRepository.findByCode(code)
                .map(vc -> ValidationCodeMapper.toCore(vc, true))
                .orElseThrow(() -> new EntityNotFoundException("Code not found"));
    }

    @Override
    public boolean existsValidByUserId(long userId) {
        return this.validationCodeEntityRepository.existsValidByUserId(userId, Instant.now());
    }


    @Override
    public ValidationCode findLatestByUserId(long userId) {
        return this.validationCodeEntityRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .map(vc -> ValidationCodeMapper.toCore(vc, true))
                .orElseThrow(() -> new EntityNotFoundException("Code not found"));
    }
}
