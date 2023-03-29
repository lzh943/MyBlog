package com.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.constants.SystemConstants;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.AddArticleDto;
import com.hello.domain.dto.GetArticleDto;
import com.hello.domain.entity.Article;
import com.hello.domain.entity.ArticleTag;
import com.hello.domain.entity.Category;
import com.hello.domain.entity.Tag;
import com.hello.domain.vo.*;
import com.hello.mapper.ArticleMapper;
import com.hello.mapper.ArticleTagMapper;
import com.hello.service.ArticleService;
import com.hello.service.ArticleTagService;
import com.hello.service.CategoryService;
import com.hello.utils.BeanCopyUtils;
import com.hello.utils.RedisCache;
import com.hello.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    RedisCache redisCache;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ArticleTagService articleTagService;
    @Autowired
    ArticleTagMapper articleTagMapper;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章,封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> articlePage=new Page(1,10);
        page(articlePage,queryWrapper);
        List<Article> articles=articlePage.getRecords();
        //进行vo处理
        List<HotArticleVo> articleVos= BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //分页查询 重点为判断categoryId是否为空
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0, Article::getCategoryId,categoryId);
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getIsTop);
        Page<Article> articlePage=new Page<>(pageNum,pageSize);
        page(articlePage, queryWrapper);
        List<Article> articles=articlePage.getRecords();
        //获取categoryName
        for (Article article : articles) {
            Category category= categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }
        //进行vo操作
        List<ArticleListVo> articleListVos=BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo=new PageVo(articleListVos,articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        Article article=getById(id);
        article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        ArticleDetailVo detailVo=BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        return ResponseResult.okResult(detailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),SystemConstants.ARTICLE_VIEWCOUNT);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        List<Long> tagIds=articleDto.getTags();
        Article article=BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags=tagIds.stream()
                .map(tagId->new ArticleTag(article.getId(),tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAllArticle(Integer pageNum, Integer pageSize, GetArticleDto articleDto) {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.like(StringUtils.hasText(articleDto.getTitle()),
                Article::getTitle,articleDto.getTitle());
        queryWrapper.like(StringUtils.hasText(articleDto.getSummary()),
                Article::getSummary,articleDto.getSummary());
        Page<Article> articlePage=new Page(pageNum,pageSize);
        page(articlePage, queryWrapper);
        List<AdminArticleVo> articleVos=BeanCopyUtils.copyBeanList(articlePage.getRecords(),AdminArticleVo.class);
        return ResponseResult.okResult(new PageVo(articleVos, articlePage.getTotal()));
    }

    @Override
    public ResponseResult getArticleById(Long id) {
        Article article=getById(id);
        LambdaQueryWrapper<ArticleTag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,article.getId());
        List<ArticleTag> tags=articleTagService.list(queryWrapper);
        List<Long> tagIds=tags.stream()
                .map(tag->new Long(tag.getTagId()))
                .collect(Collectors.toList());
        ArticleVo articleVo=BeanCopyUtils.copyBean(article, ArticleVo.class);
        articleVo.setTags(tagIds);
        return ResponseResult.okResult(articleVo);
    }

    @Override
    public ResponseResult updateArticle(ArticleVo articleVo) {
        Article article=BeanCopyUtils.copyBean(articleVo, Article.class);
        article.setUpdateBy(SecurityUtils.getUserId());
        article.setUpdateTime(new Date());
        updateById(article);
        articleTagMapper.deleteByArticleId(article.getId());
        List<ArticleTag> articleTags=articleVo.getTags().stream()
                .map(tag->new ArticleTag(article.getId(), tag))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        getBaseMapper().deleteById(id);
        return ResponseResult.okResult();
    }
}
