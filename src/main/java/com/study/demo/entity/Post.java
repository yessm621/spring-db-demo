package com.study.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Long id;
    private String content;
    private int viewCnt;

    public Post(String content, int viewCnt) {
        this.content = content;
        this.viewCnt = viewCnt;
    }
}
