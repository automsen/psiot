package cn.com.tw.saas.serv.controller.wechat;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.controller.wechat.thirdAPI.WechatPayService;
import cn.com.tw.saas.serv.controller.wechat.vo.PayReq;


@RestController
@RequestMapping("thirdPay")
public class ThirdPayController {
	
	@Autowired
	private WechatPayService wXPayService;
	
//	@Autowired
//	private ALIPayService aLIPayService;
	
	/**
	 *  生成仪表充值二维码
	 * @param payMoney 支付金额  单位 元
	 * @param outTradeNo 自定义第三方支付订单ID   // 后台生成
	 * @param custno   支付的客户号
	 * @param payType 支付方式
	 * 			1401	现金
				1402	银行转账
				1403	微信支付
				1404	补贴
				1405	支付宝支付
	 * @param orderSource 支付来源
	 * 			1301	web订单
				1302	支付宝订单
				1303	微信订单
				1304	ATM订单
	 * @param meterAddr 充值的仪表
	 * @return
	 */
	@PostMapping("qrcode")
	public Response<?> qrcode(@RequestParam("requstStr") String requstStr){
		if(StringUtils.isEmpty(requstStr)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "请求JSON不能为空！");
		}
		
		PayReq req = new PayReq();
		JSONObject queryObj = null;
		try {
			queryObj = JSONObject.parseObject(requstStr);
			
		} catch (Exception e) {
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "参数格式错误！");
		}
		String payMoney=queryObj.getString("payMoney");
		String outTradeNo=queryObj.getString("outTradeNo");
		String customerId=queryObj.getString("customerId");
		String payType=queryObj.getString("payType");
		String orderSource = queryObj.getString("orderSource");
		String meterAddr = queryObj.getString("meterAddr");
		Response<?> result = null;
	
		if(StringUtils.isEmpty(payMoney)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "请求payMoney不能为空！");
		}
		if(StringUtils.isEmpty(outTradeNo)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "请求outTradeNo不能为空！");
		}
		if(StringUtils.isEmpty(customerId)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "请求customerId不能为空！");
		}
		if(StringUtils.isEmpty(payType)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "请求payType不能为空！");
		}
		if(StringUtils.isEmpty(orderSource)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "请求orderSource不能为空！");
		}
		if(StringUtils.isEmpty(meterAddr)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "请求meterAddr不能为空！");
		}
		
		try {
			// 单位转换为
			req.setPayMoney(new BigDecimal(payMoney));
			req.setCustomerId(customerId);
			req.setMeterAddr(meterAddr);
			req.setOrderSource(orderSource);
			req.setOutTradeNo(outTradeNo);
			
			req.setPayType(payType);
			if("1403".equals(payType)){
				req.setTradeType("NATIVE");
				result = wXPayService.doUnifiedOrder(req);
			}
//			else if("1405".equals(payType)){
//				req.setTradeType("FACE_TO_FACE_PAYMENT");
//				result = aLIPayService.tradePrecreate(req);
//			}
			if(result == null){
				return Response.retn(ResultCode.SERVICE_EXPIRE_ERROR, "支付方式不支持！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.SERVICE_EXPIRE_ERROR, "支付二维码生成失败！");
		}
		return result;
	}
	
}
