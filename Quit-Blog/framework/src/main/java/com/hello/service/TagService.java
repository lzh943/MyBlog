package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.TagListDto;
import com.hello.domain.entity.Tag;

public interface TagService extends IService<Tag> {
    ResponseResult tagPageList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteTag(Long id);

    ResponseResult getTagById(Long id);

    ResponseResult listAllTag();
}
