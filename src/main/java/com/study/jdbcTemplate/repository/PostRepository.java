package com.study.jdbcTemplate.repository;


import com.study.jdbcTemplate.entity.Post;
import com.study.jdbcTemplate.entity.PostDto;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);

    void update(Long postId, PostDto postDto);

    Optional<Post> findById(Long postId);

    List<Post> findAll(PostSearchCond searchCond);
}
