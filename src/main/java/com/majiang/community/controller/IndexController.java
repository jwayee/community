package com.majiang.community.controller;

import com.majiang.community.dto.PaginationDTO;
import com.majiang.community.mapper.UserMapper;
import com.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@controller:自动扫描这个类，被spring当做bean来管理，controller:允许这个类去接受前端的请求
@Controller
public class IndexController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;
    @GetMapping("/")
    public String index(
                        Model model,
                        @RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "size",defaultValue = "5") Integer size,
                        @RequestParam(value = "search",required = false)String search){
//        以数据库中的字段token作为cookie解决的问题：比如说有的用户正在使用我们的网站，我们的网站正好直接重启或者是连接断开了，当我们重启或者修复这个问题的时候，发现这个登录就失效了，每次重启我们都需要用户登录一下，那就相当麻烦了，所以我们就需要一种方式，通过页面中的key就行登录。
//        那么为什么不把用户信息存到前端cookie中呢，只存一个令牌或者token来前后端交换使用（gitHub里也是这样的，大部分网站也是这样做到）
//          问题：当用户访问量大的时候,我们还用数据库校验这种方式,成本会非常高或者说请求非常慢,我们这里只是通过大家小范围,小用户量的场景去给大家更仔细的剖析一下这种token和session，包括这种一次性访问、分布式session的一种方式，深入了解这套机制后，我们可以用redis,或者其他方式做这种机制

        PaginationDTO paginationDTO = questionService.list(page,size,search);
        model.addAttribute("paginationDTO",paginationDTO);
        model.addAttribute("search",search);
        return "index";
    }
}
