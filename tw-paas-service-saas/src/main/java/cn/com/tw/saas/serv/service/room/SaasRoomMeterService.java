package cn.com.tw.saas.serv.service.room;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.business.archives.RoomMeterChangeParam;
import cn.com.tw.saas.serv.entity.excel.RoomMeterExcel;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.SaasRoomMeter;

public interface SaasRoomMeterService {
	/**
	 * app绑定
	 * @param param
	 * @return
	 */
	int appBind(SaasRoomMeter param, OrgUser user);
	
	/**
	 * excel导入绑定
	 * @param roomMeterExcel
	 * @param regionNo
	 * @return
	 */
	String excelBind(RoomMeterExcel roomMeterExcel, String regionId,String orgId);

	int deleteById(String arg0);
	
	/**
	 * 取消关联
	 * @param param
	 * @return
	 */
	int cancel(RoomMeterChangeParam param);
	
	/**
	 * 更换关联
	 * @param param
	 * @return
	 */
	int replace(RoomMeterChangeParam param);

	SaasRoomMeter selectById(String arg0);

	List<SaasRoomMeter> selectByPage(Page arg0);

	int updateSelect(SaasRoomMeter arg0);

}
