package com.majiang.community.controller;

import com.majiang.community.dto.CommentCreateDTO;
import com.majiang.community.dto.CommentDTO;
import com.majiang.community.dto.ResultDTO;
import com.majiang.community.enums.CommentTypeEnum;
import com.majiang.community.exception.CustomizeErrorCode;
import com.majiang.community.model.Comment;
import com.majiang.community.model.User;
import com.majiang.community.service.CommentService;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;
    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentCreateDTO==null|| StringUtils.isNullOrEmpty(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_COTENT_NOT_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setCommentator(user.getId());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        commentService.insert(comment,user);
//        评论成功，返回封装信息对象
        return ResultDTO.okOf();
    }
    @ResponseBody
    @RequestMapping("/comment/{id}")
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name="id") Long id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
