package com.majiang.community.mapper;

import com.majiang.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmt_create,gmt_modified,creator,tag) values(#{title},#{description},"+
            "#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param("offset")Integer offset, @Param("size") Integer size);

    /**
     * 查询总条数
     * @return
     */
    @Select("select count(1) from question")
    Integer count();

    /**
     * 根据用户id查询问题并且分页
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> findByUserId(@Param(value = "userId") Integer userId, @Param(value="offset")Integer offset, @Param(value = "size") Integer size);
    @Select("select count(1) from QUESTION where CREATOR = #{userId}")
    Integer countByUserId(@Param(value = "userId") Integer userId);

    @Select("select * from question where id = #{id}")
    Question findById(Integer id);
    @Update("update question set title = #{title},description = #{description},tag = #{tag},gmt_create = #{gmtCreate} where id = #{id}")
    void update(Question dbQuestion);
}
