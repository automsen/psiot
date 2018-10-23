package cn.com.tw.paas.service.queue.thread;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.core.jms.MqHandler;
import cn.com.tw.common.core.jms.cons.MqCons;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.http.HttpPostReq;
import cn.com.tw.common.utils.tools.http.entity.HttpCode;
import cn.com.tw.common.utils.tools.http.entity.HttpRes;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.service.queue.eum.HisColEnum;
import cn.com.tw.paas.service.queue.eum.PushStatusEm;
import cn.com.tw.paas.service.queue.service.ReadDataService;

public class PushMsgThread implements Runnable{
	
	private static Logger logger = LoggerFactory.getLogger(PushMsgThread.class);
	
	private ReadDataService readService;
	
	private MqHandler mqHandler;
	
	private String jsonMsg;
	
	private String proxyHostName;
	
	private int proxyPort;
	
	public PushMsgThread(ReadDataService readService, MqHandler mqHandler, String jsonMsg) {
		this.setReadService(readService);
		this.mqHandler = mqHandler;
		this.setJsonMsg(jsonMsg);
	}
	
	public PushMsgThread setProxy(String proxyHostName, int proxyPort) {
		this.proxyHostName = proxyHostName;
		this.proxyPort = proxyPort;
		return this;
	}
	
	public String getProxyHostName() {
		return proxyHostName;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	@Override
	public void run() {
		
		logger.debug("PushMsgThread sending....msg = {}", jsonMsg);
		
		// 接收到的msg转为map格式
		@SuppressWarnings("unchecked")
		Map<String, Object> msgMap = JsonUtils.jsonToPojo(jsonMsg, Map.class);

		String reqDatas = JsonUtils.objectToJson(msgMap.get("pushData"));
		
		String pTimesStr = (String) msgMap.get(HisColEnum.pTs.name());
		
		int pTimes = pTimesStr == null ? 0 : Integer.parseInt(pTimesStr) + 1;
		
		String pUrl = (String) msgMap.get(HisColEnum.pUl.name());
		
		StringEntity entity = new StringEntity(reqDatas, "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");

		Map<String, Object> putInfo = new HashMap<String, Object>();
		putInfo.put("rowKey", msgMap.get("rowKey"));
		putInfo.put(HisColEnum.pTs.name(), String.valueOf(pTimes));
		putInfo.put(HisColEnum.pTm.name(), new Date().getTime());
		
		try {
			
			/**
			 * 如果推送次数大于等于5次 则不做请求
			 */
			if (pTimes > 1) {
				logger.warn("push time gt {} ! msg:{}", pTimes, "push times exceeds limit");
				return;
			}
			
			HttpRes result = new HttpPostReq(pUrl, null, entity).setProxy(proxyHostName, proxyPort).excuteReturnObj();

			logger.debug("push msg return result = {}", result == null ? "" : result.toString());
			
			/**
			 * 判断Http请求是否请求成功 失败则往MQ里发送数据
			 */
			if (!HttpCode.RES_SUCCESS.equals(result.getCode())) {
				
				putInfo.put(HisColEnum.pUs.name(), PushStatusEm.PUSH_HTTP_FAIL.getValue());
				readService.updatePushInfo(putInfo);
				
				msgMap.put(HisColEnum.pTs.name(), String.valueOf(pTimes));
				// 往Mq里发送数据
				mqHandler.send(MqCons.QUEUE_PUSH_RETRY, JsonUtils.objectToJson(msgMap));
				return;
			}

			String content = result.getData();

			Response<?> resp = JsonUtils.jsonToPojo(content, Response.class);

			/**
			 * 判断返回数据状态
			 */
			if (resp == null || !ResultCode.OPERATION_IS_SUCCESS.equals(resp.getStatusCode())) {
				putInfo.put(HisColEnum.pUs.name(), PushStatusEm.PUSH_FAIL.getValue());
				readService.updatePushInfo(putInfo);
				
				msgMap.put(HisColEnum.pTs.name(), String.valueOf(pTimes));

				//往Mq里发送数据
				mqHandler.send(MqCons.QUEUE_PUSH_RETRY, JsonUtils.objectToJson(msgMap));
				return;
			}

			//成功
			putInfo.put(HisColEnum.pUs.name(), PushStatusEm.PUSH_SUCCESS.getValue());
			readService.updatePushInfo(putInfo);

			logger.debug("<<------ PushMsgThread push success!");
		} catch (IOException e) {

			logger.error("push fail!, IOException e = {}",e);
			putInfo.put(HisColEnum.pUs.name(), PushStatusEm.PUSH_HTTP_FAIL.getValue());
			
			readService.updatePushInfo(putInfo);
			msgMap.put(HisColEnum.pTs.name(), String.valueOf(pTimes));
			// 往Mq里发送数据
			mqHandler.send(MqCons.QUEUE_PUSH_RETRY, JsonUtils.objectToJson(msgMap));

			e.printStackTrace();
			return;
		} catch (Exception e) {
			logger.error("push fail!, Exception e = {}",e);
			
			putInfo.put(HisColEnum.pUs.name(), PushStatusEm.PUSH_FAIL.getValue());
			readService.updatePushInfo(putInfo);
			msgMap.put(HisColEnum.pTs.name(), String.valueOf(pTimes));
			// 往Mq里发送数据
			mqHandler.send(MqCons.QUEUE_PUSH_RETRY, JsonUtils.objectToJson(msgMap));
			return;
		}
	}

	public ReadDataService getReadService() {
		return readService;
	}

	public void setReadService(ReadDataService readService) {
		this.readService = readService;
	}

	public String getJsonMsg() {
		return jsonMsg;
	}

	public void setJsonMsg(String jsonMsg) {
		this.jsonMsg = jsonMsg;
	}

}
