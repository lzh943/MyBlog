package com.hello.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.domain.entity.ArticleTag;
import com.hello.mapper.ArticleTagMapper;
import com.hello.service.ArticleTagService;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
}
