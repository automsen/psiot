package cn.com.tw.saas.serv.mapper.room;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.SaasSurchargeRoom;

public interface SaasSurchargeRoomMapper {
    int deleteByPrimaryKey(String surchargeRommId);

    int insert(SaasSurchargeRoom record);

    int insertSelective(SaasSurchargeRoom record);

    SaasSurchargeRoom selectByPrimaryKey(String surchargeRommId);

    int updateByPrimaryKeySelective(SaasSurchargeRoom record);

    int updateByPrimaryKey(SaasSurchargeRoom record);
    
    List<SaasSurchargeRoom> selectByPage(Page page);
}