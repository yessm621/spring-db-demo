package com.study.demo.service;

import com.study.demo.entity.Post;
import com.study.demo.repository.PostRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class PostServiceV3_3 {

    private final PostRepositoryV3 postRepository;

    @Transactional
    public void accountTransfer(Long fromId, Long toId, int cnt) throws SQLException {
        bizLogic(fromId, toId, cnt);
    }

    private void bizLogic(Long fromId, Long toId, int cnt) throws SQLException {
        Post fromPost = postRepository.findById(fromId);
        Post toPost = postRepository.findById(toId);
        postRepository.update(fromId, fromPost.getViewCnt() - cnt);
        validation(toPost);
        postRepository.update(toId, toPost.getViewCnt() + cnt);
    }

    private void validation(Post post) {
        if (post.getId().equals(3L)) {
            throw new IllegalStateException("예외 발생");
        }
    }
}
