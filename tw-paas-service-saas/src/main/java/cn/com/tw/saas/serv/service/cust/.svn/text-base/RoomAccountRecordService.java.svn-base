package cn.com.tw.saas.serv.service.cust;

import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.cust.OrderRecharge;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;

public interface RoomAccountRecordService{

	RoomAccountRecord selectById(String id);

	List<RoomAccountRecord> selectByPage(Page page);
	
	List<RoomAccountRecord> selectByPage2(Page page);
	
	Map<String,Object> count(Map<String,Object> param);

	List<RoomAccountRecord> selectByEntity(RoomAccountRecord param);

	List<RoomAccountRecord> selectAccountRecord(String userId);

	List<RoomAccountRecord> selectNewAccountRecord(RoomAccountRecord roomAccountRecord);
	
	List<RoomAccountRecord> roomAccountRecordExpert(RoomAccountRecord roomAccountRecord);
	
	List<RoomAccountRecord> roomOrderSourceAnalysis(Page page);
	
	List<RoomAccountRecord> roomOrderSourceExport(RoomAccountRecord roomAccountRecord);
	
	RoomAccountRecord selectOneRecordByCondition(RoomAccountRecord roomAccountRecord);
	
	List<RoomAccountRecord> selectRecordsByRoomId(String roomId);

	List<OrderRecharge> selectOrderRechargeByPage(Page page);

	List<OrderRecharge> sourceOfFundExpert(OrderRecharge orderRecharge);
}
