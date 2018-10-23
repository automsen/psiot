package cn.com.tw.paas.monit.service.inn.callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.InsExe;

import com.alibaba.fastjson.JSONObject;

/**
 * 处理指令下发基本数据保存，推送日志
 * 
 * @author liming 2018年3月7日17:57:34
 *
 */
public abstract class AbstractCmdCallback implements CmdCallback {

	@Autowired
	private MqHandler mqHandler;

	

	@Value("${mq.queue.meter.data}")
	private String queueDataName;



	/**
	 * 默认执行完毕，只写推送日志
	 */
	@Override
	public void cmdSuccess(CmdExe cmd) throws Exception {
		// // 写命令 需要返回命令的整体执行结果
		// if (writeCmd.equals(cmd.getCmdType())) {
		//
		//
		// }
	}

	private static String[] moneyInn = new String[] { "070101FF", "070102FF", "070103FF", "070104FF" };

	private boolean notInRetry(String key) {

		for (String notInKey : moneyInn) {
			if (notInKey.equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 指令执行成功，拼接执行完成后的json
	 */
	@Override
	public void insCallbackSuccess(CmdExe cmd, String result) throws Exception {
		try {
			InsExe currIns = cmd.getCurrIns();
			JSONObject obj = JSONObject.parseObject(result);
			obj.put("businessNo", cmd.getBusinessNo());
			JSONObject resultMap = obj.getJSONObject("data");
			String errorCode = resultMap.getString("errCode");
			// 控制码
//			String cotrolCode = resultMap.getString("cotrolCode");
			String dataMarker = resultMap.getString("dataMarker");
//			// 下联modbus终端的集中器专用上传
//			if (cotrolCode.toUpperCase().equals("8D")||cotrolCode.toUpperCase().equals("CD")){
//				// TODO
//			}
		
			if (resultMap != null && resultMap.getBooleanValue("success")) {
				// 收集返回值 ,更新数据到数据库
				mqHandler.send(queueDataName, obj.toJSONString());
				// saveRead(cmd, obj.getString("uniqueId"), resultMap);
			}
			// 失败了，是充值方面的datamarker
			else if (!resultMap.getBooleanValue("success") && notInRetry(dataMarker)
					&& ("02".equals(errorCode) || "20".equals(errorCode))) {
				// 收集充值参数
				mqHandler.send(queueDataName, obj.toJSONString());
				// 跳出重试
				currIns.setHandleTimes(currIns.getLimitHandleTimes());
				throw new Exception("meter recharge error!(meter recharge num error)");
				// saveRead(cmd, obj.getString("uniqueId"), resultMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void cmdError(CmdExe cmd, Exception e) throws Exception {
		
	}


	
}
