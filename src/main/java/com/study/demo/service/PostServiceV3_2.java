package com.study.demo.service;

import com.study.demo.entity.Post;
import com.study.demo.repository.PostRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class PostServiceV3_2 {

    // 트랜잭션 템플릿 사용
    private final TransactionTemplate txTemplate;
    private final PostRepositoryV3 postRepository;

    public PostServiceV3_2(PlatformTransactionManager transactionManager, PostRepositoryV3 postRepository) {
        this.txTemplate = new TransactionTemplate(transactionManager);
        this.postRepository = postRepository;
    }

    public void accountTransfer(Long fromId, Long toId, int cnt) throws SQLException {

        txTemplate.executeWithoutResult((status) -> {
            try {
                // 비즈니스 로직
                bizLogic(fromId, toId, cnt);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
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
