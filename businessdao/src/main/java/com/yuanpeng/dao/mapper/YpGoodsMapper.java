package com.yuanpeng.dao.mapper;

import com.yuanpeng.dao.model.YpGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YpGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YpGoods record);

    int insertSelective(YpGoods record);

    YpGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YpGoods record);

    int updateByPrimaryKey(YpGoods record);

    List<YpGoods> getAllGoodsByStatus(@Param("status") Integer status);

    List<YpGoods> getAllGoods(@Param("status")Integer status,@Param("name")String name);

    String getOldImg(@Param("id") Integer id);

    int deleteAll();

}