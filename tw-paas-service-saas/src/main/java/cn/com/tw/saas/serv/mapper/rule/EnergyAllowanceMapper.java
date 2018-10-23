/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.rule;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.rule.EnergyAllowance;

public interface EnergyAllowanceMapper {
    int deleteByPrimaryKey(String ruleId);

    int insert(EnergyAllowance record);

    EnergyAllowance selectByPrimaryKey(String ruleId);

    int updateByPrimaryKeySelective(EnergyAllowance record);
    
    List<EnergyAllowance> selectByPage(Page page);

    List<EnergyAllowance> selectByEntity(EnergyAllowance record);
}