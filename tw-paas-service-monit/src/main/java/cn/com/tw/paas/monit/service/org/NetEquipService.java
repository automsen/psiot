package cn.com.tw.paas.monit.service.org;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.excel.NetEquipExcel;
import cn.com.tw.paas.monit.entity.business.org.NetEquipExpand;
import cn.com.tw.paas.monit.entity.db.org.NetEquip;

public interface NetEquipService extends IBaseSerivce<NetEquip>{

	List<NetEquipExpand> selectEquipByPage(Page page);

	List<NetEquipExpand> selectNetEquipExpandAll(NetEquipExpand equipExpand);

	void insertNetEquipExpand(NetEquipExpand equipExpand);

	NetEquipExpand selectByEquipId(String equipId);

	List<NetEquip> selectByEntity(NetEquip queryOrg);
	
	List<NetEquip> selectNetForApi(NetEquip param);

	void insertNetEquipExcel(NetEquipExcel orgGatewayExcel);

	NetEquipExpand selectByEquipNumber(String equipNumber);
	
	List<NetEquip> selectLikeEquipNumber(String equipNumber);

}
