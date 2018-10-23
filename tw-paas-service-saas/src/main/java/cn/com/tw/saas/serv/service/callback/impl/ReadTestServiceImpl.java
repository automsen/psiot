package cn.com.tw.saas.serv.service.callback.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.com.tw.saas.serv.entity.command.CmdRecord;
import cn.com.tw.saas.serv.mapper.command.CmdRecordMapper;
import cn.com.tw.saas.serv.service.AbstractHandelService;
import cn.com.tw.saas.serv.service.callback.ReadTestService;
import cn.com.tw.saas.serv.service.hb.TerminalHisDataService;
import cn.com.tw.saas.serv.service.read.ReadService;

/**
 * 通讯测试读取止码
 */
@Service
public class ReadTestServiceImpl extends AbstractHandelService implements ReadTestService {
	
	private static Logger logger = LoggerFactory.getLogger(ReadCommonServiceImpl.class);

	@Autowired
	private ReadService readService;
	@Autowired
	private TerminalHisDataService terminalHisDataService;
	@Autowired
	private CmdRecordMapper cmdRecordMapper;
	
	public void handleCallback( CmdRecord commands,String result){
		// 保存http返回结果到本地
		callbackSuccess(commands, result);
	}
	
	@Transactional
	@Override
	public void callbackSuccess(CmdRecord commands, String result) {
		logger.debug(">>>>>>>>>>>>>> 执行成功 返回值：{}", result);
		JSONObject resultObj = JSONObject.parseObject(result);
		JSONObject resultData = resultObj.getJSONObject("data");
		
		if(resultData == null/* || !dataObj.getBooleanValue("success")*/){
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
	}

	@Override
	public void callbackError(CmdRecord commands, String result) {
		logger.debug(">>>>>>>>>>>>>> 执行失败 返回值：{}", result);
	}
}
