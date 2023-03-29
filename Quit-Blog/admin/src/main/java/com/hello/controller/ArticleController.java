package com.hello.controller;

import com.hello.domain.ResponseResult;
import com.hello.domain.dto.AddArticleDto;
import com.hello.domain.dto.GetArticleDto;
import com.hello.domain.entity.Article;
import com.hello.domain.vo.ArticleVo;
import com.hello.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

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

    /**
     * 删除博文
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable Long id){
        return articleService.deleteArticle(id);
    }
    /**
     * 查询所有文章
     * @param pageNum
     * @param pageSize
     * @param articleDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getAllArticle(Integer pageNum, Integer pageSize, GetArticleDto articleDto){
        return articleService.getAllArticle(pageNum,pageSize,articleDto);
    }
    /**
     * 查询单篇文章
     */
    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable Long id){
        return articleService.getArticleById(id);
    }

}
