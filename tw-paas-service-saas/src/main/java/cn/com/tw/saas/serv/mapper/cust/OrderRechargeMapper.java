package cn.com.tw.saas.serv.mapper.cust;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.cust.OrderRecharge;

public interface OrderRechargeMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(OrderRecharge record);

    int insertSelective(OrderRecharge record);

    OrderRecharge selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(OrderRecharge record);

    int updateByPrimaryKey(OrderRecharge record);

	List<OrderRecharge> selectOrderRechargeByPage(Page page);

	List<OrderRecharge> selectByPage(Page page);
	
	List<OrderRecharge> selectByEntity(OrderRecharge record);

	List<OrderRecharge> sourceOfFundExpert(OrderRecharge orderRecharge);
}