package cn.com.tw.saas.serv.service.rule;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.db.rule.RuleAlarm;

public interface RuleAlarmService extends IBaseSerivce<RuleAlarm>{

	List<RuleAlarm> selectAlarmAll(RuleAlarm ruleAlarm);

}
