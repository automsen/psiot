package cn.com.tw.paas.monit.service.inn.callback;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.InsExe;

/**
 *  基础回调
 *  自动保存下发指令返回的参数
 *  自动发布推送日志
 * @author liming
 * 2018年3月7日18:57:37
 */
@SuppressWarnings("unchecked")
@Service(value="noPushCallback")
@Scope("prototype") // 多例 
public class NoPushCallback extends AbstractCmdCallback{

	
	private Logger logger = LoggerFactory.getLogger(NoPushCallback.class);
	
	
	@Override
	public void insCallbackSuccess(CmdExe cmd, String result)throws Exception {
		InsExe inn = cmd.getCurrIns();
		String returnValue = inn.getReturnValue();
		Map<String, String> map = JsonUtils.jsonToPojo(returnValue, Map.class); 
		String statusCode = map.get("statusCode");//返回码
		String data = JsonUtils.objectToJson(map.get("data"));
		cmd.setParam(statusCode);
		cmd.setReturnValue(data);
		super.insCallbackSuccess(cmd, result);
	}
	
	@Override
	public void cmdSuccess(CmdExe cmd) throws Exception{
		
		
	}
	
	
	@Override
	public void insCallbackError(CmdExe cmd, Exception e)throws Exception {
		InsExe inn = cmd.getCurrIns();
		String returnValue = inn.getReturnValue();
		if(!StringUtils.isEmpty(returnValue)){
			Map<String, String> map = JsonUtils.jsonToPojo(returnValue, Map.class); 
			String statusCode = map.get("statusCode");//错误码
			cmd.setParam(statusCode);
		}
		logger.info(String.format(">>>>>>>>>>>>>>>>>>>>>订单号%s,指令%s,执行失败！",
				cmd.getBusinessNo(),inn.getDataMarker()
				));
	}

	@Override
	public void cmdError(CmdExe cmd, Exception e) throws Exception{
		InsExe ins = cmd.getCurrIns();
		String returnValue = ins.getReturnValue();
		if(!StringUtils.isEmpty(returnValue)){
			Map<String, String> map = JsonUtils.jsonToPojo(returnValue, Map.class); 
			String statusCode = map.get("statusCode");//错误码
			cmd.setParam(statusCode);
		}
		logger.info(String.format(">>>>>>>>>>>>>>>>>>>>>命令%s执行失败！",
				cmd.getCmdName()
				));
	}


}
