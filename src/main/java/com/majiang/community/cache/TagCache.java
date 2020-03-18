package com.majiang.community.cache;

import com.majiang.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO client = new TagDTO();
        client.setCategoryName("前端");
        client.setTags(Arrays.asList("javascript","vue.js","css","html","html5","node.js","react.js","jquery","css3","es6","typescript","chrome","npm","bootstrap","sass","less","chrome-devtools","firefox","angular","coffeescript","safari","postcss","postman","fiddler","yarn","webkit","firebug","edge"));
        tagDTOS.add(client);

        TagDTO server = new TagDTO();
        server.setCategoryName("后端");
        server.setTags(Arrays.asList("php","java","node.js","python","c++c","golang","spring","django","springboot","flask","c#","swoole","ruby","asp.net","ruby-on-rails","scala","rust","lavarel","爬虫"));
        tagDTOS.add(server);

        TagDTO mobile = new TagDTO();
        mobile.setCategoryName("移动端");
        mobile.setTags(Arrays.asList("java","android","ios","objective-c","小程序","swift","react-native","xcode","android-studi ","owebapp","flutter","kotlin"));
        tagDTOS.add(mobile);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql","redis","mongodb","sql","json","elasticsearch","nosql","memcached","postgresql","sqlite","mariadb"));
        tagDTOS.add(db);

        TagDTO AI = new TagDTO();
        AI.setCategoryName("人工智能");
        AI.setTags(Arrays.asList("算法","机器学习","人工智能","深度学习","数据挖掘","tensorflow","神经网络","图像识别","人脸识别","自然语言处理","机器人","pytorch","自动驾驶"));
        tagDTOS.add(AI);
        return tagDTOS;
    }
    public static String filterInvalid(String tags){
        String[] split = StringUtils.split(tags,",");
//       获取所有标签进行判断
        List<TagDTO> tagDTOS = TagCache.get();
        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
