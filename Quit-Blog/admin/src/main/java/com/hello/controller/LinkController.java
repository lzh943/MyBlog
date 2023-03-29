package com.hello.controller;

import com.hello.domain.ResponseResult;
import com.hello.domain.dto.ALinkDto;
import com.hello.domain.dto.LinkDto;
import com.hello.domain.dto.ULinkDto;
import com.hello.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    /**
     * 查询所有友链
     * @param pageNum
     * @param pageSize
     * @param linkDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getAllLinkPage(Integer pageNum, Integer pageSize, LinkDto linkDto) {
        return linkService.getAllLinkPage(pageNum, pageSize, linkDto);
    }

    /**
     * 添加友链
     * @param aLinkDto
     * @return
     */
    @PostMapping()
    public ResponseResult addLink(@RequestBody ALinkDto aLinkDto){
        return linkService.addLink(aLinkDto);
    }

    /**
     * 通过id查询友链信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable Long id){
        return linkService.getLinkById(id);
    }

    /**
     * 修改友链
     * @param uLinkDto
     * @return
     */
    @PutMapping()
    public ResponseResult updateLink(@RequestBody ULinkDto uLinkDto){
        return linkService.updateLink(uLinkDto);
    }

    /**
     * 删除友链
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id){
        return linkService.deleteLink(id);
    }

}
