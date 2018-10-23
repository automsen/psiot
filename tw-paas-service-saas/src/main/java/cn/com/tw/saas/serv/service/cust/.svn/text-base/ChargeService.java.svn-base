package cn.com.tw.saas.serv.service.cust;

import java.util.Map;

import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.db.cust.OrderRecharge;
import cn.com.tw.saas.serv.entity.db.cust.ThirdOrder;

public interface ChargeService {
	Map<String, Object> charge(CustRoomParam param);
	
	ThirdOrder createWechatOrder(ThirdOrder order);

	String wechatChargeSuccess(ThirdOrder order);

	String wechatChargeFail(ThirdOrder order);
	
	void repeal(String recordId) throws Exception;
	
}
