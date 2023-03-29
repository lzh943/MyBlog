package com.hello.controller;

import com.hello.domain.ResponseResult;
import com.hello.domain.dto.TagListDto;
import com.hello.domain.entity.Tag;
import com.hello.service.TagService;
import com.hello.utils.BeanCopyUtils;
import com.hello.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 查询标签列表并分页
     * @param pageNum
     * @param pageSize
     * @param tagListDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult tagPageList(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.tagPageList(pageNum,pageSize,tagListDto);
    }

    /**
     * 添加标签信息
     * @param tagListDto
     * @return
     */
    @PostMapping()
    public ResponseResult addTag(@RequestBody TagListDto tagListDto){
        Tag tag= BeanCopyUtils.copyBean(tagListDto, Tag.class);
        return tagService.addTag(tag);
    }

    /**
     * 删除标签信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Long id){
        return tagService.deleteTag(id);
    }

    /**
     * 查询单个标签信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getTagById(@PathVariable Long id){
        return tagService.getTagById(id);
    }

    /**
     * 修改标签信息
     * @param tagListDto
     * @return
     */
    @PutMapping()
    public ResponseResult updateTagById(@RequestBody TagListDto tagListDto){
        Tag tag= BeanCopyUtils.copyBean(tagListDto, Tag.class);
        tag.setUpdateBy(SecurityUtils.getUserId());
        tag.setUpdateTime(new Date());
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

    /**
     * 获取全部标签名
     * @return
     */
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }
}