package cn.com.tw.saas.serv.service.room;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.excel.RegionExcel;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.RoomTree;
import cn.com.tw.saas.serv.entity.room.SaasRegion;

public interface SaasRegionService{

	 List<RoomTree> loadRoomNodeTree(OrgUser saasOrgUser,String searchName);

	List<SaasRegion> selectSaasRegion(SaasRegion saasRegion);

	int deleteById(String roomId);

	int insertSelect(SaasRegion saasRegion);

	SaasRegion selectById(String arg0);

	List<SaasRegion> selectByPage(Page arg0);

	int update(SaasRegion arg0);

	void updateSelect(SaasRegion arg0);

	List<SaasRegion> selectByEntity(SaasRegion param);

	String insertRegion(RegionExcel regionTemp, OrgUser user);
}
