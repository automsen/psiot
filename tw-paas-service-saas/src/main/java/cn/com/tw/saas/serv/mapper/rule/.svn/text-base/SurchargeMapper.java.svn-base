package cn.com.tw.saas.serv.mapper.rule;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.rule.Surcharge;

public interface SurchargeMapper {
    int deleteByPrimaryKey(String surchargeId);

    int insert(Surcharge record);

    int insertSelective(Surcharge record);

    Surcharge selectByPrimaryKey(String surchargeId);

    int updateByPrimaryKeySelective(Surcharge record);

    int updateByPrimaryKey(Surcharge record);

	List<Surcharge> selectByPage(Page page);

	List<Surcharge> selectSurchargeAll(Surcharge surcharge);
}