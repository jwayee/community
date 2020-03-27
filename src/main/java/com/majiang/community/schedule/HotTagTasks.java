package com.majiang.community.schedule;

// import com.majiang.community.cache.HotTagCache;
import com.majiang.community.cache.HotTagCache;
import com.majiang.community.mapper.QuestionMapper;
import com.majiang.community.model.Question;
import com.majiang.community.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class HotTagTasks {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private HotTagCache hotTagCache;
    @Scheduled(fixedRate = 1000*60*60*3)//三个小时刷新一次
    public void hotTagSchedule() {
        int offset=0;
        int limit = 20;
        log.info("hotTag start {}",new Date());
        List<Question> list = new ArrayList<>();
        Map<String, Integer> priorities = new HashMap<>();
        while(offset==0||limit==list.size()){
            list = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, limit));
            for (Question question : list) {
                String[] tags = StringUtils.split(question.getTag(), ",");
                for (String tag : tags) {
                    // 判断priorites中是否有tag
                    Integer priority = (Integer) priorities.get(tag);
                    if (priority !=null){
                        priorities.put(tag,priority+5+question.getCommentCount()+question.getViewCount());
                    }else{
                        priorities.put(tag,5+question.getCommentCount()+question.getViewCount());
                    }
                }
            }
            offset+=limit;
        }
        hotTagCache.updateTags(priorities);
        log.info("hotTag stop {}", new Date());
    }

}
