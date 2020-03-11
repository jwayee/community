package com.majiang.community.controller;

import com.majiang.community.dto.QuestionDTO;
import com.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(value = "id") Integer id,
                           Model model){
        questionService.incView(id);
        QuestionDTO questionDTO = questionService.getById(id);
        //累加阅读数
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
