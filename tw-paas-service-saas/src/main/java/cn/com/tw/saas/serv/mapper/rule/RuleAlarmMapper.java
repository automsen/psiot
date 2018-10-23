package cn.com.tw.saas.serv.mapper.rule;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.rule.RuleAlarm;
import feign.Param;

public interface RuleAlarmMapper {
    int deleteByPrimaryKey(String alarmId);

    int insert(RuleAlarm record);

    int insertSelective(RuleAlarm record);

    RuleAlarm selectByPrimaryKey(String alarmId);

    int updateByPrimaryKeySelective(RuleAlarm record);

    int updateByPrimaryKey(RuleAlarm record);

	List<RuleAlarm> selectByPage(Page arg0);

	List<RuleAlarm> selectAlarmAll(RuleAlarm ruleAlarm);

	RuleAlarm selectIsDefaultRule(String orgId, String equipType);
	
	List<RuleAlarm> selectByRuleName(String ruleName);
}