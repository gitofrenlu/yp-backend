package com.yuanpeng.dao.mapper;

import com.yuanpeng.dao.model.YpCustomer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface YpCustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YpCustomer record);

    int insertSelective(YpCustomer record);

    YpCustomer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YpCustomer record);

    int updateByPrimaryKey(YpCustomer record);

    int updateByOpenId(YpCustomer record);

    YpCustomer selectByOpenid(@Param("openId")String openId);

    int deleteAll();
}