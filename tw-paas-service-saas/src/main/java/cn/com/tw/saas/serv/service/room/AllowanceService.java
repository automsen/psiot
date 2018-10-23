package cn.com.tw.saas.serv.service.room;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.AllowancePlan;
import cn.com.tw.saas.serv.entity.room.AllowanceRecord;

public interface AllowanceService {

	int savePlan(AllowancePlan plan);
	
	List<AllowancePlan> selectByPage(Page param);
	
	List<AllowanceRecord> selectByRoomId(String roomId);

	int executePlan();

	int cancelPlan(String id);
}
