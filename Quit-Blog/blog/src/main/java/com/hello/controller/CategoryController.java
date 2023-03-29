package com.hello.controller;

import com.hello.annotation.SystemLog;
import com.hello.domain.ResponseResult;
import com.hello.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询分类id和名称
     * @return
     */
    @RequestMapping("/getCategoryList")
    @SystemLog(BusinessName = "查询分类id和名称")
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();
    }
    /**
     * 获取全部分类名
     * @return
     */
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }
}
