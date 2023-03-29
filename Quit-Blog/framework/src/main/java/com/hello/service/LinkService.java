package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.ALinkDto;
import com.hello.domain.dto.LinkDto;
import com.hello.domain.dto.ULinkDto;
import com.hello.domain.entity.Link;


public interface LinkService extends IService<Link> {
    ResponseResult getAllLink();

    ResponseResult getAllLinkPage(Integer pageNum, Integer pageSize, LinkDto linkDto);

    ResponseResult addLink(ALinkDto aLinkDto);

    ResponseResult getLinkById(Long id);

    ResponseResult updateLink(ULinkDto uLinkDto);

    ResponseResult deleteLink(Long id);
}
