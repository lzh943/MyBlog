package com.hello.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.constants.SystemConstants;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.CategoryListDto;
import com.hello.domain.entity.Article;
import com.hello.domain.entity.Category;
import com.hello.domain.vo.CategoryVo;
import com.hello.domain.vo.HotCategoryVo;
import com.hello.domain.vo.PageVo;
import com.hello.mapper.ArticleMapper;
import com.hello.mapper.CategoryMapper;
import com.hello.service.ArticleService;
import com.hello.service.CategoryService;
import com.hello.utils.BeanCopyUtils;
import com.hello.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(CategoryVo)表服务实现类
 *
 * @author makejava
 * @since 2022-12-16 15:32:20
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //查询符合条件的分类id并去重
        LambdaQueryWrapper<Article> articleWapper=new LambdaQueryWrapper<>();
        articleWapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList=articleService.list(articleWapper);
        Set<Long> categoryId=articleList.stream()
                .map(article ->article.getCategoryId())
                .collect(Collectors.toSet());
        //查询分类表进行过滤,vo操作得到需要的数据
        List<Category> categories=listByIds(categoryId);
        categories=categories.stream()
                .filter(category -> SystemConstants.CATEGORY_STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        List<HotCategoryVo> categoryVos=BeanCopyUtils.copyBeanList(categories, HotCategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus, SystemConstants.CATEGORY_STATUS_NORMAL);
        List<Category> categories=list();
        List<CategoryVo> categoryVos=BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult categoryPageList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto) {
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(categoryListDto.getStatus()),
                Category::getStatus, categoryListDto.getStatus());
        queryWrapper.like(StringUtils.hasText(categoryListDto.getName()),
                Category::getName,categoryListDto.getName());
        Page<Category> categoryPage=new Page(pageNum,pageSize);
        page(categoryPage,queryWrapper);
        List<CategoryVo> categoryVos=BeanCopyUtils.copyBeanList(categoryPage.getRecords(), CategoryVo.class);
        return ResponseResult.okResult(new PageVo(categoryVos, categoryPage.getTotal()));
    }

    @Override
    public ResponseResult addCategory(CategoryListDto categoryListDto) {
        Category category=BeanCopyUtils.copyBean(categoryListDto, Category.class);
        category.setCreateBy(SecurityUtils.getUserId());
        category.setCreateTime(new Date());
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category=getBaseMapper().selectById(id);
        CategoryVo categoryVo=BeanCopyUtils.copyBean(category, CategoryVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult updateCategory(CategoryListDto categoryListDto) {
        Category category=BeanCopyUtils.copyBean(categoryListDto, Category.class);
        category.setUpdateBy(SecurityUtils.getUserId());
        category.setUpdateTime(new Date());
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(Long id) {
        getBaseMapper().deleteById(id);
        return ResponseResult.okResult();
    }
}
