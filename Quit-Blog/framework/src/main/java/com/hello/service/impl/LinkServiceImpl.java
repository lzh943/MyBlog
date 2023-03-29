package com.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.constants.SystemConstants;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.ALinkDto;
import com.hello.domain.dto.LinkDto;
import com.hello.domain.dto.ULinkDto;
import com.hello.domain.entity.Link;
import com.hello.domain.vo.LinkVo;
import com.hello.domain.vo.PageVo;
import com.hello.mapper.LinkMapper;
import com.hello.service.LinkService;
import com.hello.utils.BeanCopyUtils;
import com.hello.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> linkList=list(queryWrapper);
        List<LinkVo> linkVos= BeanCopyUtils.copyBeanList(linkList, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult getAllLinkPage(Integer pageNum, Integer pageSize, LinkDto linkDto) {
        LambdaQueryWrapper<Link> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(linkDto.getName())
                ,Link::getName,linkDto.getName());
        queryWrapper.eq(StringUtils.hasText(linkDto.getStatus()),
                Link::getStatus, linkDto.getStatus());
        Page<Link> linkPage=new Page(pageNum,pageSize);
        page(linkPage, queryWrapper);
        List<LinkVo> linkVos=BeanCopyUtils.copyBeanList(linkPage.getRecords(),LinkVo.class);
        return ResponseResult.okResult(new PageVo(linkVos, linkPage.getTotal()));
    }

    @Override
    public ResponseResult addLink(ALinkDto aLinkDto) {
        Link link=BeanCopyUtils.copyBean(aLinkDto, Link.class);
        link.setCreateBy(SecurityUtils.getUserId());
        link.setCreateTime(new Date());
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkById(Long id) {
        Link link=getBaseMapper().selectById(id);
        LinkVo linkVo=BeanCopyUtils.copyBean(link, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @Override
    public ResponseResult updateLink(ULinkDto uLinkDto) {
        Link link=BeanCopyUtils.copyBean(uLinkDto, Link.class);
        link.setUpdateBy(SecurityUtils.getUserId());
        link.setUpdateTime(new Date());
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(Long id) {
        getBaseMapper().deleteById(id);
        return ResponseResult.okResult();
    }
}
