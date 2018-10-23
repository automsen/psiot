package cn.com.tw.saas.serv.controller.wechat;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.controller.wechat.thirdAPI.WechatPayService;
import cn.com.tw.saas.serv.controller.wechat.vo.PayReq;

/**
 * 微信支付接口
 * 
 * @author liming
 *
 */
@Controller
@RequestMapping("wechat/wechatPay")
public class WechatPayController {

	@Autowired
	private WechatPayService wXPayService;

	private static Logger logger = LoggerFactory.getLogger(WechatPayController.class);

	/**
	 * 支付下订单
	 */
	@RequestMapping("wxOrderCreate")
	@ResponseBody
	public Response<?> wxOrderCreate(@RequestBody PayReq payReq, HttpServletRequest request) {
		return wXPayService.doUnifiedOrder(payReq);
	}

}
