package cn.com.tw.saas.serv.service.rule;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.db.rule.RuleAppend;

public interface RuleAppendService extends IBaseSerivce<RuleAppend>{

	List<RuleAppend> selectByEntity(RuleAppend ruleAppend);

}
