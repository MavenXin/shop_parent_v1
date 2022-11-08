package com.mwx.controller;

import com.mwx.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Hojin
 * @date 2022/11/4 18:18
 */
@RestController
@RequestMapping("/product/")
public class TestController {
    @Resource
    private TestService testService;

    @GetMapping("redis")
    public void testRedis(){
        testService.getInfo();

    }
}
