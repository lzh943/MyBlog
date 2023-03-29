package com.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.TagListDto;
import com.hello.domain.entity.Tag;
import com.hello.domain.vo.PageVo;
import com.hello.domain.vo.TagListVo;
import com.hello.mapper.TagMapper;
import com.hello.service.TagService;
import com.hello.utils.BeanCopyUtils;
import com.hello.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper,Tag> implements TagService {
    @Override
    public ResponseResult tagPageList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());
        Page<Tag> tagPage=new Page(pageNum,pageSize);
        page(tagPage, queryWrapper);
        List<TagListVo> voList= BeanCopyUtils.copyBeanList(tagPage.getRecords(), TagListVo.class);
        return ResponseResult.okResult(new PageVo(voList, tagPage.getTotal()));
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        tag.setCreateBy(SecurityUtils.getUserId());
        tag.setCreateTime(new Date());
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        getBaseMapper().deleteById(id);
        return ResponseResult.okResult();
    }
    @Override
    public ResponseResult getTagById(Long id) {
        Tag tag= getBaseMapper().selectById(id);
        TagListVo vo=BeanCopyUtils.copyBean(tag, TagListVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult listAllTag() {
        List<TagListVo> tagListVos=BeanCopyUtils.copyBeanList(list(), TagListVo.class);
        return ResponseResult.okResult(tagListVos);
    }
}
