package cn.com.tw.saas.serv.service.room;

import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.excel.RoomExcel;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.room.RoomAllowanceInfo;

public interface RoomService {

	String insertRoom(RoomExcel roomExcel, String regionNo,String orgId);

	List<Room> selectRoomMeterByregionId(String regionNo, String string);

	List<Room> selectByEntity(Room saasRoom);

	int deleteById(String arg0);

	Room selectById(String arg0);

	List<Room> selectByPage(Page arg0);

	int update(Room arg0);

	int insertSelect(Room arg0);

	List<Room> selectShopRoomBalanceByPage(Page page);

	List<Room> selectDormRoomBalanceByPage(Page page);

	List<Room> selectWithCustByPage(Page page);

	Map<String, Object> selectAllInfo(CustRoomParam param);

	void submitAuditForUpdate(Map<String, Object> paramMap);

	void submitAuditForSign(Map<String, Object> paramMap);
	
	void submitAuditForRenewal(Map<String, Object> paramMap);

	List<RoomAllowanceInfo> selectByPageForAllowance(Page arg0);
	
	List<Room> shopbalanceExport(Room room);

	List<Room> selectRoomStatusByPage(Page page);

	void updateRoomStatus(Room newRoom);

	List<Room> selectRoomCustByRegionId(String regionId);

	List<Room> selectRoomMeterByRegionId(String regionId);
	
	/**
	 * 查询带三相电表的房间的房间号和房间Id
	 * @param regionId 楼栋Id 为空则查询所有
	 * @param orgId 机构Id 
	 * @return 房间列表
	 */
	List<Room> selectRoomWithThreePhaseByRegionId(Room room);

	void discardById(String id);

	void roomBalabceReset(List<Room> rooms);

	List<Room> dormRoomBalanceExpert(Room room);
}
