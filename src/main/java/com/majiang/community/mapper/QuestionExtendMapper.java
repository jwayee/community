package com.majiang.community.mapper;

import com.majiang.community.model.Question;

import java.util.List;

public interface QuestionExtendMapper {
    int incView(Question question);

    int incCommentCount(Question question);

    List<Question> selectRelated(Question question);
}
