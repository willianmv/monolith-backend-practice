package com.simple.blog.backend.infra.persistence.repository;

import com.simple.blog.backend.infra.persistence.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostEntityRepository extends JpaRepository <PostEntity, Long>, JpaSpecificationExecutor<PostEntity>{}
