package com.study.jdbcTemplate.config;

import com.study.jdbcTemplate.repository.JdbcTemplateRepositoryV1;
import com.study.jdbcTemplate.repository.PostRepository;
import com.study.jdbcTemplate.service.PostService;
import com.study.jdbcTemplate.service.PostServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateV1Config {

    private final DataSource dataSource;

    @Bean
    public PostService postService() {
        return new PostServiceV1(postRepository());
    }

    @Bean
    public PostRepository postRepository() {
        return new JdbcTemplateRepositoryV1(dataSource);
    }
}
