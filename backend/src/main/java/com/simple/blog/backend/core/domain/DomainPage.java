package com.simple.blog.backend.core.domain;

import java.util.List;

public record DomainPage<T>(int pageNumber, int pageSize, long totalItems, List<T> items) {

}
