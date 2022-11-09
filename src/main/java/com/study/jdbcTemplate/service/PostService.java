package com.study.jdbcTemplate.service;

import com.study.jdbcTemplate.entity.Post;
import com.study.jdbcTemplate.entity.PostDto;
import com.study.jdbcTemplate.repository.PostSearchCond;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post save(Post Post);

    void update(Long PostId, PostDto postDto);

    Optional<Post> findById(Long id);

    List<Post> findPosts(PostSearchCond PostSearch);

}
