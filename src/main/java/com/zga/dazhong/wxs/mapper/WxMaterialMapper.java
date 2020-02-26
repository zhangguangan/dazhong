package com.zga.dazhong.wxs.mapper;

import com.zga.dazhong.wxs.modules.entity.WxMaterial;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WxMaterialMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WxMaterial record);

    int insertSelective(WxMaterial record);

    WxMaterial selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WxMaterial record);

    int updateByPrimaryKey(WxMaterial record);
}