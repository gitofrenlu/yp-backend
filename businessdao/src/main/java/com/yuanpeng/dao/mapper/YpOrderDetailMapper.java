package com.yuanpeng.dao.mapper;

import com.yuanpeng.dao.model.OrderBackDetailModel;
import com.yuanpeng.dao.model.YpOrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface YpOrderDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YpOrderDetail record);

    int insertSelective(YpOrderDetail record);

    YpOrderDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YpOrderDetail record);

    int updateByPrimaryKey(YpOrderDetail record);

    List<OrderBackDetailModel> getBackDetails(@Param("orderId") Integer orderId);

    int updateDetailStatus(@Param("id") Integer id, @Param("status") Integer status, @Param("price")BigDecimal price);

    int deleteAll();

}