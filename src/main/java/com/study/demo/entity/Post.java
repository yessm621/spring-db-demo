package com.study.demo.entity;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Post {
    private Long id;
    private String content;
    private int viewCnt;

    public Post(String content, int viewCnt) {
        this.content = content;
        this.viewCnt = viewCnt;
    }
}
