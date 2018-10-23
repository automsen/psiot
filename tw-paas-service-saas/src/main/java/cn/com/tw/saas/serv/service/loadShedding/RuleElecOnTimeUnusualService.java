package cn.com.tw.saas.serv.service.loadShedding;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.loadShedding.RuleElecOnTimeUnusual;

public interface RuleElecOnTimeUnusualService extends IBaseSerivce<RuleElecOnTimeUnusual> {

	List<RuleElecOnTimeUnusual> selectByStartAndEndTime(RuleElecOnTimeUnusual reotu);

	int replaceRuleElecOnTimeUnusual(RuleElecOnTimeUnusual info);

	List<RuleElecOnTimeUnusual> selectBySomeDay(RuleElecOnTimeUnusual info);
}
