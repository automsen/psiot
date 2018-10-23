package cn.com.tw.saas.serv.mapper.rule;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.rule.RuleElec;

public interface RuleElecMapper {
    int deleteByPrimaryKey(String ruleId);

    int insert(RuleElec record);

    int insertSelective(RuleElec record);

    RuleElec selectByPrimaryKey(String ruleId);

    int updateByPrimaryKeySelective(RuleElec record);

    int updateByPrimaryKey(RuleElec record);

	List<RuleElec> selectByPage(Page page);

	List<RuleElec> selectByEntity(RuleElec ruleElec);
	
	List<RuleElec> selectByName(String ruleName);
}