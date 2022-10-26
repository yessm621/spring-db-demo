package com.study.demo.repository;

import com.study.demo.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

@Slf4j
public class PostRepositoryV1 {

    private final DataSource dataSource;

    // 의존관계주입. DataSource는 인터페이스이므로
    // DriverManagerDataSource -> HikariDataSource로 변경되어도 PostRepositoryV1은 변경되지 않는다.
    public PostRepositoryV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Post save(Post post) throws SQLException {
        String sql = "insert into post(id, content, view_cnt) values (?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, post.getId());
            pstmt.setString(2, post.getContent());
            pstmt.setInt(3, post.getViewCnt());
            pstmt.executeUpdate();
            return post;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, null);
        }
    }

    public Post findById(Long id) throws SQLException {
        String sql = "select * from post where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Post post = new Post();
                post.setId(rs.getLong("id"));
                post.setContent(rs.getString("content"));
                post.setViewCnt(rs.getInt("view_cnt"));
                return post;
            } else {
                throw new NoSuchElementException("post not found post_id=" + id);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public void update(Long id, int viewCnt) throws SQLException {
        String sql = "update post set view_cnt = ? where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, viewCnt);
            pstmt.setLong(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "delete from post where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }


    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}
