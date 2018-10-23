package cn.com.tw.saas.serv.service.command.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.ApiTemplate;
import cn.com.tw.saas.serv.entity.business.command.PageCmdResult;
import cn.com.tw.saas.serv.entity.command.CmdRecord;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.command.CmdRecordMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.AbstractHandelService;
import cn.com.tw.saas.serv.service.command.CmdRecordService;
@Service
public class CmdRecordServiceImpl extends AbstractHandelService implements CmdRecordService {
	
	private static Logger logger = LoggerFactory.getLogger(CmdRecordServiceImpl.class);

	@Autowired
	private CmdRecordMapper cmdRecordMapper;
	@Autowired
	private MeterMapper meterMapper;
	/**
	 * Url带meterAddr
	 */
	@Override
	public PageCmdResult generateCmd(ApiTemplate temp,String meterAddr,Map<String,String> requestMap) {
		logger.info("cmd request "+temp.getUrl()+" start!");
		// 参数拼接
		CmdRecord commandExe = new CmdRecord();
		Meter meter = meterMapper.selectByMeterAddr(meterAddr);
		// 将入口参数放入命令中
		commandExe.setCmdId(CommUtils.getUuid());
		commandExe.setOrgId(meter.getOrgId());
		commandExe.setCmdObjid(meterAddr);
		commandExe.setMeterAddr(meterAddr);
		commandExe.setRequestUrl(temp.getUrl()+"/"+meterAddr);
		commandExe.setCmdName(temp.getApiName());
		commandExe.setCreateTime(new Date(System.currentTimeMillis()));
		commandExe.setStatus(new Byte("10")); // 0创建；1成功；2失败；3超时；10正在处理
		commandExe.setCmdTable(temp.getCallbackClass());
		// 配置返回参数
		PageCmdResult result = new PageCmdResult();
		
		result.setCmdId(commandExe.getCmdId());
		result.setIsRepeat(false);
		result.setMeterAddr(commandExe.getMeterAddr());
		result.setCmdName(commandExe.getCmdName());
		try {
			saveCmd(commandExe,requestMap);
			exceResult(commandExe);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Url不带meterAddr
	 */
	@Override
	public PageCmdResult generateCmdUrlNoAddr(ApiTemplate temp,String meterAddr,Map<String,String> requestMap) {
		logger.info("cmd request "+temp.getUrl()+" start!");
		// 参数拼接
		CmdRecord commandExe = new CmdRecord();
		Meter meter = meterMapper.selectByMeterAddr(meterAddr);
		// 将入口参数放入命令中
		commandExe.setCmdId(CommUtils.getUuid());
		commandExe.setOrgId(meter.getOrgId());
		commandExe.setCmdObjid(meterAddr);
		commandExe.setMeterAddr(meterAddr);
		commandExe.setRequestUrl(temp.getUrl());
		commandExe.setCmdName(temp.getApiName());
		commandExe.setCreateTime(new Date(System.currentTimeMillis()));
		commandExe.setStatus(new Byte("10")); // 0创建；1成功；2失败；3超时；10正在处理
		commandExe.setCmdTable(temp.getCallbackClass());
		// 配置返回参数
		PageCmdResult result = new PageCmdResult();
		
		result.setCmdId(commandExe.getCmdId());
		result.setIsRepeat(false);
		result.setMeterAddr(commandExe.getMeterAddr());
		result.setCmdName(commandExe.getCmdName());
		try {
			// 指定cmdCode
			if (!StringUtils.isEmpty(temp.getCmdCode())){
				requestMap.put("cmdCode", temp.getCmdCode());
			}
			saveCmd(commandExe,requestMap);
			String result1 = exceResult(commandExe);
			Map<String, String> map = JsonUtils.jsonToPojo(result1, Map.class);
			if("000000".equals(map.get("statusCode"))){
				result.setSuccess(true);
				result.setErrorMsg("成功");
			}else{
				result.setSuccess(false);
				result.setErrorMsg(map.get("message"));
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public void retryCmd(String cmdId){
		CmdRecord cmd = cmdRecordMapper.selectByPrimaryKey(cmdId);
		cmd.setRequestUrl(cmd.getCmdCode());
		exceResult(cmd);
	}

	@Override
	public int deleteById(String paramString) {
		return cmdRecordMapper.deleteByPrimaryKey(paramString);
	}

	@Override
	public int insert(CmdRecord paramT) {
		return cmdRecordMapper.insert(paramT);
	}

	@Override
	public List<CmdRecord> selectByPage(Page paramPage) {
		return cmdRecordMapper.selectByPage(paramPage);
	}

	@Override
	public int updateSelect(CmdRecord paramT) {
		return cmdRecordMapper.updateByPrimaryKeySelective(paramT);
	}
	

	@Override
	public CmdRecord selectById(String arg0) {
		return cmdRecordMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public void callbackSuccess(CmdRecord commands, String result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callbackError(CmdRecord commands, String result) {
		// TODO Auto-generated method stub
		
	}

}
