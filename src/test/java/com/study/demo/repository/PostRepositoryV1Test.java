package com.study.demo.repository;

import com.study.demo.entity.Post;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.study.demo.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class PostRepositoryV1Test {

    PostRepositoryV1 repository;

    @BeforeEach
    void beforeEach() {
        // DataSource 인터페이스를 사용
        // DriverManagerDataSource -> HikariDataSource로 변경해도 PostRepositoryV1은 코드 수정이 필요 없다.
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        repository = new PostRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {
        log.info("start");
        Post post = new Post(4L, "내용입니다.", 0);
        repository.save(post);

        Post findPost = repository.findById(post.getId());
        assertThat(findPost).isEqualTo(post);

        repository.update(post.getId(), 1);
        Post updatePost = repository.findById(post.getId());
        assertThat(updatePost.getViewCnt()).isEqualTo(1);

        repository.delete(post.getId());
        assertThatThrownBy(() -> repository.findById(post.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

}