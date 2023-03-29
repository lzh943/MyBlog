package com.hello.job;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hello.domain.entity.Article;
import com.hello.service.ArticleService;
import com.hello.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Autowired
    RedisCache redisCache;
    @Autowired
    ArticleService articleService;
    @Scheduled(cron = "0/30 * * * * ?")
    @Transactional
    public void updateViewCount() {
        // 从缓存获取浏览量
        Map<String, Integer> map = redisCache.getCacheMap("article:viewCount");
        //更新数据库
        List<Article> articles = map.entrySet()
                .stream()
                .map(item -> new Article(Long.valueOf(item.getKey()), Long.valueOf(item.getValue())))
                .collect(Collectors.toList());
        for (Article article : articles) {
            LambdaUpdateWrapper<Article> wrapper = Wrappers.lambdaUpdate(Article.class)
                    .eq(Article::getId, article.getId())
                    .set(Article::getViewCount, article.getViewCount());
            articleService.update(wrapper);
        }
    }
}
