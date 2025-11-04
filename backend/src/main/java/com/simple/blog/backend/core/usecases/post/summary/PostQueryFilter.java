package com.simple.blog.backend.core.usecases.post.summary;

import java.util.Set;

public record PostQueryFilter(
        String titleContains,
        Set<String> tags,
        SortOrder sortOrder,
        Long authorId
) {

    public enum SortOrder{
        NEWEST, OLDEST
    }

}
