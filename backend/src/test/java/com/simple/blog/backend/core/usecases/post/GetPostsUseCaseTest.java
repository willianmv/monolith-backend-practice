package com.simple.blog.backend.core.usecases.post;

import com.simple.blog.backend.core.domain.DomainPage;
import com.simple.blog.backend.core.gateway.repository.IPostRepository;
import com.simple.blog.backend.core.gateway.service.ILoggerService;
import com.simple.blog.backend.core.usecases.post.summary.GetPostsUseCase;
import com.simple.blog.backend.core.usecases.post.summary.PostQueryFilter;
import com.simple.blog.backend.core.usecases.post.summary.SummaryPostOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetPostsUseCase Test")
class GetPostsUseCaseTest {

    @Mock
    private IPostRepository postRepository;

    @Mock
    private ILoggerService loggerService;

    @InjectMocks
    private GetPostsUseCase useCase;

    private PostQueryFilter filter;
    private DomainPage<SummaryPostOutput> page;

    @BeforeEach
    void setup() {
        filter = new PostQueryFilter(null, Set.of(), PostQueryFilter.SortOrder.NEWEST, null);

        SummaryPostOutput post1 = new SummaryPostOutput(1L, "Title 1", "user1", "Content 1", 1, null, Instant.now());
        SummaryPostOutput post2 = new SummaryPostOutput(2L, "Title 2", "user2", "Content 2", 2, null, Instant.now());

        page = new DomainPage<>(0, 2, 2L, List.of(post1, post2));
    }

    @Test
    @DisplayName("Should return paged summary posts successfully")
    void shouldReturnPagedSummaryPosts() {
        when(postRepository.findSummaryPosts(filter, 0, 2)).thenReturn(page);

        DomainPage<SummaryPostOutput> result = useCase.execute(filter, 0, 2);

        assertNotNull(result);
        assertEquals(0, result.pageNumber());
        assertEquals(2, result.pageSize());
        assertEquals(2L, result.totalItems());
        assertEquals(2, result.items().size());

        verify(postRepository).findSummaryPosts(filter, 0, 2);
    }

}