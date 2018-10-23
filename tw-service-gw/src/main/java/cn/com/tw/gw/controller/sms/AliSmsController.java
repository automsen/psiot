package cn.com.tw.gw.controller.sms;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.gw.service.sms.AliSmsService;

/**
 * 阿里云短信接口
 * @author admin
 *
 */
@RestController
@RequestMapping("aliSMS")
public class AliSmsController {
	
	@Autowired
	private AliSmsService aliSmsService;
	
	public static final Logger log = LoggerFactory.getLogger(AliSmsController.class);
	
	//发送验证码
	@RequestMapping(value="verify")
    public Response<?> sendVerifySMS(@RequestParam String message) {
		log.debug("{} send ali verify SMS, params phone =  {}",new Object[]{SysCons.LOG_START,message == null ? "null" : message.toString()});
        try {
        	String json = new String(Base64.decodeBase64(message),"utf-8");
        	
        	@SuppressWarnings("unchecked")
			Map<String, String> params = JsonUtils.jsonToPojo(json, Map.class);
        	log.debug(" params =  {}", params.toString());
        	
            //获取手机号，此处需要校验手机号
            aliSmsService.sendVerifySMS(params.get("phoneNum"), params.get("signature"), params.get("templNum"), params.get("message"));
            
            log.debug("{} send ali verify SMS success",SysCons.LOG_END);
            //返回码为000000则为发送成功
            return Response.ok();
        } catch (BusinessException e) {
        	log.error("{} code = {},get auth code is exception, message = {}",new Object[]{SysCons.LOG_ERROR, e.getCode(), e.getMessage()});
            return Response.retn(e.getCode(), e.getMessage());
        } catch (Exception e) {
        	log.error("{} code = {},get auth code is exception, message = {}",new Object[]{SysCons.LOG_ERROR, ResultCode.UNKNOW_ERROR, e.toString()});
            return Response.retn(ResultCode.UNKNOW_ERROR);
        }
       
   }

}
