package cn.com.tw.saas.serv.mapper.loadShedding;

import java.util.List;

import cn.com.tw.saas.serv.entity.loadShedding.RuleElecOnTimeUsual;

public interface RuleElecOnTimeUsualMapper {
    int deleteByPrimaryKey(String ruleId);

    int insert(RuleElecOnTimeUsual record);

    int insertSelective(RuleElecOnTimeUsual record);

    RuleElecOnTimeUsual selectByPrimaryKey(String ruleId);

    int updateByPrimaryKeySelective(RuleElecOnTimeUsual record);

    int updateByPrimaryKey(RuleElecOnTimeUsual record);

	int replace(RuleElecOnTimeUsual ruleElecOnTimeUsual);

	List<RuleElecOnTimeUsual> selectByAll(RuleElecOnTimeUsual retu);

	List<RuleElecOnTimeUsual> selectByScreeningCondition(RuleElecOnTimeUsual reotu);
	
	List<RuleElecOnTimeUsual> selectByScreeningConditionToAll(RuleElecOnTimeUsual reotu);
}