package cn.com.tw.paas.monit.service.org;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.EquipNetStatusExpand;
import cn.com.tw.paas.monit.entity.db.org.EquipNetStatus;

public interface EquipNetStatusService extends IBaseSerivce<EquipNetStatus>{

	void updateStatus(String commAddr, String status);

	List<EquipNetStatusExpand> selectEquipNetStatusByPage(Page page);

	List<EquipNetStatusExpand> selectNetByPage(Page page);

}
