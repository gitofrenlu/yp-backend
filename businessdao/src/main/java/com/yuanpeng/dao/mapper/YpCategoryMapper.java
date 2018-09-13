package com.yuanpeng.dao.mapper;

import com.yuanpeng.dao.model.YpCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YpCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YpCategory record);

    int insertSelective(YpCategory record);

    YpCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YpCategory record);

    int updateByPrimaryKey(YpCategory record);

    List<YpCategory> getAllCate();

    int deleteAll();

}