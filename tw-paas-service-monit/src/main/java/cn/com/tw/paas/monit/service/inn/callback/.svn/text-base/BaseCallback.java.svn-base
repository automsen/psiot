package cn.com.tw.paas.monit.service.inn.callback;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.InsExe;

/**
 *  基础回调
 *  自动保存下发指令返回的参数
 *  自动发布推送日志
 * @author liming
 * 2018年3月7日18:57:37
 */
@Service(value="baseCallback")
@Scope("prototype") // 多例 
public class BaseCallback extends AbstractCmdCallback{

	
	private Logger logger = LoggerFactory.getLogger(BaseCallback.class);
	
	@Override
	public void insCallbackSuccess(CmdExe cmd, String result)throws Exception {
		super.insCallbackSuccess(cmd, result);
	}
	
	@Override
	public void cmdSuccess(CmdExe cmd) throws Exception{
		super.cmdSuccess(cmd);
	}
	
	
	@Override
	public void insCallbackError(CmdExe cmd,Exception e)throws Exception {
		InsExe inn = cmd.getCurrIns();
		logger.info(String.format(">>>>>>>>>>>>>>>>>>>>>订单号%s,指令%s,执行失败！",
				cmd.getBusinessNo(),inn.getDataMarker()
				));
	}

	@Override
	public void cmdError(CmdExe cmd,Exception e) throws Exception{
		// 推送默认错误信息
		super.cmdError(cmd, e);
		logger.info(String.format(">>>>>>>>>>>>>>>>>>>>>命令%s执行失败！",
				cmd.getCmdName()
				));
	}
	
	
	
	
	
	

}
