package com.majiang.community.controller;

import com.majiang.community.dto.CommentDTO;
import com.majiang.community.dto.QuestionDTO;
import com.majiang.community.enums.CommentTypeEnum;
import com.majiang.community.model.User;
import com.majiang.community.service.CommentService;
import com.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(value = "id") Long id,
                           Model model,
                           HttpServletRequest request){
        User sessionUser = (User)request.getSession().getAttribute("user");
        User user = new User();
        if (sessionUser==null){
            user.setName("用户名");
            user.setAvatarUrl("/img/avatar-max-img.png");
        }
        questionService.incView(id);
        QuestionDTO questionDTO = questionService.getById(id);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        //累加阅读数
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("user",user);
        return "question";
    }
}
