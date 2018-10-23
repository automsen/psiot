package cn.com.tw.saas.serv.service;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import cn.com.tw.common.utils.tools.http.HttpPostReq;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.saas.serv.common.utils.SignatureUtil;
import cn.com.tw.saas.serv.common.utils.cons.SysCommons;
import cn.com.tw.saas.serv.entity.command.CmdRecord;
import cn.com.tw.saas.serv.mapper.command.CmdRecordMapper;
import cn.com.tw.saas.serv.service.hb.TerminalHisDataService;
import cn.com.tw.saas.serv.service.read.ReadService;

/**
 *  paas 平台 http 交互
 * @author liming
 * 2017年9月4日18:31:41
 *
 */
public abstract class AbstractHandelService implements CallbackHandle{
	
	private Logger logger = LoggerFactory.getLogger(AbstractHandelService.class);
	
	private String URL = Env.getVal("paas.url");
	
	@Autowired
	private CmdRecordMapper cmdRecordMapper;
	@Autowired
	private TerminalHisDataService terminalHisDataService;
	
	@Autowired
	private ReadService readService;
	
	private String appKey = Env.getVal("paas.appKey");
	
	private String secret = Env.getVal("paas.secret");
	
	// 总重试次数
	private final int total_req_num  =3;
	
	@SuppressWarnings("unchecked")
	public String exceResult(CmdRecord commands) {
		try {
			String params = commands.getParam();
			Map<String,String> requestParams = new HashMap<String, String>();
			requestParams = JSONObject.parseObject(params,Map.class);
			signPostRequest(requestParams);
			
			//当前执行指令
			logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<http start request{} currTime:{}",commands.getRequestUrl(),System.currentTimeMillis());
			String requestStr = JSONObject.toJSONString(requestParams);
			commands.setParam(requestStr);
			StringEntity entity = new StringEntity(requestStr,"utf-8");//解决中文乱码问题    
	        entity.setContentEncoding("UTF-8");    
	        entity.setContentType("application/json");
	        String uri = StringUtils.isEmpty(commands.getRequestUrl())?commands.getCmdCode():commands.getRequestUrl();
	        String result = new HttpPostReq(URL+"/"+uri, null, entity).excuteReturnStr();
	        // url连接失败
	        if(StringUtils.isEmpty(result)){
	    		//查询数据失败
	    		handleError(commands, null, null);
	    		logger.debug(">>>>>>>>>>>>>>>>>>>http request error  currTime:{}",System.currentTimeMillis());
	    		return null;
	        }
	        handleResponse(commands,result);
	        logger.debug(">>>>>>>>>>>>>>>>>>>http response success result:{}",result);
	        return result;
		} catch (SocketTimeoutException db){
			logger.debug(">>>>>>>>>>>>>>>>>>>http response timeout  currTime:{}",System.currentTimeMillis());
			// 连接超时，不再重试
			commands.setReqNum(total_req_num);
			handleRuntimeException(commands, db);
		} catch (IOException e){
			logger.debug(">>>>>>>>>>>>>>>>>>>http response IOException  currTime:{}",System.currentTimeMillis());
			// 连接超时，不再重试
			commands.setReqNum(total_req_num);
			handleException(commands,e);
		}catch (Exception  e) {
			logger.debug(">>>>>>>>>>>>>>>>>>>http response error  currTime:{}",System.currentTimeMillis());
			handleException(commands, e);
		}
		return null;
	}

	
	public boolean isRetryOver(CmdRecord commands){
		//起步为0 小于等于即可
		if(commands.getReqNum()>=total_req_num){
			//执行完毕，更新状态
			handleError(commands, null, null);
			return true;
		}
		commands.setReqNum(commands.getReqNum() + 1);
		return false;
	}
	
	
	/**
	 * http有返回值时
	 * @param commands
	 * @param result
	 */
	public void handleResponse( CmdRecord commands,String result){
		
		JSONObject jsonObj = JSONObject.parseObject(result);
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>http response :{}",result);
		if(!"000000".equals(jsonObj.getString("statusCode"))){
			handleError(commands,result,null);
			return;
		}
		handleSuccess(commands,result);
	}
	
	/**
	 * 如果发生未知错误，或连接失败 操作
	 */
	public void handleException(CmdRecord commands, Exception e) {
		//重试后继续
		if(isRetryOver(commands)){
			//查询数据失败
			handleError(commands, null, e);
			return;
		}
		// 重试
		exceResult(commands);
	}
	
	/**
	 * 读数超时后执行
	 */
	public void handleRuntimeException(CmdRecord commands,
		 SocketTimeoutException	run) {
		//判断是否还能重试
		if(isRetryOver(commands)){
			//查询数据失败
			handleError(commands, null, run);
			return;
		}
		// 重试
		exceResult(commands);
	}
	
	
	/**
	 * 请求加密执行
	 * @param requestMap
	 * @return
	 * @throws Exception
	 */
	private String signPostRequest(Map<String,String> requestMap) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		if(requestMap == null){
			throw new Exception("requestObj can not be null!");
		}
		requestMap.put("appKey", appKey);
		requestMap.put("timestamp", sdf.format(new Date()));
		String sign = SignatureUtil.generateSign(requestMap, secret);
		requestMap.put("sign", sign);
		return JsonUtils.objectToJson(requestMap);
	}
	
	/**
	 * 保存未请求指令
	 * @param cmd
	 * @param requestParams
	 * @throws Exception
	 */
	public void saveCmd(CmdRecord cmd,Map<String,String> requestParams)throws Exception{
		requestParams.put("businessNo", cmd.getCmdId());
		String params = JsonUtils.objectToJson(requestParams);
		cmd.setCmdCode(cmd.getRequestUrl());
		cmd.setParam(params);
		cmdRecordMapper.insert(cmd);
	}
	
	/**
	 *  http 请求成功后执行
	 * @param commands
	 * @param result
	 */
	public void handleSuccess( CmdRecord commands,String result){
		// 保存http返回结果到本地
	}
	/**
	 *  http请求失败后执行
	 * @param commands
	 * @param result
	 * @param e
	 */
	public void handleError(CmdRecord commands,String result,Exception e){
		if(!StringUtils.isEmpty(result)){
			commands.setReturnValue(result);
		}
		// 更新命令执行情况
		if(e != null && ( e instanceof RuntimeException || e instanceof SocketTimeoutException)){
			commands.setStatus(new Byte("3"));  //执行超时
		}else{
			commands.setStatus(new Byte("2"));  //执行失败
		}
		cmdRecordMapper.updateByPrimaryKeySelective(commands);
	}
	
	public void handleCallback(CmdRecord commands,String result){
		// 返回值不能为空
		if(StringUtils.isEmpty(result)){
			handleError(commands,result,null);
			callbackError( commands, result);
			return;
		}
		// 重复的回调  直接返回
		if(!SysCommons.CmdStatus.Ready.equals(commands.getStatus())&&!SysCommons.CmdStatus.Handle.equals(commands.getStatus())){
			return;
		}
		JSONObject resultObj = JSONObject.parseObject(result);
		String dataType = resultObj.getString("dataType");
		// 指令下发失败。
		if(!StringUtils.isEmpty(dataType)&& "1606".equals(dataType)) {
			handleError(commands,result,null);
			callbackError( commands, result);
			return ;
		}
		JSONObject dataObj = resultObj.getJSONObject("data");
		// 返回的值块为空或者 返回的错误的识别码  
		if(dataObj == null/* || !dataObj.getBooleanValue("success")*/){
			handleError(commands,result,null);
			callbackError( commands, result);
			return;
		}
		
		// 更新本地数据
		try {
			terminalHisDataService.putRadHis(result);
			readService.saveRead(result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		commands.setReturnValue(result);
		commands.setStatus(new Byte("1"));
		cmdRecordMapper.updateByPrimaryKeySelective(commands);
		// 执行正确的回调
		try {
			callbackSuccess(commands, result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public abstract void callbackSuccess( CmdRecord commands,String result);
	
	public abstract void callbackError(CmdRecord commands,String result);


}
