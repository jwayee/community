package com.majiang.community.mapper;

import com.majiang.community.model.Question;

public interface QuestionExtendMapper {
    int incView(Question question);
    int incCommentCount(Question question);
}
