package cn.com.tw.saas.serv.service.rule;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.rule.EnergyAllowance;

public interface EnergyAllowanceService {

	List<EnergyAllowance> selectByPage(Page page);

	int deleteById(String ruleId);

	int insert(EnergyAllowance rule);

	EnergyAllowance selectById(String ruleId);

	int update(EnergyAllowance rule);

	List<EnergyAllowance> selectByEntity(EnergyAllowance param);

}
