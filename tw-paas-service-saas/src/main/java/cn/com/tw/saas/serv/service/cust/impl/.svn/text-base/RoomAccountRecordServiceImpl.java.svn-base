package cn.com.tw.saas.serv.service.cust.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.cust.OrderRecharge;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.mapper.cust.OrderRechargeMapper;
import cn.com.tw.saas.serv.mapper.cust.RoomAccountRecordMapper;
import cn.com.tw.saas.serv.service.cust.RoomAccountRecordService;

@Service
public class RoomAccountRecordServiceImpl implements RoomAccountRecordService {

	@Autowired
	private RoomAccountRecordMapper roomAccountRecordMapper;
	@Autowired
	private OrderRechargeMapper orderRechargeMapper;
	
	@Deprecated
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Deprecated
	public int insert(RoomAccountRecord arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RoomAccountRecord selectById(String id) {
		return roomAccountRecordMapper.selectByPrimaryKey(id);
	}
	
	
	
	
	@Override
	public List<RoomAccountRecord> selectByPage(Page page) {
		// TODO Auto-generated method stub
		return roomAccountRecordMapper.selectByPage(page);
	}
	
	@Override
	public List<RoomAccountRecord> selectByPage2(Page page) {
		// TODO Auto-generated method stub
		return roomAccountRecordMapper.selectByPage2(page);
	}
	
	@Override
	public List<RoomAccountRecord> selectByEntity(RoomAccountRecord param) {
		// TODO Auto-generated method stub
		return roomAccountRecordMapper.selectByEntity(param);
	}
	
	@Deprecated
	public int update(RoomAccountRecord arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, Object> count(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return roomAccountRecordMapper.count(param);
	}

	@Override
	public List<RoomAccountRecord> selectAccountRecord(String userId) {
		return roomAccountRecordMapper.selectAccountRecord(userId);
	}
	
	

	@Override
	public List<RoomAccountRecord> selectNewAccountRecord(RoomAccountRecord roomAccountRecord) {
		return roomAccountRecordMapper.selectNewAccountRecord(roomAccountRecord);
	}
	
	
	/**
	 * 房间充值明细导出
	 * @param roomAccountRecord
	 * @return
	 */
	@Override
	public List<RoomAccountRecord> roomAccountRecordExpert(
			RoomAccountRecord roomAccountRecord) {

		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<RoomAccountRecord>  roomAccountRecord1 = new ArrayList<RoomAccountRecord>();
		roomAccountRecord1 = roomAccountRecordMapper.roomAccountRecordExpert(roomAccountRecord);
		for (RoomAccountRecord roomAccountRecord2 : roomAccountRecord1) {
			
			String dateString = formatter.format(roomAccountRecord2.getCreateTime());
			roomAccountRecord2.setCreateTimeStr(dateString);
			if("1710".equals(roomAccountRecord2.getRoomUse())){
				roomAccountRecord2.setRoomUse("商铺");
			}
			if("1720".equals(roomAccountRecord2.getRoomUse())){
				roomAccountRecord2.setRoomUse("宿舍");
			}
			if("1901".equals(roomAccountRecord2.getPayModeCode())){
				roomAccountRecord2.setPayModeCode("现金支付");
			} 
			if("1902".equals(roomAccountRecord2.getPayModeCode())){
				roomAccountRecord2.setPayModeCode("微信支付");
			}
			if("1903".equals(roomAccountRecord2.getPayModeCode())){
				roomAccountRecord2.setPayModeCode("支付宝支付");				
			}
			if("1904".equals(roomAccountRecord2.getPayModeCode())){
				roomAccountRecord2.setPayModeCode("银联支付");				
			}
			if("1905".equals(roomAccountRecord2.getPayModeCode())){
				roomAccountRecord2.setPayModeCode("POS转账");				
			}
			if("1906".equals(roomAccountRecord2.getPayModeCode())){
				roomAccountRecord2.setPayModeCode("余额转入");				
			}
		}
		return roomAccountRecord1;
	}
	
	/**
	 *  资金来源分析 列表
	 */
	@Override
	public List<RoomAccountRecord> roomOrderSourceAnalysis(Page page) {
		return roomAccountRecordMapper.selectBySourceAnalysis(page);
	}
	/**
	 *  资金来源分析 导出
	 */
	@Override
	public List<RoomAccountRecord>  roomOrderSourceExport(RoomAccountRecord roomAccountRecord) {
		List<RoomAccountRecord>  roomAccountRecord1=roomAccountRecordMapper.selectBySourceExport(roomAccountRecord);
		for (RoomAccountRecord roomAccountRecord2 : roomAccountRecord1) {
			if("1710".equals(roomAccountRecord2.getRoomUse())){
				roomAccountRecord2.setRoomUse("商铺");
			}else if("1720".equals(roomAccountRecord2.getRoomUse())){
				roomAccountRecord2.setRoomUse("宿舍");
			}
		}
		return roomAccountRecord1;
	}

	@Override
	public RoomAccountRecord selectOneRecordByCondition(RoomAccountRecord roomAccountRecord) {
		return roomAccountRecordMapper.selectOneRecordByCondition(roomAccountRecord);
	}
	/**
	 * 根据roomId查询当前用户的充值记录
	 * @param roomId
	 * @return
	 */
	@Override
	public List<RoomAccountRecord> selectRecordsByRoomId(String roomId) {
		return roomAccountRecordMapper.selectRecordsByRoomId(roomId);
	}

	@Override
	public List<OrderRecharge> selectOrderRechargeByPage(Page page) {
		List<OrderRecharge> orderRecharges = orderRechargeMapper.selectByPage(page);
		return orderRecharges;
	}

	@Override
	public List<OrderRecharge> sourceOfFundExpert(OrderRecharge orderRecharge) {
		List<OrderRecharge>  orderRecharges = orderRechargeMapper.sourceOfFundExpert(orderRecharge);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (OrderRecharge orderRecharge2 : orderRecharges) {
			orderRecharge2.setCreateTimeStr(formatter.format(orderRecharge2.getCreateTime()));
			if("1710".equals(orderRecharge2.getRoomUse())){
				orderRecharge2.setRoomUse("商铺");
			}
			if("1720".equals(orderRecharge2.getRoomUse())){
				orderRecharge2.setRoomUse("宿舍");
			}
			if("1901".equals(orderRecharge2.getPayModeCode())){
				orderRecharge2.setPayModeCode("现金支付");
			} 
			if("1902".equals(orderRecharge2.getPayModeCode())){
				orderRecharge2.setPayModeCode("微信支付");
			}
			if("1903".equals(orderRecharge2.getPayModeCode())){
				orderRecharge2.setPayModeCode("支付宝支付");				
			}
			if("1904".equals(orderRecharge2.getPayModeCode())){
				orderRecharge2.setPayModeCode("银联支付");				
			}
			if("1905".equals(orderRecharge2.getPayModeCode())){
				orderRecharge2.setPayModeCode("POS转账");				
			}
			if("1906".equals(orderRecharge2.getPayModeCode())){
				orderRecharge2.setPayModeCode("余额转入");
			}
		}
		return orderRecharges;
	}

}
