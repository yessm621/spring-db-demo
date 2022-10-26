package com.study.demo.repository;

import com.study.demo.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class PostRepositoryV0Test {

    PostRepositoryV0 repository = new PostRepositoryV0();

    @Test
    void crud() throws SQLException {
        Post post = new Post(3L, "내용입니다.", 0);
        repository.save(post);

        Post findPost = repository.findById(post.getId());
        log.info("findPost={}", findPost);
        log.info("post={}", post);
        log.info("post == findPost {}", post == findPost);
        log.info("post equals findPost {}", post.equals(findPost));
        assertThat(findPost).isEqualTo(post);

        repository.update(post.getId(), 1);
        Post updatePost = repository.findById(post.getId());
        assertThat(updatePost.getViewCnt()).isEqualTo(1);

        repository.delete(post.getId());
        assertThatThrownBy(() -> repository.findById(post.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}