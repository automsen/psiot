package cn.com.tw.paas.monit.service.baseEquipModel;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.baseEquipModel.BaseEquipModel;

public interface BaseEquipModelService extends IBaseSerivce<BaseEquipModel>{

	List<BaseEquipModel> selectBaseEquipModelPage(Page page);

	List<BaseEquipModel> selectBaseEquipModelAll(BaseEquipModel baseEquipModel);

	BaseEquipModel selectByEquipNumber(String equipNumber);
	
	BaseEquipModel selectByModelName(String modelName);

}
