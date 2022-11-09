package com.study.jdbcTemplate.repository;

import com.study.jdbcTemplate.entity.Post;
import com.study.jdbcTemplate.entity.PostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * extends vs implements
 * extends: 일반 클래스, 추상(abstract) 클래스 상속에 사용, 오버라이딩 필요 없이 부모 메서드/변수 사용 가능
 * implements: interface 상속에 사용, 부모는 선언만 하고 자식은 반드시 오버라이딩해서 사용
 */
@Slf4j
public class JdbcTemplateRepositoryV1 implements PostRepository {

    private final JdbcTemplate template;

    public JdbcTemplateRepositoryV1(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Post save(Post post) {
        String sql = "Insert into post(content, view_cnt) values (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, post.getContent());
            ps.setInt(2, post.getViewCnt());
            return ps;
        }, keyHolder);

        long key = keyHolder.getKey().longValue();
        post.setId(key);

        return post;
    }

    @Override
    public void update(Long postId, PostDto postDto) {
        String sql = "update post set content=?, view_cnt=? where id=?";
        template.update(sql,
                postDto.getContent(),
                postDto.getViewCnt(),
                postId);
    }

    @Override
    public Optional<Post> findById(Long postId) {
        String sql = "select id, content, view_cnt from post where id=?";

        try {
            Post post = template.queryForObject(sql, postRowMapper(), postId);
            return Optional.of(post);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Post> findAll(PostSearchCond searchCond) {
        String postName = searchCond.getPostName();
        Integer maxViewCnt = searchCond.getMaxViewCnt();

        String sql = "select id, content, view_cnt from post";
        // 동적 쿼리
        if (StringUtils.hasText(postName) || maxViewCnt != null) {
            sql += " where";
        }

        boolean andFlag = false;
        List<Object> param = new ArrayList<>();
        if (StringUtils.hasText(postName)) {
            sql += " content like concat('%',?,'%')";
            param.add(postName);
            andFlag = true;
        }
        if (maxViewCnt != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " view_cnt <= ?";
            param.add(maxViewCnt);
        }
        log.info("sql={}", sql);

        return template.query(sql, postRowMapper(), param.toArray());
    }

    private RowMapper<Post> postRowMapper() {
        return (rs, rowNum) -> {
            Post post = new Post();
            post.setId(rs.getLong("id"));
            post.setContent(rs.getString("content"));
            post.setViewCnt(rs.getInt("view_cnt"));
            return post;
        };
    }
}
