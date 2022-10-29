package com.study.demo.service;

import com.study.demo.entity.Post;
import com.study.demo.repository.PostRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class PostServiceV3_1 {

    // 트랜잭션 매니저 주입 받음
    private final PlatformTransactionManager transactionManager;
    private final PostRepositoryV3 postRepository;

    public void accountTransfer(Long fromId, Long toId, int cnt) throws SQLException {

        // 트랜잭션 시작. 트랜잭션의 상태정보를 반환. 이 정보는 트랜잭션 커밋, 롤백 시 필요
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 비즈니스 로직
            bizLogic(fromId, toId, cnt);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
        }
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
