package cn.com.tw.saas.serv.service.audit;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.db.cust.DeductOrder;

public interface DeductOrderService extends IBaseSerivce<DeductOrder> {

	List<DeductOrder> selectByEntity1(DeductOrder deductOrder1);

	
}
