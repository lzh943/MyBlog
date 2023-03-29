package com.hello.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.CategoryListDto;
import com.hello.domain.entity.Category;
import com.hello.domain.vo.ExcelCategoryVo;
import com.hello.enums.AppHttpCodeEnum;
import com.hello.service.CategoryService;
import com.hello.utils.BeanCopyUtils;
import com.hello.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有分类信息
     * @param pageNum
     * @param pageSize
     * @param categoryListDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult categoryPageList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto){
        return categoryService.categoryPageList(pageNum,pageSize,categoryListDto);
    }

    /**
     * 添加分类
     * @param categoryListDto
     * @return
     */
    @PostMapping()
    public ResponseResult addCategory(@RequestBody CategoryListDto categoryListDto){
        return categoryService.addCategory(categoryListDto);
    }

    /**
     * 根据id获取分类信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }
    /**
     * 修改分类信息
     * @param categoryListDto
     * @return
     */
    @PutMapping()
    public ResponseResult updateCategory(@RequestBody CategoryListDto categoryListDto){
        return categoryService.updateCategory(categoryListDto);
    }

    /**
     * 删除分类信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }
    /**
     * 获取全部分类名
     * @return
     */
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }

    /**
     * 分类信息导出
     * @param response
     */
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}