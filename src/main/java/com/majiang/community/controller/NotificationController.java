package com.majiang.community.controller;

import com.majiang.community.dto.NotificationDTO;
import com.majiang.community.enums.NotificationTypeEnum;
import com.majiang.community.model.User;
import com.majiang.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request, @PathVariable(name = "id")Long id){
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id,user);
        if (notificationDTO.getType()== NotificationTypeEnum.REPLY_COMMENT.getType()
                ||notificationDTO.getType() == NotificationTypeEnum.REPLY_QUESTION.getType()){
            return "redirect:/question/"+notificationDTO.getOutId();
        }else{
            return "redirect:/";
        }
    }
}
