package cn.com.tw.gw.controller.email;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.gw.entity.MailParam;
import cn.com.tw.gw.service.email.EmailService;

@RestController
@RequestMapping("send")
public class EmailController {
	
	public static final Logger log = LoggerFactory.getLogger(EmailService.class);
	
	@Autowired
	private EmailService emailService;
	
	/**
	 * 邮件网关api
	 * @param message
	 * @return
	 */
	@RequestMapping(value="email")
	public Response<?> sendEmail(@RequestParam String message) {
		log.debug("{} send email start, params message =  {}",new Object[]{SysCons.LOG_START,message == null ? "null" : message.toString()});
		try {
			String json = new String(Base64.decodeBase64(message),"utf-8");
			MailParam mailParam = JsonUtils.jsonToPojo(json, MailParam.class);
			emailService.sendEmail(mailParam);
			log.debug("{} exit out sendEmail(), mailParam = {}",new Object[]{SysCons.LOG_END, mailParam.toString()});
			return Response.ok();
		} catch (Exception e) {
			log.error("{} code = {},send email is exception, message = {}",new Object[]{SysCons.LOG_ERROR, ResultCode.UNKNOW_ERROR, e.toString()});
			return Response.retn(ResultCode.UNKNOW_ERROR);
		}
	}

}
