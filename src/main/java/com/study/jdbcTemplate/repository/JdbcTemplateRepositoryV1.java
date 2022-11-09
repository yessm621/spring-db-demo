package com.study.jdbcTemplate.repository;

import com.study.jdbcTemplate.entity.Post;
import com.study.jdbcTemplate.entity.PostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
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
        return null;
    }

    @Override
    public void update(Long postId, PostDto postDto) {

    }

    @Override
    public Optional<Post> findById(Long postId) {
        return Optional.empty();
    }

    @Override
    public List<Post> findAll(PostSearchCond searchCond) {
        return null;
    }
}
