package com.study.demo.service;

import com.study.demo.entity.Post;
import com.study.demo.repository.PostRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class PostServiceV2 {

    private final DataSource dataSource;
    private final PostRepositoryV2 postRepository;

    public void accountTransfer(Long fromId, Long toId, int cnt) throws SQLException {

        Connection con = dataSource.getConnection();

        try {
            // 트랜잭션 시작, 수동 커밋 모드
            con.setAutoCommit(false);
            // 비즈니스 로직
            bizLogic(con, fromId, toId, cnt);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(con);
        }
    }

    private void bizLogic(Connection con, Long fromId, Long toId, int cnt) throws SQLException {
        Post fromPost = postRepository.findById(con, fromId);
        Post toPost = postRepository.findById(con, toId);
        postRepository.update(con, fromId, fromPost.getViewCnt() - cnt);
        validation(toPost);
        postRepository.update(con, toId, toPost.getViewCnt() + cnt);
    }

    private void validation(Post post) {
        if (post.getId().equals(3L)) {
            throw new IllegalStateException("예외 발생");
        }
    }

    private void release(Connection con) {
        if (con != null) {
            try {
                // 기본값인 자동 커밋 모드로 변경해야 한다.
                con.setAutoCommit(true);
                con.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }
}
