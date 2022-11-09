package com.study.jdbcTemplate.service;

import com.study.jdbcTemplate.entity.Post;
import com.study.jdbcTemplate.entity.PostDto;
import com.study.jdbcTemplate.repository.PostRepository;
import com.study.jdbcTemplate.repository.PostSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceV1 implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void update(Long postId, PostDto postDto) {
        postRepository.update(postId, postDto);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findPosts(PostSearchCond postSearch) {
        return postRepository.findAll(postSearch);
    }
}
