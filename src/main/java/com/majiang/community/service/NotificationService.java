package com.majiang.community.service;

import com.majiang.community.dto.NotificationDTO;
import com.majiang.community.dto.PaginationDTO;
import com.majiang.community.enums.NotificationStatusEnum;
import com.majiang.community.enums.NotificationTypeEnum;
import com.majiang.community.exception.CustomizeErrorCode;
import com.majiang.community.exception.CustomizeException;
import com.majiang.community.mapper.NotificationMapper;
import com.majiang.community.mapper.UserMapper;
import com.majiang.community.model.Notification;
import com.majiang.community.model.NotificationExample;
import com.majiang.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    UserMapper userMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {
//        分页封装页面相关属性和数据
        PaginationDTO paginationDTO = new PaginationDTO();
//       当前用户的问题总条数
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        int totalcount = (int) notificationMapper.countByExample(example);
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
        NotificationExample example1 = new NotificationExample();
        example1.createCriteria().andReceiverEqualTo(userId);
        example1.setOrderByClause("gmt_create desc");
        List<Notification> notificationList = notificationMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));
        if (notificationList.size()==0){
            return paginationDTO;
        }
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        for (Notification notification : notificationList) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOs.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOs);

        return paginationDTO;
    }

    public Long unreadCount(Long UserId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(UserId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        long count = notificationMapper.countByExample(notificationExample);
        return count;
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification==null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (user.getId()!=notification.getReceiver()){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
//      标记为已读(READ(1))
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
