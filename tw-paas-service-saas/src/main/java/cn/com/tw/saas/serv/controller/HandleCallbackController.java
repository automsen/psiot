package cn.com.tw.saas.serv.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.saas.serv.entity.command.CmdRecord;
import cn.com.tw.saas.serv.service.CallbackHandle;
import cn.com.tw.saas.serv.service.command.CmdRecordService;
import cn.com.tw.saas.serv.service.hb.TerminalHisDataService;
import cn.com.tw.saas.serv.service.read.ReadService;

import com.alibaba.fastjson.JSONObject;

/**
 * 接收paas回调接口
 * @author admin
 *
 */
@RestController
@RequestMapping("callback")
@Api(description = "接收paas回调接口")
public class HandleCallbackController {
	
	private Logger logger = LoggerFactory.getLogger(HandleCallbackController.class);

	@Autowired
	private CmdRecordService cmdService;
	
	@Autowired
	private ReadService readService;
	
	@Autowired
	private TerminalHisDataService serminalHisDataService;
	
	@PostMapping("")
	@ApiOperation(value="通用回调", notes="")
	public Response<?> callback(HttpServletRequest request){
		String jsonStr = null;
		try {
			jsonStr = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
			JSONObject resultJSON = JSONObject.parseObject(jsonStr);
			String businessNo = resultJSON.getString("businessNo");
			// 有业务编号交给业务处理
			if (!StringUtils.isEmpty(businessNo)){
				CmdRecord exe =  cmdService.selectById(businessNo);
				if(exe == null  || StringUtils.isEmpty(exe.getCmdTable() )){
					logger.error("业务号不匹配");
					return Response.ok();
				}
				CallbackHandle callback = (CallbackHandle) SpringContext.getBean(exe.getCmdTable());
				// 回调执行
				callback.handleCallback(exe, jsonStr);
			}
			// 无业务编号为主动上报数据
			else {
				readService.saveRead(jsonStr);
				serminalHisDataService.putRadHis(jsonStr);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return Response.ok();
		
	}
}
