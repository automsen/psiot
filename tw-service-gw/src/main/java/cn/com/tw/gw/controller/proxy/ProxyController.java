package cn.com.tw.gw.controller.proxy;

import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.http.HttpGetReq;
import cn.com.tw.common.utils.tools.http.HttpPostReq;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;

@RestController
@RequestMapping("proxy")
public class ProxyController {
	
	public static final Logger log = LoggerFactory.getLogger(ProxyController.class);
	
	/**
	 * 
	 * @param reqUrl
	 * @return
	 */
	@GetMapping
	public Response<?> getReq(@RequestParam String redirectUrl) {
		
		log.debug("------> get proxy sending, redirectUrl = {}", redirectUrl);
		
		try {
			String retContent = new HttpGetReq(redirectUrl).excuteReturnStr();
			log.debug("<------- get proxy send complete, retContent = {}", retContent);
			return Response.ok(retContent);
		} catch (Exception e) {
			log.error("get proxy send fail!!， e = {}", e);
			return Response.retn(ResultCode.REMOTE_CALL_ERROR, "get proxy send fail!!");
		}
	}
	
	@PostMapping
	public Response<?> postReq(@RequestParam String redirectUrl, @RequestBody Map<String, Object> params) {
		
		log.debug("------> post proxy sending, redirectUrl = {}, params = {}", new Object[]{redirectUrl, params.toString()});
		
		String reqDatas = JsonUtils.objectToJson(params);
		StringEntity entity = new StringEntity(reqDatas, "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		
		try {
			String result = new HttpPostReq(redirectUrl, null, entity).excuteReturnStr();
			log.debug("<------- post proxy send complete, retContent = {}", result);
			return Response.ok(result);
		} catch (Exception e) {
			log.error("post proxy send fail!!， e = {}", e);
			return Response.retn(ResultCode.REMOTE_CALL_ERROR, "post proxy send fail!!");
		}
	}

}
