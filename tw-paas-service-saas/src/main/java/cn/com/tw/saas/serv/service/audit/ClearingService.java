package cn.com.tw.saas.serv.service.audit;

import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.audit.Clearing;

public interface ClearingService extends IBaseSerivce<Clearing>{
	
	Clearing selectById(String id);

	void roomClearingAdd(List<Clearing> clearings);

	List<Clearing> selectByEntity(Clearing clearing);

	List<Clearing> selectClearingAudit(Clearing clearing);

	void rejectClearing(List<Clearing> clearings);

	void passClearing(List<Clearing> clearings);

	void roomClearingAdd(Clearing clearing);

	Map<String, Object> selectClearing(Clearing clearing);

	Map<String, Object> selectHisClearing(Clearing clearing);

	List<Clearing> clearingExpert(Clearing clearing);

	Map<String, Object> count(Map<String, Object> param);

}
