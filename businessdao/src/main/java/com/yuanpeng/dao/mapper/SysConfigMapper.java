package com.yuanpeng.dao.mapper;

import com.yuanpeng.dao.model.AccessTokenModel;
import com.yuanpeng.dao.model.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysConfig record);

    int insertSelective(SysConfig record);

    SysConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysConfig record);

    int updateByPrimaryKey(SysConfig record);

    List<SysConfig>  getAccessToken(@Param("keys") List<String> keys);

    int updateByKey(@Param("key") String key,@Param("value") String value);

}