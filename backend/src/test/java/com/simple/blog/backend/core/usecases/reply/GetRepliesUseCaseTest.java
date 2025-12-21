package com.simple.blog.backend.core.usecases.reply;

import com.simple.blog.backend.core.domain.DomainPage;
import com.simple.blog.backend.core.domain.Post;
import com.simple.blog.backend.core.domain.Reply;
import com.simple.blog.backend.core.domain.User;
import com.simple.blog.backend.core.gateway.IPostRepository;
import com.simple.blog.backend.core.gateway.IReplyRepository;
import com.simple.blog.backend.core.gateway.ILoggerService;
import com.simple.blog.backend.core.usecases.reply.get.GetRepliesUseCase;
import com.simple.blog.backend.core.usecases.reply.get.ReplySummaryOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetRepliesUseCase Test")
class GetRepliesUseCaseTest {

    @Mock
    private IReplyRepository replyRepository;

    @Mock
    private IPostRepository postRepository;

    @Mock
    private ILoggerService loggerService;

    @InjectMocks
    private GetRepliesUseCase useCase;

    private Post post;
    private User author;
    private Reply reply1;
    private Reply reply2;


    @BeforeEach
    void setup() {
        author = new User();
        author.setId(1L);
        author.setUsername("testUser");

        post = Post.create("Test Post", "Post content", null, author, null);
        post.setId(10L);

        reply1 = Reply.create("Reply 1", post, author);
        reply1.setId(100L);
        reply1.setCreatedAt(Instant.now());

        reply2 = Reply.create("Reply 2", post, author);
        reply2.setId(101L);
        reply2.setCreatedAt(Instant.now());
    }

    @Test
    @DisplayName("Should return paged list of replies for a post")
    void shouldReturnRepliesForPost() {
        int page = 0;
        int size = 10;

        DomainPage<Reply> replyPage = new DomainPage<>(
                page,
                size,
                2L,
                List.of(reply1, reply2)
        );

        when(postRepository.findById(post.getId())).thenReturn(post);
        when(replyRepository.findByPostId(post.getId(), page, size)).thenReturn(replyPage);

        DomainPage<ReplySummaryOutput> result = useCase.execute(post.getId(), page, size);

        assertEquals(2, result.items().size());
        assertEquals(reply1.getId(), result.items().get(0).id());
        assertEquals(reply1.getAuthor().getUsername(), result.items().get(0).replyAuthor());
        assertEquals(reply2.getId(), result.items().get(1).id());
    }

    @Test
    @DisplayName("Should return empty list when no replies exist")
    void shouldReturnEmptyListIfNoReplies() {
        int page = 0;
        int size = 10;

        DomainPage<Reply> replyPage = new DomainPage<>(
                page,
                size,
                0L,
                List.of()
        );

        when(postRepository.findById(post.getId())).thenReturn(post);
        when(replyRepository.findByPostId(post.getId(), page, size)).thenReturn(replyPage);

        DomainPage<ReplySummaryOutput> result = useCase.execute(post.getId(), page, size);

        assertEquals(0, result.items().size());
    }

}