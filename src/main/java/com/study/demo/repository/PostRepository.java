package com.study.demo.repository;

import com.study.demo.entity.Post;

public interface PostRepository {

    Post save(Post post);
    Post findById(Long id);
    void update(Long id, int viewCnt);
    void delete(Long id);
}
