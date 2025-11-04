package com.simple.blog.backend.infra.persistence.repository;

import com.simple.blog.backend.infra.persistence.entity.ReplyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyEntityRepository extends JpaRepository <ReplyEntity, Long>{

    Page<ReplyEntity> findByPostId(Long postId, Pageable pageable);

}
