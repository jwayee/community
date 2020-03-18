package com.majiang.community.mapper;

import com.majiang.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}