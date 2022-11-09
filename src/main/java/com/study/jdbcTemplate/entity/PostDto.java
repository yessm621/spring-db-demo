package com.study.jdbcTemplate.entity;

import lombok.Data;

@Data
public class PostDto {
    private String content;
    private int viewCnt;

    public PostDto() {
    }

    public PostDto(String content, int viewCnt) {
        this.content = content;
        this.viewCnt = viewCnt;
    }
}
