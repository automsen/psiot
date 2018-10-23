package cn.com.tw.saas.serv.service.callback.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;

import cn.com.tw.saas.serv.entity.command.CmdRecord;
import cn.com.tw.saas.serv.service.AbstractHandelService;
import cn.com.tw.saas.serv.service.callback.ReadCommonService;
import cn.com.tw.saas.serv.service.read.ReadService;

/**
 * 读取常规
 * 
 * @author liming 2017年10月12日15:04:47
 *
 */
@Service
public class ReadCommonServiceImpl extends AbstractHandelService implements ReadCommonService {

	private static Logger logger = LoggerFactory.getLogger(ReadCommonServiceImpl.class);

	@Autowired
	private ReadService readService;
	
	@Transactional
	@Override
	public void callbackSuccess(CmdRecord commands, String result) {
		logger.debug(">>>>>>>>>>>>>> 执行成功 返回值：{}", result);
		JSONObject resultObj = JSONObject.parseObject(result);
		JSONObject resultData = resultObj.getJSONObject("data");

		readService.saveRead(result);
		// 测试
		String totalActiveE = resultData.getString("totalActiveE");
		if (!StringUtils.isEmpty(totalActiveE)){
			System.out.println(totalActiveE);	
		}

	}

	@Override
	public void callbackError(CmdRecord commands, String result) {
		logger.debug(">>>>>>>>>>>>>> 执行失败 返回值：{}", result);
	}

}
