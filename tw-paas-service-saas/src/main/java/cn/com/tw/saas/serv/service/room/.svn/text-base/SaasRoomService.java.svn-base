package cn.com.tw.saas.serv.service.room;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.excel.RoomExcel;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.SaasRoom;

public interface SaasRoomService {

	String insertRoom(RoomExcel roomExcel, String regionNo,OrgUser user);

	List<SaasRoom> selectRoomMeterByregionId(String regionNo);

	List<SaasRoom> selectByEntity(SaasRoom saasRoom);

	int deleteById(String arg0);

	SaasRoom selectById(String arg0);

	List<SaasRoom> selectByPage(Page arg0);

	int update(SaasRoom arg0);

	int insertSelect(SaasRoom arg0);
}
