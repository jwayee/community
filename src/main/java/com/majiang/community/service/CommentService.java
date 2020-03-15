package com.majiang.community.service;

import com.majiang.community.dto.CommentDTO;
import com.majiang.community.enums.CommentTypeEnum;
import com.majiang.community.exception.CustomizeErrorCode;
import com.majiang.community.exception.CustomizeException;
import com.majiang.community.mapper.CommentMapper;
import com.majiang.community.mapper.QuestionExtendMapper;
import com.majiang.community.mapper.QuestionMapper;
import com.majiang.community.mapper.UserMapper;
import com.majiang.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    QuestionExtendMapper questionExtendMapper;
    @Transactional//事务注解
    public void insert(Comment comment) {
        if (comment.getParentId()==null||comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType()== null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else{
            //回复问题
            Question dbQuestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (dbQuestion==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTOIN_NOT_FOUND);
            }
            commentMapper.insert(comment);
//          添加评论数
            Question question = new Question();
            question.setCommentCount(1);
            question.setId(comment.getParentId());
            int updated = questionExtendMapper.incCommentCount(question);
            if (updated!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTOIN_NOT_FOUND);
            }
        }
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum commentTypeEnum) {
//        根据id和type确认问题评论并且返回评论列表
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(commentTypeEnum.getType());
        example.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments.size()==0){
            return new ArrayList<>();
        }
//        lambda语法，函数式编程：comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
//      1.获取去重的评论人(一个问题有多个评论人，也有重复评论的人)
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
//      转换成List集合,用于andIdIn(userIds);
        ArrayList<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

//      2.获取评论人并转换成map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> userList = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

//      3.转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
//        暴力破解,时间复杂度为n*n
//        List<CommentDTO> commentDTOS = new ArrayList<>();
//        for (Comment comment : comments) {
//            CommentDTO commentDTO = new CommentDTO();
//            BeanUtils.copyProperties(comment,commentDTO);
//            User user = userMapper.selectByPrimaryKey(comment.getCommentator());
//            commentDTO.setUser(user);
//            commentDTOS.add(commentDTO);
//
//        }
    }
}
