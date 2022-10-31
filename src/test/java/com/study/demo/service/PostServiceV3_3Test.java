package com.study.demo.service;

import com.study.demo.entity.Post;
import com.study.demo.repository.PostRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.study.demo.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class PostServiceV3_3Test {

    public static final Long POST_A = 1L;
    public static final Long POST_B = 2L;
    public static final Long POST_EX = 3L;

    @Autowired
    PostRepositoryV3 postRepository;
    @Autowired
    PostServiceV3_3 postService;

    @AfterEach
    void after() throws SQLException {
        postRepository.delete(POST_A);
        postRepository.delete(POST_B);
        postRepository.delete(POST_EX);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        DataSource dataSource() {
            return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        }

        @Bean
        PlatformTransactionManager transactionManager() {
            return new DataSourceTransactionManager(dataSource());
        }

        @Bean
        PostRepositoryV3 postRepository() {
            return new PostRepositoryV3(dataSource());
        }

        @Bean
        PostServiceV3_3 postService() {
            return new PostServiceV3_3(postRepository());
        }
    }

    @Test
    void aopCheck() {
        log.info("postService class={}", postService.getClass());
        log.info("postRepository class={}", postRepository.getClass());
        assertThat(AopUtils.isAopProxy(postService)).isTrue();
        assertThat(AopUtils.isAopProxy(postRepository)).isFalse();
    }

    @Test
    @DisplayName("정상이체")
    void accountTransfer() throws SQLException {
        Post postA = new Post(1L, "postA", 10);
        Post postB = new Post(2L, "postB", 20);
        postRepository.save(postA);
        postRepository.save(postB);

        log.info("START TX");
        postService.accountTransfer(postA.getId(), postB.getId(), 5);
        log.info("END TX");

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

        // 트랜잭션 적용함.
        // A는 성공하고 Ex는 실패했다. 트랜잭션이 적용되었으므로 모두 원래 데이터값으로 변경됨.
        Post findPostA = postRepository.findById(postA.getId());
        Post findPostB = postRepository.findById(postEx.getId());
        log.info("findPostA={}", findPostA);
        log.info("findPostB={}", findPostB);
        assertThat(findPostA.getViewCnt()).isEqualTo(10);
        assertThat(findPostB.getViewCnt()).isEqualTo(30);
    }
}