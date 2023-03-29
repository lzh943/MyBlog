package com.hello.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hello.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
    int deleteByArticleId(Long id);
}
