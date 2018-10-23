package cn.com.tw.saas.serv.service.terminal;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.terminal.SaasNet;

public interface SaasNetService extends IBaseSerivce<SaasNet>{
	SaasNet selectByNumber(String netNumber);
	List<SaasNet> selectSaasNet(SaasNet saasNet);
	List<SaasNet> selectByRegionId(String regionId);
}
