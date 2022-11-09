package com.study.jdbcTemplate;

import com.study.jdbcTemplate.config.JdbcTemplateV1Config;
import com.study.jdbcTemplate.repository.PostRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Import(JdbcTemplateV1Config.class)
@SpringBootApplication(scanBasePackages = "com.study.jdbcTemplate")
public class jdbcTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.study.jdbcTemplate.jdbcTemplateApplication.class, args);
    }

    @Bean
    @Profile("local")
    public TestDataInit testDataInit(PostRepository postRepository) {
        return new TestDataInit(postRepository);
    }
}