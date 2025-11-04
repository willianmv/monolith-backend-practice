package com.simple.blog.backend.infra.persistence.specs;

import org.springframework.data.jpa.domain.Specification;

public class SpecHelper {

    public static <T> Specification<T> andSpec(Specification<T> base, Specification<T> addition){
        return (base == null) ? addition : base.and(addition);
    }

}
