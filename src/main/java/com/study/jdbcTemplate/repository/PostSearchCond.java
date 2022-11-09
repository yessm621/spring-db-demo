package com.study.jdbcTemplate.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchCond {
    private String postName;
    private Integer maxViewCnt;
}
