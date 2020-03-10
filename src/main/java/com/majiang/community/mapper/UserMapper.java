package com.majiang.community.mapper;

import com.majiang.community.model.Question;
import com.majiang.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);
    @Select("select * from user where token = #{token}")
//    如果形参里面是类的形参，不用写@Param,否则需要写@Param
    User findUserByToken(@Param("token")String token);
    @Select("select * from user where id = #{id}")
    User findUserById(@Param("id") Integer id);
    @Select("select * from user where account_id = #{acountId}")
    User findUserByAccountId(String accountId);
    @Update("update user set name=#{name},avatar_url=#{avatarUrl},token=#{token},gmt_modified=#{gmtModified}")
    void update(User user);
    @Select("select * from question where id = #{id}")
    Question findQuestionById(Integer id);
}
