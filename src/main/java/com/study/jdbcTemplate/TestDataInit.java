package com.study.jdbcTemplate;

import com.study.jdbcTemplate.entity.Post;
import com.study.jdbcTemplate.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {

    private final PostRepository postRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        postRepository.save(new Post("postA", 10));
        postRepository.save(new Post("postB", 20));
    }

}
