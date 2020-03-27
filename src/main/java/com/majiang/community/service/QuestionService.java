package com.majiang.community.service;

import com.majiang.community.dto.PaginationDTO;
import com.majiang.community.dto.QuestionDTO;
import com.majiang.community.dto.QuestionQueryDTO;
import com.majiang.community.exception.CustomizeErrorCode;
import com.majiang.community.exception.CustomizeException;
import com.majiang.community.mapper.QuestionExtendMapper;
import com.majiang.community.mapper.QuestionMapper;
import com.majiang.community.mapper.UserMapper;
import com.majiang.community.model.Question;
import com.majiang.community.model.QuestionExample;
import com.majiang.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtendMapper questionExtendMapper;
    public PaginationDTO list(Integer page, Integer size, String search, String tag){
        // 处理search
        if (StringUtils.isNotBlank(search)){
            String[] split = StringUtils.split(search, " ");
            search = Arrays.stream(split).collect(Collectors.joining("|"));
        }
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
      // 问题总条数
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setTag(tag);
        Integer totalcount = null;
        try {
            totalcount = questionExtendMapper.countBySearch(questionQueryDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        if (totalPage!=0){
            if (page>totalPage){
                page=totalPage+1;
            }
        }
        paginationDTO.setPagination(totalPage,page);
        Integer offset = size*(page-1);
        questionQueryDTO.setPage(offset);
        questionQueryDTO.setSize(size);
        List<Question> questionList = null;
        try {
            questionList = questionExtendMapper.selectBySearch(questionQueryDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            Long creator = question.getCreator();
            User user = userMapper.selectByPrimaryKey(creator);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
//        分页封装页面相关属性和数据
        PaginationDTO paginationDTO = new PaginationDTO();
//       当前用户的问题总条数
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalcount = (int)questionMapper.countByExample(example);
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
        QuestionExample example1 = new QuestionExample();
        example1.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questionList) {
            Long creator = question.getCreator();
            User user = userMapper.selectByPrimaryKey(creator);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = null;
        try {
            question = questionMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTOIN_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
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
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }else{
            //更新数据

            Question updateQuestion = new Question();
            updateQuestion.setGmtCreate(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTOIN_NOT_FOUND);
            }

        }
    }

    public void incView(Long id) {
        Question updateQuestion = new Question();
        updateQuestion.setId(id);
        updateQuestion.setViewCount(1);
        questionExtendMapper.incView(updateQuestion);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(questionDTO.getTag(), ",");
        String regexTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexTag);
        List<Question> questions = questionExtendMapper.selectRelated(question);
//      lambda表达式遍历集合将questions转换成questionDTO
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO1 = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
