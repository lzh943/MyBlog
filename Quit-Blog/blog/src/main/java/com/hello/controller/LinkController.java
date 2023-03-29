package com.hello.controller;

import com.hello.annotation.SystemLog;
import com.hello.domain.ResponseResult;
import com.hello.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    /**
     * 查询所有的友链信息
     * @return
     */
    @GetMapping("/getAllLink")
    @SystemLog(BusinessName = "查询所有的友链信息")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}
