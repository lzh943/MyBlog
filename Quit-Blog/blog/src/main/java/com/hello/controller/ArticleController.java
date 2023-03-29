package com.hello.controller;


import com.hello.annotation.SystemLog;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.AddArticleDto;
import com.hello.domain.entity.Article;
import com.hello.domain.vo.ArticleVo;
import com.hello.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;

    /**
     * 查询热门文章
     * @return
     */
    @GetMapping("/hotArticleList")
    @SystemLog(BusinessName = "查询热门文章")
    public ResponseResult hotArticleList(){
        return articleService.hotArticleList();
    }

    /**
     * 查询所有文章,有id查询分类下的所有文章
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @RequestMapping("/articleList")
    @SystemLog(BusinessName = "查询所有文章,有id查询分类下的所有文章")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    /**
     * 查看文章详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @SystemLog(BusinessName = "查看文章详情")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    /**
     * 更新浏览次数
     * @param id
     * @return
     */
    @PutMapping("/updateViewCount/{id}")
    @SystemLog(BusinessName = "更新浏览次数")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
    /**
     * 写博文
     * @param article
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }

    /**
     * 修改博文
     * @param articleVo
     * @return
     */
    @PutMapping
    private ResponseResult update(@RequestBody ArticleVo articleVo){
        return articleService.updateArticle(articleVo);
    }
}
