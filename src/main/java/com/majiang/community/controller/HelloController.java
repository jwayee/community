package com.majiang.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//@controller:自动扫描这个类，被spring当做bean来管理，controller:允许这个类去接受前端的请求
@Controller
public class HelloController {
    @GetMapping("/")
    public String index(){
        return "index";
    }
}
