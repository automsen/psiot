package cn.com.tw.paas.monit.service.inn.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.InsExe;
import cn.com.tw.paas.monit.mapper.org.OrgApplicationMapper;
import cn.com.tw.paas.monit.mapper.org.PushLogMapper;

/**
 * 生产调试回调
 * 
 * @author admin
 *
 */
@Service(value = "testCallback")
@Scope("prototype") // 多例
public class TestCallback extends AbstractCmdCallback {

	//private String callbackURL = Env.getVal("test.callback");

	@Autowired
	private PushLogMapper pushLogMapper;
	@Autowired
	private MqHandler mqHandler;
	@Autowired
	private OrgApplicationMapper applicationMapper;
	
	private Logger logger = LoggerFactory.getLogger(TestCallback.class);


	/**
	 * 默认执行完毕，只写推送日志
	 */
	@Override
	public void cmdSuccess(CmdExe cmd) throws Exception {
		//saveTestPushLog(cmd, null);
	}
	
	@Override
	public void insCallbackSuccess(CmdExe cmd, String result) throws Exception {
		super.insCallbackSuccess(cmd, result);
	}
	
	@Override
	public void cmdError(CmdExe cmd, Exception e) throws Exception {
		//saveTestPushLog(cmd, e);
		super.cmdError(cmd,  e);
	}

	@Override
	public void insCallbackError(CmdExe cmd, Exception e) throws Exception {
		InsExe inn = cmd.getCurrIns();
		logger.info(String.format(">>>>>>>>>>>>>>>>>>>>>订单号%s,指令%s,执行失败！", cmd.getBusinessNo(),inn.getDataMarker()));
	}

	/*private void saveTestPushLog(CmdExe cmd, Exception e) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		TerminalEquip equipment = cmd.getTerminalEquip();
		map.put("appKey", cmd.getApplication().getAppKey());
		map.put("businessNo", cmd.getApplication().getBussinesNo());
		map.put("equipNumber", cmd.getConnAddr());
		if (!StringUtils.isEmpty(equipment.getNetEquipNumber())) {
			map.put("netNumber", equipment.getNetEquipNumber());
		}
		map.put("lastSaveTime", System.currentTimeMillis() + "");

		// 异常返回
		if (e != null) {
			dataMap.put("success", false);
			if (e instanceof SocketTimeoutException) {
				dataMap.put("errMsg", "middleware socket time out!");
			} else if (e instanceof IOException) {
				dataMap.put("errMsg", "middleware connect fault!");
			}
		}
		// 实际执行状态
		String result = cmd.getCurrIns().getReturnValue();
		// 中间件返回
		if (!StringUtils.isEmpty(result)) {
			JSONObject obj = JSONObject.parseObject(result);
			JSONObject resultMap = obj.getJSONObject("data");
			// 和中间建的通讯正常
			if (resultMap != null) {

				// 有返回值的情况
				if (cmd.getPushData() != null && cmd.getPushData().size() > 0) {
					dataMap = cmd.getPushData();
				}
				if (!resultMap.getBooleanValue("success")) {
					dataMap.put("errMsg", resultMap.getString("errCode"));
				}

				dataMap.put("success", resultMap.getBoolean("success"));
			} else {
				dataMap.put("success", false);
				dataMap.put("errMsg", obj.getString("message"));
			}
			// 依然没有插入返回值
		} else if (dataMap.size() == 0) {
			dataMap.put("success", false);
			dataMap.put("errMsg", "unknow error");
		}
		map.put("data", dataMap);
		map.put("commType", equipment.getNetTypeCode());
		map.put("dataType", "233");
		PushLog log = new PushLog();
		log.setLogId(CommUtils.getUuid());
		OrgApplication application = cmd.getApplication();
		OrgApplicationExpand app = applicationMapper.selectByPrimaryKey(application.getAppId());
		log.setOrgId(application.getOrgId());
		log.setAppId(application.getAppId());
		log.setPushUrl(callbackURL);
		log.setBusinessNo(application.getBussinesNo());
		log.setAppBusinessNo(application.getAppBussinesNo());
		log.setBusinessType("233");
		log.setPushData(JsonUtils.objectToJson(map));
		log.setTryTimes(0);
		log.setAppName(app.getAppName());
		log.setOrgName(app.getOrgName());
		log.setStatusCode(PushStatusEm.PUSH_CREATE.getValue());
		pushLogMapper.insert(log);
		String logJson = JsonUtils.objectToJson(log);
		try {
			// 往Mq里发送数据
			mqHandler.sendDelay(MqCons.QUEUE_PUSH_RETRY, logJson, 1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}*/

}
