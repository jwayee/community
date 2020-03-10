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
        Integer totalPage;
        if (totalcount%size==0){
            // 10/5 共两页
            totalPage=totalcount/size;
        }else{
            // 11/5 余 1 页数加一 共三页
            totalPage=totalcount/size+1;
        }
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage+1;
        }
        paginationDTO.setPagination(totalPage,page);
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

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
//        分页封装页面相关属性和数据
        PaginationDTO paginationDTO = new PaginationDTO();
//       当前用户的问题总条数
        Integer totalcount = questionMapper.countByUserId(userId);
//        定义总页数
        Integer totalPage;
        if (totalcount%size==0){
            // 10/5 共两页
            totalPage = totalcount/size;
        }else{
            // 11/5 余 1 页数加一 共三页
            totalPage = totalcount/size+1;
        }
        if (page<1){
            page=1;
        }
        if (page>totalPage){
            page=totalPage;
        }
        paginationDTO.setPagination(totalPage,page);

        Integer offset = size*(page-1);
        List<Question> questionList = questionMapper.findByUserId(userId,offset,size);
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

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.findById(id);
        User user = userMapper.findUserById(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdateQuestion(Question question) {
        if (question.getId()==null){
            //插入数据
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else{
            //更新数据
            question.setGmtCreate(System.currentTimeMillis());
            questionMapper.update(question);
        }
    }
}
