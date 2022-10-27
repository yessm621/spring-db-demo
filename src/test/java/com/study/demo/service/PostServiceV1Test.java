package com.study.demo.service;

import com.study.demo.entity.Post;
import com.study.demo.repository.PostRepositoryV1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static com.study.demo.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class PostServiceV1Test {

    public static final Long POST_A = 1L;
    public static final Long POST_B = 2L;
    public static final Long POST_EX = 3L;

    private PostRepositoryV1 postRepository;
    private PostServiceV1 postService;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        postRepository = new PostRepositoryV1(dataSource);
        postService = new PostServiceV1(postRepository);
    }

    @AfterEach
    void after() throws SQLException {
        postRepository.delete(POST_A);
        postRepository.delete(POST_B);
        postRepository.delete(POST_EX);
    }

    @Test
    @DisplayName("정상이체")
    void accountTransfer() throws SQLException {
        Post postA = new Post(1L, "postA", 10);
        Post postB = new Post(2L, "postB", 20);
        postRepository.save(postA);
        postRepository.save(postB);

        postService.accountTransfer(postA.getId(), postB.getId(), 5);

        Post findPostA = postRepository.findById(postA.getId());
        Post findPostB = postRepository.findById(postB.getId());
        log.info("findPostA={}", findPostA);
        log.info("findPostB={}", findPostB);
        assertThat(findPostA.getViewCnt()).isEqualTo(5);
        assertThat(findPostB.getViewCnt()).isEqualTo(25);
    }

    @Test
    @DisplayName("이체 중 예외 발생")
    void accountTransferEx() throws SQLException {
        Post postA = new Post(1L, "postA", 10);
        Post postEx = new Post(3L, "postEx", 30);
        postRepository.save(postA);
        postRepository.save(postEx);

        assertThatThrownBy(() -> postService.accountTransfer(postA.getId(), postEx.getId(), 5))
                .isInstanceOf(IllegalStateException.class);

        // A는 성공하고 Ex는 실패하여 변경은 A만 됨. 트랜잭션 적용 안됨.
        Post findPostA = postRepository.findById(postA.getId());
        Post findPostB = postRepository.findById(postEx.getId());
        log.info("findPostA={}", findPostA);
        log.info("findPostB={}", findPostB);
        assertThat(findPostA.getViewCnt()).isEqualTo(5);
        assertThat(findPostB.getViewCnt()).isEqualTo(30);
    }
}