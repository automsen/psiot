package cn.com.tw.paas.monit.service.org;

import java.util.List;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;

public interface CmdExeService  {

	Response<?> selectEquipGroup(String id);

	Response<?> selectEquipInsGroup(String id);
	
	List<CmdExe> selectByEntity(CmdExe query);
	
	List<CmdExe> selectByPage(Page page);
	
	CmdExe findCmdIns(String cmdId);

	public CmdExe selectById(String id);
	
	public int updateCmdInsStatus(CmdExe arg0);
	
	public void updateCmdExeSelective(CmdExe cmd);
	
}
