package com.yuanpeng.dao.mapper;

import com.yuanpeng.dao.model.OrderbackModel;
import com.yuanpeng.dao.model.YpOrder;
import com.yuanpeng.dao.model.OrderGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;


@Mapper
public interface YpOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(YpOrder record);

    int insertSelective(YpOrder record);

    YpOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(YpOrder record);

    int updateByPrimaryKey(YpOrder record);

    List<OrderGoods> getAllOrderGoods(@Param("dateTime") String dateTime, @Param("orderStatus") Integer orderStatus, @Param("openid")String openid);

    int updateOrderStatus(@Param("orderId")Integer orderId, @Param("status")Integer status, @Param("price")BigDecimal price);

    List<OrderbackModel> getAllOrders(@Param("mobile")String mobile,@Param("status")Integer status,@Param("name")String name,@Param("current")Integer current);

    OrderbackModel getOrderMessage(@Param("id")Integer id);

    long getTotal(@Param("mobile")String mobile,@Param("status")Integer status,@Param("name")String name);

    int deleteAll();
}