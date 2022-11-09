package com.study.jdbcTemplate.entity;

import lombok.Data;

@Data
public class Post {
    private Long id;
    private String content;
    private int viewCnt;

    public Post(String content, int viewCnt) {
        this.content = content;
        this.viewCnt = viewCnt;
    }
}