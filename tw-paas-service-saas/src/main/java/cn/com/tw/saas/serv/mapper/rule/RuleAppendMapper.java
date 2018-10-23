package cn.com.tw.saas.serv.mapper.rule;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.rule.RuleAppend;

public interface RuleAppendMapper {
    int deleteByPrimaryKey(String appendId);

    int insert(RuleAppend record);

    int insertSelective(RuleAppend record);

    RuleAppend selectByPrimaryKey(String appendId);

    int updateByPrimaryKeySelective(RuleAppend record);

    int updateByPrimaryKey(RuleAppend record);

	List<RuleAppend> selectByPage(Page page);

	List<RuleAppend> selectByEntity(RuleAppend ruleAppend);
}