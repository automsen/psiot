package cn.com.tw.saas.serv.mapper.loadShedding;

import java.util.List;

import cn.com.tw.saas.serv.entity.loadShedding.RuleElecOnTimeUnusual;

public interface RuleElecOnTimeUnusualMapper {
    int deleteByPrimaryKey(String ruleId);

    int insert(RuleElecOnTimeUnusual record);

    int insertSelective(RuleElecOnTimeUnusual record);

    RuleElecOnTimeUnusual selectByPrimaryKey(String ruleId);

    int updateByPrimaryKeySelective(RuleElecOnTimeUnusual record);

    int updateByPrimaryKey(RuleElecOnTimeUnusual record);

	List<RuleElecOnTimeUnusual> selectByStartAndEndTime(RuleElecOnTimeUnusual reotu);

	int replace(RuleElecOnTimeUnusual info);
	
	List<RuleElecOnTimeUnusual> selectByStartAndEndTimeToAll(RuleElecOnTimeUnusual reotu);
}