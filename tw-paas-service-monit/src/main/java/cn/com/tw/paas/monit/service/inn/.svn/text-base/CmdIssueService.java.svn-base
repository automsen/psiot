package cn.com.tw.paas.monit.service.inn;

import java.util.List;

import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.service.inn.callback.CmdCallback;


/**
 * 命令下发服务
 * @author liming
 * 2018年3月7日16:50:39
 */
public interface CmdIssueService {

	void cmdIssue(CmdExe cmd ,CmdCallback callback) throws CmdHandleException; 
	
	void cmdIssueBatch(List<CmdExe> cmds, CmdCallback callback);
	
	void sendCmdToRedis(CmdExe cmd,String gwId);
}
