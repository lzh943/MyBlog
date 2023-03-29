package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.CategoryListDto;
import com.hello.domain.entity.Category;


public interface CategoryService extends IService<Category> {
    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult categoryPageList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto);

    ResponseResult addCategory(CategoryListDto categoryListDto);

    ResponseResult getCategoryById(Long id);

    ResponseResult updateCategory(CategoryListDto categoryListDto);

    ResponseResult deleteCategory(Long id);
}
