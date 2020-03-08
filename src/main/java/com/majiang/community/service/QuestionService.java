package com.majiang.community.service;

import com.majiang.community.dto.PaginationDTO;
import com.majiang.community.dto.QuestionDTO;
import com.majiang.community.mapper.QuestionMapper;
import com.majiang.community.mapper.UserMapper;
import com.majiang.community.model.Question;
import com.majiang.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    public PaginationDTO list(Integer page, Integer size){
/**       对应数据分页查询条件offset
 *         select * from question limit offset,size
 *                                      0   ,5  -> 1 page
 *                                      5   ,5  -> 2 page
 *                                      10  ,5  -> 3 page
 *        规律：0 = 5*（1-1）
 *             5 = 5*（2-1）
 *            10 = 5*（3-1）
 *       结论： offset = size*（page-1）
 *
 */
        PaginationDTO paginationDTO = new PaginationDTO();
//       问题总条数
        Integer totalcount = questionMapper.count();
        paginationDTO.setPagination(totalcount,page,size);
        if (page<1){
            page=1;
        }
        if (page>paginationDTO.getTotalPage()){
            page=paginationDTO.getTotalPage();
        }

        Integer offset = size*(page-1);
        List<Question> questionList = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            Integer creator = question.getCreator();
            User user = userMapper.findUserById(creator);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }
}
