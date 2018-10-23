package cn.com.tw.paas.monit.controller.api;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.paas.monit.common.utils.StreamUtils;
import cn.com.tw.paas.monit.common.utils.cons.CacheNameCons;
import cn.com.tw.paas.monit.entity.db.base.Cmd;
import cn.com.tw.paas.monit.entity.db.base.CmdIns;
import cn.com.tw.paas.monit.entity.db.base.Ins;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.InsExe;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.service.base.InsService;
import cn.com.tw.paas.monit.service.org.TerminalEquipService;
import cn.com.tw.paas.monit.service.sys.CmdInsCacheService;


/**
 *  通用的数据验证
 *  @author liming 
 *  2018年3月7日10:38:29
 *
 */
public class IssueValidateController {
	
	private Logger logger = LoggerFactory.getLogger(IssueValidateController.class);

	@Autowired
	private TerminalEquipService terminalEquipService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private InsService insService;
	@Autowired
	private CmdInsCacheService cmdInsCacheService;
	
	private static final int cmdLevel = 20;
	// 整数校验
	public final static String Integer_Patten = "^[0-9]*$";
	// 数字校验
	public final static String Numeric_Patten = "([1-9]\\d*(\\.\\d*[1-9])?)|(0\\.\\d*[1-9])";
	
	@SuppressWarnings("unchecked")
	public Map<String,String> getRequestParams(HttpServletRequest request){
		
		String jsonStr = null;
		try {
			jsonStr = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return JsonUtils.jsonToPojo(jsonStr, Map.class);
	}
	
	/**
	 * 获取国际化参数
	 * @param request  
	 * @param key	  国际化键值
	 * @param params  带变量型的字符串     参考stringformat的%s
	 * @return
	 */
	public String getMessage(HttpServletRequest request,String key,String...params){
		return messageSource.getMessage(key, params,  RequestContextUtils.getLocale(request));
	}
	
	/**
	 *  验证仪表，appid 是否合法 , 并返回相关仪表信息
	 *  @param meterAddr 仪表地址
	 *  @param appKey   应用ID
	 *  @return OrgEquipment 仪表信息
	 *  @exception RequestParamValidException
	 */
	@Deprecated
	public TerminalEquip valiedateEquipment(String equipNumber, String appKey) throws RequestParamValidException{
		if(StringUtils.isEmpty(equipNumber) || StringUtils.isEmpty(appKey)){
			throw new RequestParamValidException("equipNumber or appKey can not be empty!");
		}
		List<TerminalEquip> tempList = null;
	
		// 查询仪表相关信息
		Map<String,String> param = new HashMap<String,String>();
		param.put("appKey", appKey);
		param.put("equipNumber", equipNumber);
		try {
			tempList = terminalEquipService.selectByAppkey(param);
		} catch (Exception e) {
			throw new RequestParamValidException("unknow error!");
		}
		if(tempList == null ){
			throw new RequestParamValidException("equipNumber can not find!");
		}
		if(tempList.size() > 1 || tempList.size() == 0 ){
			throw new RequestParamValidException("Application Equipment reference error!");
		}
		return tempList.get(0); 
	}
	
	/**
	 *  验证仪表，appid 是否合法 , 并返回相关仪表信息
	 *  @param meterAddr 仪表地址
	 *  @param appKey   应用ID
	 *  @return OrgEquipment 仪表信息
	 *  @exception RequestParamValidException
	 */
	public TerminalEquip valiedateEquipAndOrgId(String equipNumber, String orgId) throws RequestParamValidException{
		if(StringUtils.isEmpty(equipNumber) || StringUtils.isEmpty(orgId)){
			throw new RequestParamValidException("equipNumber or appKey can not be empty!");
		}
		List<TerminalEquip> tempList = null;
	
		// 查询仪表相关信息
		Map<String,String> param = new HashMap<String,String>();
		param.put("orgId", orgId);
		param.put("equipNumber", equipNumber);
		try {
			tempList = terminalEquipService.selectByOrgIdAndMeterAddr(param);
		} catch (Exception e) {
			logger.error("selectByOrgIdAndMeterAddr {}", e);
			throw new BusinessException(ResultCode.UNKNOW_ERROR, "equipNumber can not find!");
		}
		if(tempList == null ){
			throw new RequestParamValidException("equipNumber can not find!");
		}
		if(tempList.size() > 1 || tempList.size() == 0 ){
			throw new BusinessException(ResultCode.UNKNOW_ERROR, "system data exception!");
		}
		return tempList.get(0); 
	}

	/**
	 * 获取仪表命令相关指令
	 * @param equipType
	 * @param protocolType
	 * @param modelId
	 * @param cmdcode
	 * @return
	 * @throws RequestParamValidException
	 */
	public List<Ins> getCmdIns(String equipType,String protocolType,String modelId ,String cmdcode)throws RequestParamValidException{
		CmdIns param = new CmdIns();
		param.setCmdCode(cmdcode);
		param.setEquipType(equipType);
		if(!StringUtils.isEmpty(protocolType)){
			param.setProtocolType(protocolType);
		}
		// 参数是否包含型号id
		boolean haveModelId = false;
		if(!StringUtils.isEmpty(modelId)){
			param.setModelId(modelId);
			haveModelId = true;
		}
		List<Ins> ins = null;
		try {
			// 初次查询
			ins = insService.selectInsByCmdIns(param);
			// 参数包含型号id时，初次查询未查到对应指令，则设定modelId = common再次查询
			if (haveModelId&&ins == null || ins.size() == 0){
				param.setModelId("common");
				ins = insService.selectInsByCmdIns(param);
			}
		} catch (Exception e) {
			throw new RequestParamValidException("unknow error!");
		}
		if(ins == null || ins.size() == 0){
			throw new RequestParamValidException("ins not find in database!");
		}
		return ins;
	}
	
	/**
	 * 准备cmd对象
	 * @param cmdCode
	 * @param connAddr
	 * @param appId
	 * @param bussNo
	 * @param orgId
	 * @param meterType
	 * @param gwType
	 * @param gwId
	 * @param commPwd
	 * @return
	 */
	public CmdExe getCmdExe(String cmdCode,String connAddr, String appId, String bussNo, String orgId, String meterType, String gwType, String gwId, String commPwd) {
		CmdExe cmdExe = new CmdExe();
		Object cmdObj = getCmdCache(CacheNameCons.cmd_dataItem,cmdCode);
		Cmd tempCmd = cmdObj == null ? null : (Cmd) cmdObj;
		cmdExe.setCmdType(tempCmd.getCmdType()); // 命令的读写方式
		cmdExe.setId(CommUtils.getUuid());
		cmdExe.setAppId(appId);
		cmdExe.setBusinessNo(bussNo);
		cmdExe.setCmdCode(cmdCode);
		cmdExe.setCmdLevel(cmdLevel);
		cmdExe.setCmdName(tempCmd != null ?tempCmd.getCmdName():"");
		cmdExe.setConnAddr(connAddr);
		cmdExe.setCreateTime(new Date());
		cmdExe.setOrgId(orgId);
		cmdExe.setStatus(new Byte("10"));  // 准备下发
		cmdExe.setCommPwd(commPwd);
		cmdExe.setLimitHandleTimes(tempCmd.getCmdRetryNum());
		Map<String,Object> requestParams = new HashMap<String, Object>();
		requestParams.put("meterType", meterType);
		requestParams.put("gatewayType", gwType);
		requestParams.put("cmdLvl", cmdLevel);
		requestParams.put("meterAddr", connAddr);
		requestParams.put("gwId", gwId);
		cmdExe.setRequestParams(requestParams);
		return cmdExe;
	}
	
	/*
	 * 命令组装指令
	 */
	public void loadCmdIns(CmdExe cmd,String protocolType,List<Ins> ins, String...params) throws RequestParamValidException{
		Ins tempIns = null;
		List<InsExe> insExes = new ArrayList<InsExe>();
		InsExe tempInExe = null;
		// 如果参数不为空，那么长度必须和指令集一致
		if(params != null&&params.length != 0 && params.length != ins.size()){
			throw new RequestParamValidException(ResultCode.PARAM_VALID_ERROR);
		}
		InsExe upInExe = null;
		for (int i = 0,len = ins.size(); i < len; i++) {
			tempIns = ins.get(i);
			// 每个命令拼接自己的参数， 如果没有可以不填
			if(params != null&&params.length != 0 ){
				tempInExe = getInsExe(cmd,protocolType,tempIns,params[i]);
			}else{
				tempInExe = getInsExe(cmd,protocolType,tempIns);
			}
			tempInExe.setLimitHandleTimes(tempIns.getRetryNum());
			tempInExe.setStatus(new Byte("0"));
			if(i == 0){
				tempInExe.setStatus(new Byte("10"));
				cmd.setCurrIns(tempInExe);
			}
			insExes.add( tempInExe);
			if(upInExe == null){
				upInExe = tempInExe;
			}else{
				upInExe.setNextIns(tempInExe);
				upInExe = tempInExe;
			}
		}
		cmd.setIns(insExes);
		
	}
	
	/**
	 * 指令组装参数
	 * @param cmdexe
	 * @param protocolType
	 * @param ins
	 * @param params
	 * @return
	 */
	private InsExe getInsExe(CmdExe cmdexe,String protocolType,Ins ins,String...params){
		InsExe insExe = new InsExe();
		insExe.setId(CommUtils.getUuid());
		insExe.setAppId(cmdexe.getAppId());
		insExe.setBusinessNo(cmdexe.getBusinessNo());
		insExe.setCmdExeId(cmdexe.getId());
		insExe.setConnAddr(cmdexe.getConnAddr());
		insExe.setControlCode(ins.getControlCode());
		insExe.setCreateTime(new Date());
		insExe.setDataMarker(ins.getDataMarker());
		insExe.setGroupSort(ins.getItemIndex());
		insExe.setInsName(ins.getInsName());
		insExe.setOrgId(cmdexe.getOrgId());
		
		Map<String,Object> requestParams = cmdexe.getRequestParams();
		
		StringBuffer requestContent = new StringBuffer();
		// 645协议
		if (protocolType.equals("102")||protocolType.equals("103")){
			requestContent.append(cmdexe.getConnAddr()).append(",");
		}
		// modbus协议，截取三位设备编号
		else if (protocolType.equals("104")){
			requestContent.append(cmdexe.getConnAddr().substring(8, 11)).append(",");
		}
		// 歌华协议，不拼接设备编号
		else if (protocolType.equals("105")){
//			requestContent.append(cmdexe.getConnAddr()).append(",");
		}
		requestContent.append(ins.getControlCode()).append(",");
		if(!StringUtils.isEmpty(ins.getDataMarker())){
			requestContent.append(ins.getDataMarker()).append(",");
		}
		// 等于1 的加入密码
		if(ins.getNeedPassword()!= null && ins.getNeedPassword().equals(new Byte("1")) ){
			requestContent.append(cmdexe.getCommPwd()).append(",");
		}
		// 判断额外参数
		if(!StringUtils.isEmpty(ins.getOtherMarker())){
			requestContent.append(ins.getOtherMarker()).append(",");
		}
		if(!StringUtils.isEmpty(params)){
			for (String tempStr : params) {
				requestContent.append(tempStr).append(",");
			}
		}
		requestParams.put("content", requestContent.substring(0,requestContent.length()-1));
		insExe.setParam(JsonUtils.objectToJson(requestParams));
		return insExe;
	}
	
	/**
	 * 网关命令拼接
	 * @param cmdexe
	 * @param ins
	 * @param params
	 * @return
	 */
	public InsExe getNetInsExe(CmdExe cmdexe,Ins ins,String...params){
		InsExe insExe = new InsExe();
		insExe.setId(CommUtils.getUuid());
		insExe.setAppId(cmdexe.getAppId());
		insExe.setBusinessNo(cmdexe.getBusinessNo());
		insExe.setCmdExeId(cmdexe.getId());
		insExe.setConnAddr(cmdexe.getConnAddr());
		insExe.setControlCode(ins.getControlCode());
		insExe.setCreateTime(new Date());
		insExe.setDataMarker(ins.getDataMarker());
		insExe.setGroupSort(ins.getItemIndex());
		insExe.setInsName(ins.getInsName());
		insExe.setOrgId(cmdexe.getOrgId());
		
		Map<String,Object> requestParams = cmdexe.getRequestParams();
		
		StringBuffer requestContent = new StringBuffer();
		requestContent.append(cmdexe.getConnAddr()).append(",");
		requestContent.append(ins.getControlCode()).append(",");
		
		if(!StringUtils.isEmpty(ins.getDataMarker())){
			requestContent.append(ins.getDataMarker()).append(",");
		}
		// 等于1 的加入密码
		if(ins.getNeedPassword()!= null && ins.getNeedPassword().equals(new Byte("1")) ){
			requestContent.append(cmdexe.getCommPwd()).append(",");
		}
		// 判断额外参数
		if(!StringUtils.isEmpty(ins.getOtherMarker())){
			requestContent.append(ins.getOtherMarker()).append(",");
		}
		/**
		 * 读     否 写
		 */
		if("11".equals(ins.getControlCode())){
			if(!StringUtils.isEmpty(params)){
				for (String tempStr : params) {
					requestContent.append(tempStr).append(",");
				}
			}
			requestParams.put("content", requestContent.substring(0,requestContent.length()-1));
		}else{
			if(!StringUtils.isEmpty(params)){
				/**
				 * 判断配置终端 指令 或者 采集频率
				 */
				//配置下联终端
				if("04700103".equals(ins.getDataMarker())){
					for (String tempStr : params) {
						String[] equipNumbers = tempStr.split(",");
						requestContent.append(equipNumbers.length);
						for (String equip : equipNumbers) {
							requestContent.append(",").append(equip).append(",1");
						}
					}
				}
				//配置采集指令
				if("04700201".equals(ins.getDataMarker())){
					for (String tempStr : params) {
						String[] datas = tempStr.split(",");
						requestContent.append(datas.length);
						for (String data : datas) {
							requestContent.append(",").append(data);
						}
					}
				}
				//配置采集频率
				if("04700102".equals(ins.getDataMarker())){
					for (String tempStr : params) {
						requestContent.append(tempStr);
					}
				}
			}
			requestParams.put("content", requestContent.substring(0,requestContent.length()));
		}
		insExe.setParam(JsonUtils.objectToJson(requestParams));
		return insExe;
	}
	
	/**
	 * 网关命令拼接
	 * @param cmdexe
	 * @param ins
	 * @param params
	 * @return
	 */
	public void loadNetCmdIns(CmdExe cmd,List<Ins> ins, String...params) throws RequestParamValidException{
		Ins tempIns = null;
		List<InsExe> insExes = new ArrayList<InsExe>();
		InsExe tempInExe = null;
		// 如果参数不为空，那么长度必须和指令集一致
		if(params != null&&params.length != 0 && params.length != ins.size()){
			throw new RequestParamValidException(ResultCode.PARAM_VALID_ERROR);
		}
		InsExe upInExe = null;
		for (int i = 0,len = ins.size(); i < len; i++) {
			tempIns = ins.get(i);
			// 每个命令拼接自己的参数， 如果没有可以不填
			if(params != null&&params.length != 0 ){
				tempInExe = getNetInsExe(cmd,tempIns,params[i]);
			}else{
				tempInExe = getNetInsExe(cmd,tempIns);
			}
			tempInExe.setLimitHandleTimes(tempIns.getRetryNum());
			tempInExe.setStatus(new Byte("0"));
			if(i == 0){
				tempInExe.setStatus(new Byte("10"));
				cmd.setCurrIns(tempInExe);
			}
			insExes.add( tempInExe);
			if(upInExe == null){
				upInExe = tempInExe;
			}else{
				upInExe.setNextIns(tempInExe);
				upInExe = tempInExe;
			}
		}
		cmd.setIns(insExes);
	}

	public Object getCmdCache(String cacheKey,String key){
		return  cmdInsCacheService.selectCmdById(key);
	}
	
	public static boolean isInteger(String str){
	    Pattern pattern = Pattern.compile(Integer_Patten);
	    return pattern.matcher(str).matches();   
	}
	
	public static boolean isNumeric(String str){
	    Pattern pattern = Pattern.compile(Numeric_Patten);
	    return pattern.matcher(str).matches();   
	}
}
