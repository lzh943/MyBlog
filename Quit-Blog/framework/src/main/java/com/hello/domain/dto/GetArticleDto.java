package com.hello.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetArticleDto {
    //标题
    private String title;
    //文章摘要
    private String summary;
}
