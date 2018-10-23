package cn.com.tw.saas.serv.service.rule;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.db.rule.RuleElec;

public interface RuleElecService extends IBaseSerivce<RuleElec>{

	List<RuleElec> selectByEntity(RuleElec ruleElec);

}
