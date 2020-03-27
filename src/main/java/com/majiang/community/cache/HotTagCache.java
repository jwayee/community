package com.majiang.community.cache;

import com.majiang.community.dto.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component//被component标注的类是一个单例
@Data
public class HotTagCache {
    private List<String> hots = new ArrayList<>();

    public void updateTags(Map<String,Integer> tags){
        int max = 5;
        //构建一个PriorityQueue(一个基于java的优先队列，非常容易实现小顶堆和大顶堆)
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);
        tags.forEach((name,priority)->{
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);
            if (priorityQueue.size()<max){
                //如果队列中的元素小于3
                priorityQueue.add(hotTagDTO);
            }else{
                //如果队列中的元素大于3
                //--得到队列中的最小元素
                HotTagDTO minHot = priorityQueue.peek();
                if (hotTagDTO.compareTo(minHot)>0){
                    //如果当前元素大于最小元素
                    //--拿出第一个的元素queue[0]
                    priorityQueue.poll();
                    //--添加当前元素进队列
                    priorityQueue.add(hotTagDTO);
                }
            }
        });
        List<String> sortedTags = new ArrayList<>();
        //poll()拿出队列中元素从小排到大的第一个元素（索引为0）,队列元素减一
        HotTagDTO poll = priorityQueue.poll();
        while (poll!=null){
            //将元素
            sortedTags.add(0,poll.getName());
            poll=priorityQueue.poll();
        }
        hots=sortedTags;
        System.out.println(hots);
    }}
