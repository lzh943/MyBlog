package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.AddArticleDto;
import com.hello.domain.dto.GetArticleDto;
import com.hello.domain.entity.Article;
import com.hello.domain.vo.ArticleVo;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    ResponseResult getAllArticle(Integer pageNum, Integer pageSize, GetArticleDto articleDto);

    ResponseResult getArticleById(Long id);

    ResponseResult updateArticle(ArticleVo articleVo);

    ResponseResult deleteArticle(Long id);
}
