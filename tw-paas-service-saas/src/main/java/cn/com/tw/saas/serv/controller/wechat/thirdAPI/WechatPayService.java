package cn.com.tw.saas.serv.controller.wechat.thirdAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.Closeorder;
import weixin.popular.bean.paymch.DownloadbillResult;
import weixin.popular.bean.paymch.MchBaseResult;
import weixin.popular.bean.paymch.MchDownloadbill;
import weixin.popular.bean.paymch.MchOrderInfoResult;
import weixin.popular.bean.paymch.MchOrderquery;
import weixin.popular.bean.paymch.MchPayNotify;
import weixin.popular.bean.paymch.Refundquery;
import weixin.popular.bean.paymch.RefundqueryResult;
import weixin.popular.bean.paymch.SecapiPayRefund;
import weixin.popular.bean.paymch.SecapiPayRefundResult;
import weixin.popular.bean.paymch.Unifiedorder;
import weixin.popular.bean.paymch.UnifiedorderResult;
import weixin.popular.client.LocalHttpClient;
import weixin.popular.support.ExpireKey;
import weixin.popular.util.PayUtil;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.StreamUtils;
import weixin.popular.util.XMLConverUtil;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.common.utils.WeixinUtil;
import cn.com.tw.saas.serv.common.utils.cons.SysCommons;
import cn.com.tw.saas.serv.controller.wechat.vo.PayReq;
import cn.com.tw.saas.serv.controller.wechat.vo.ReturnCode;
import cn.com.tw.saas.serv.entity.db.cust.ThirdOrder;
import cn.com.tw.saas.serv.entity.org.OrgPayConfig;
import cn.com.tw.saas.serv.service.cust.ChargeService;
import cn.com.tw.saas.serv.service.org.OrgPayConfigService;

import com.alibaba.fastjson.JSONObject;

@Service
public class WechatPayService {
	public final static Logger logger = LoggerFactory.getLogger(WechatPayService.class);

	@Value("${weixin.scan.payResultUrl}")
	private String payResultUrl;

	@Autowired
	private ChargeService chargeService;
	@Autowired
	private OrgPayConfigService orgPayConfigService;
	
	
	
	
	/**
	 * 统一支付下单
	 */
	public Response<?> doUnifiedOrder(PayReq payReq) {
		String mchid = null;
		String key = null;
		String appId = null;
		String signType = null;
		try {
			String configJson =initPayConfig();
			JSONObject wechatConfig = JSONObject.parseObject(configJson);
			mchid = wechatConfig.getString("mchid");
			key = wechatConfig.getString("key");
			appId = wechatConfig.getString("appId");
			signType = wechatConfig.getString("signType");
			if(StringUtils.isEmpty(mchid) || StringUtils.isEmpty(key) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(signType)){
				return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"系统错误0322");
			}
		} catch (Exception e1) {
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"系统错误0322");
		}
		// 提供给微信接口的金额，单位（分）
		BigDecimal decimal = payReq.getPayMoney().multiply(new BigDecimal("100"));
		Unifiedorder order = new Unifiedorder();
		// any order
		order.setAppid(appId);
		order.setMch_id(mchid);
		order.setNonce_str(WeixinUtil.getRandomStringByLength(32));
		order.setNotify_url(payResultUrl);

		// any TradeType
		order.setBody("仪表充值  金额：" + payReq.getPayMoney().toString() + "元");
		order.setTotal_fee(decimal.intValue() + "");
		order.setSpbill_create_ip(getHostAddress());
		order.setTrade_type(payReq.getTradeType());

		// tradeType=NATIVE
		if ("NATIVE".equals(payReq.getTradeType())) {
			order.setProduct_id(order.getOut_trade_no());
		}
		// tradeType=JSAPI
		if ("JSAPI".equals(payReq.getTradeType())) {
			order.setOpenid(payReq.getOpenId());
		}

		UnifiedorderResult result = null;
		try {
			// 初步组装参数
			ThirdOrder thirdOrder = createThirdOrder(order, payReq);
			// 预存订单
			ThirdOrder temp = chargeService.createWechatOrder(thirdOrder);
			if (temp == null)
				return Response.retn(ReturnCode.ERROR_BUS_CREATE.getCode(), ReturnCode.ERROR_BUS_CREATE.getMsg());
			order.setOut_trade_no(temp.getOrderId());
			JwtInfo jwtInfo = JwtLocal.getJwt();
			String orgId = (String) jwtInfo.getExt("orgId");
			order.setAttach(orgId);
			result = PayMchAPI.payUnifiedorder(order, key);
			// 通讯结果
			if ("SUCCESS".equals(result.getReturn_code())) {
				// 返回码正常
				if ("SUCCESS".equals(result.getResult_code())) {
					if ("JSAPI".equals(thirdOrder.getTradeType())) {
						String jsonStr = PayUtil.generateMchPayJsRequestJson(result.getPrepay_id(), appId, key);
						return Response.ok(JSONObject.parseObject(jsonStr));
					} else if ("NATIVE".equals(thirdOrder.getTradeType())) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("code_url", result.getCode_url());
						map.put("outTradeNo", thirdOrder.getOrderId());
						return Response.ok(map);
					}
					return Response.ok(result);

				} else {
					// 订单失败
					return Response.retn(ReturnCode.ERROR_BUS.getCode(), result.getErr_code_des());
				}
			} else {
				// 通讯异常
				return Response.retn(ReturnCode.ERROR_CONN.getCode(), result.getReturn_msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("weixin doUnifiedOrder request error:{}", e);
			return Response.retn(ReturnCode.ERROR_EXCEPTION.getCode(), ReturnCode.ERROR_EXCEPTION.getMsg());
		}

	}
	
	
	

	/**
	 * 查询订单
	 */
	public Response<?> doOrderQuery(String outTradeNo) {
		String mchid = null;
		String key = null;
		String appId = null;
		String signType = null;
		try {
			String configJson =initPayConfig();
			JSONObject wechatConfig = JSONObject.parseObject(configJson);
			mchid = wechatConfig.getString("mchid");
			key = wechatConfig.getString("key");
			appId = wechatConfig.getString("appId");
			signType = wechatConfig.getString("signType");
			if(StringUtils.isEmpty(mchid) || StringUtils.isEmpty(key) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(signType)){
				return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"系统错误0322");
			}
		} catch (Exception e1) {
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"系统错误0322");
		}
		MchOrderquery query = new MchOrderquery();
		query.setAppid(appId);
		query.setMch_id(mchid);
		query.setSign_type(signType);
		query.setNonce_str(WeixinUtil.getRandomStringByLength(32));

		query.setOut_trade_no(outTradeNo);
		MchOrderInfoResult result = null;
		try {
			result = PayMchAPI.payOrderquery(query, key);
			// 通讯结果
			if ("SUCCESS".equals(result.getReturn_code())) {
				// 返回码正常
				if ("SUCCESS".equals(result.getResult_code())) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("orderStatus", this.convertStatus(result.getTrade_state()));
					map.put("outTradeNo", outTradeNo);
					map.put("msg", result.getReturn_msg());
					if (result.getTotal_fee() != null)
						map.put("totalFee", String.valueOf(result.getTotal_fee() / 100));
					map.put("timeExpire", result.getTime_end());
					return Response.ok(map);
				} else {
					// 订单失败
					return Response.retn(ReturnCode.ERROR_BUS.getCode(), result.getErr_code_des());
				}
			} else {
				// 通讯异常
				return Response.retn(ReturnCode.ERROR_CONN.getCode(), result.getReturn_msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("weixin doOrderQuery request error:{}", e.toString());
			return Response.retn(ReturnCode.ERROR_EXCEPTION.getCode(), ReturnCode.ERROR_EXCEPTION.getMsg());
		}
	}

	/**
	 * 关闭订单
	 */
	public Response<?> doCloseorder(String outTradeNo) {
		String mchid = null;
		String key = null;
		String appId = null;
		String signType = null;
		try {
			String configJson =initPayConfig();
			JSONObject wechatConfig = JSONObject.parseObject(configJson);
			mchid = wechatConfig.getString("mchid");
			key = wechatConfig.getString("key");
			appId = wechatConfig.getString("appId");
			signType = wechatConfig.getString("signType");
			if(StringUtils.isEmpty(mchid) || StringUtils.isEmpty(key) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(signType)){
				return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"系统错误0322");
			}
		} catch (Exception e1) {
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"系统错误0322");
		}
		Closeorder closeorder = new Closeorder();
		closeorder.setAppid(appId);
		closeorder.setMch_id(mchid);
		closeorder.setSign_type(signType);
		closeorder.setNonce_str(WeixinUtil.getRandomStringByLength(32));

		closeorder.setOut_trade_no(outTradeNo);
		MchBaseResult result = null;
		try {
			result = PayMchAPI.payCloseorder(closeorder, key);
			// 通讯结果
			if ("SUCCESS".equals(result.getReturn_code())) {
				// 返回码正常
				if ("SUCCESS".equals(result.getResult_code())) {
					return Response.ok(result);
				} else {
					// 订单失败
					return Response.retn(ReturnCode.ERROR_BUS.getCode(), result.getErr_code_des());
				}
			} else {
				// 通讯异常
				return Response.retn(ReturnCode.ERROR_CONN.getCode(), result.getReturn_msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("weixin doCloseorder request error:{}", e.toString());
			return Response.retn(ReturnCode.ERROR_EXCEPTION.getCode(), ReturnCode.ERROR_EXCEPTION.getMsg());
		}
	}

	/**
	 * 申请退款
	 */
	public Response<?> doRefund(String outTradeNo, String refundMoney) {
		String mchid = null;
		String key = null;
		String appId = null;
		String signType = null;
		try {
			String configJson =initPayConfig();
			JSONObject wechatConfig = JSONObject.parseObject(configJson);
			mchid = wechatConfig.getString("mchid");
			key = wechatConfig.getString("key");
			appId = wechatConfig.getString("appId");
			signType = wechatConfig.getString("signType");
			if(StringUtils.isEmpty(mchid) || StringUtils.isEmpty(key) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(signType)){
				return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"系统错误0322");
			}
		} catch (Exception e1) {
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"系统错误0322");
		}
		BigDecimal decimal = new BigDecimal(refundMoney).multiply(new BigDecimal("100"));
		SecapiPayRefund refund = new SecapiPayRefund();
		refund.setAppid(appId);
		refund.setMch_id(mchid);
		refund.setSign_type(signType);
		refund.setNonce_str(WeixinUtil.getRandomStringByLength(32));

		refund.setOut_trade_no(outTradeNo);
		refund.setOut_refund_no(outTradeNo);
		refund.setTotal_fee(decimal.intValue());
		refund.setRefund_fee(decimal.intValue());
		refund.setRefund_fee_type("CNY");
		refund.setOp_user_id(mchid);
		SecapiPayRefundResult result = null;
		try {
			// String certPath="F:\\cert\\apiclient_cert.p12";
			// LocalHttpClient.initMchKeyStore(mchid,certPath);
			String certPath = "D://CERT/common/apiclient_cert.p12";
			File file = new File(certPath);
			InputStream certStream = new FileInputStream(file);
			LocalHttpClient.initMchKeyStore(mchid, certStream);
			result = PayMchAPI.secapiPayRefund(refund, key);
			// 通讯结果
			if ("SUCCESS".equals(result.getReturn_code())) {
				// 返回码正常
				if ("SUCCESS".equals(result.getResult_code())) {
					return Response.ok(result);
				} else {
					// 订单失败
					return Response.retn(ReturnCode.ERROR_BUS.getCode(), result.getErr_code_des());
				}
			} else {
				// 通讯异常
				return Response.retn(ReturnCode.ERROR_CONN.getCode(), result.getReturn_msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("weixin doRefund request error:{}", e.toString());
			return Response.retn(ReturnCode.ERROR_EXCEPTION.getCode(), ReturnCode.ERROR_EXCEPTION.getMsg());
		}
	}

	/**
	 * 查询退款
	 */
	public Response<?> doRefundQuery(String outTradeNo) {
		String mchid = null;
		String key = null;
		String appId = null;
		String signType = null;
		try {
			String configJson =initPayConfig();
			JSONObject wechatConfig = JSONObject.parseObject(configJson);
			mchid = wechatConfig.getString("mchid");
			key = wechatConfig.getString("key");
			appId = wechatConfig.getString("appId");
			signType = wechatConfig.getString("signType");
			if(StringUtils.isEmpty(mchid) || StringUtils.isEmpty(key) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(signType)){
				return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"系统错误0322");
			}
		} catch (Exception e1) {
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"系统错误0322");
		}
		Refundquery refund = new Refundquery();
		refund.setAppid(appId);
		refund.setMch_id(mchid);
		refund.setSign_type(signType);
		refund.setNonce_str(WeixinUtil.getRandomStringByLength(32));

		refund.setOut_trade_no(outTradeNo);
		RefundqueryResult result = null;
		try {
			result = PayMchAPI.payRefundquery(refund, key);
			// 通讯结果
			if ("SUCCESS".equals(result.getReturn_code())) {
				// 返回码正常
				if ("SUCCESS".equals(result.getResult_code())) {
					return Response.ok(result);
				} else {
					// 订单失败
					return Response.retn(ReturnCode.ERROR_BUS.getCode(), result.getErr_code_des());
				}
			} else {
				// 通讯异常
				return Response.retn(ReturnCode.ERROR_CONN.getCode(), result.getReturn_msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("weixin doRefundQuery request error:{}", e.toString());
			return Response.retn(ReturnCode.ERROR_EXCEPTION.getCode(), ReturnCode.ERROR_EXCEPTION.getMsg());
		}
	}

	/**
	 * 下载对账单 date "20171110"
	 */
	public Response<?> doDownloadbill(String date) {
		String mchid = null;
		String key = null;
		String appId = null;
		String signType = null;
		try {
			String configJson =initPayConfig();
			JSONObject wechatConfig = JSONObject.parseObject(configJson);
			mchid = wechatConfig.getString("mchid");
			key = wechatConfig.getString("key");
			appId = wechatConfig.getString("appId");
			signType = wechatConfig.getString("signType");
			if(StringUtils.isEmpty(mchid) || StringUtils.isEmpty(key) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(signType)){
				return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"系统错误0322");
			}
		} catch (Exception e1) {
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"系统错误0322");
		}
		MchDownloadbill bill = new MchDownloadbill();
		bill.setAppid(appId);
		bill.setMch_id(mchid);
		bill.setSign_type(signType);
		bill.setNonce_str(WeixinUtil.getRandomStringByLength(32));

		bill.setBill_date(date);
		bill.setBill_type("ALL");
		DownloadbillResult result = null;
		try {
			DownloadbillResult temp = PayMchAPI.payDownloadbill(bill, key);
			result = XMLConverUtil.convertToObject(DownloadbillResult.class, temp.getData());
			result.setData(temp.getData());
			// 通讯结果
			if ("SUCCESS".equals(result.getReturn_code())) {
				// 返回码正常
				if ("SUCCESS".equals(result.getResult_code())) {
					return Response.ok(result);
				} else {
					// 订单失败
					return Response.retn(ReturnCode.ERROR_BUS.getCode(), result.getErr_code_des());
				}
			} else {
				// 通讯异常
				return Response.retn(ReturnCode.ERROR_CONN.getCode(), result.getReturn_msg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("weixin doDownloadbill request error:{}", e.toString());
			return Response.retn(ReturnCode.ERROR_EXCEPTION.getCode(), ReturnCode.ERROR_EXCEPTION.getMsg());
		}
	}

	/**
	 * 支付结果通知
	 * 
	 * @param request
	 * @param response
	 * @param expireKey
	 */
	public void rechargeCallback(HttpServletRequest request, HttpServletResponse response, ExpireKey expireKey) {
		String mchid = null;
		String key = null;
		String appId = null;
		String signType = null;
		
		// 获取请求数据
		try {
			String xmlData = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
			// 将XML转为MAP,确保所有字段都参与签名验证
			Map<String, String> mapData = XMLConverUtil.convertToMap(xmlData);
			// 转换数据对象
			MchPayNotify payNotify = XMLConverUtil.convertToObject(MchPayNotify.class, xmlData);
			try {
				String orgId = payNotify.getAttach();
				JwtInfo info = new JwtInfo();
				info.setExtend("orgId", orgId);
				JwtLocal.setJwt(info);
				String configJson =initPayConfig();
				JSONObject wechatConfig = JSONObject.parseObject(configJson);
				mchid = wechatConfig.getString("mchid");
				key = wechatConfig.getString("key");
				appId = wechatConfig.getString("appId");
				signType = wechatConfig.getString("signType");
				if(StringUtils.isEmpty(mchid) || StringUtils.isEmpty(key) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(signType)){
					MchBaseResult baseResult = new MchBaseResult();
					baseResult.setReturn_code("FAIL");
					baseResult.setReturn_msg("ERROR");
					response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
					return;
				}
			} catch (Exception e1) {
				MchBaseResult baseResult = new MchBaseResult();
				baseResult.setReturn_code("FAIL");
				baseResult.setReturn_msg("ERROR");
				response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
				return;
			}
			
			// 已处理 去重
			if (expireKey.exists(payNotify.getTransaction_id())) {
				return;
			}
			payNotify.buildDynamicField(mapData);
			logger.info("wechat pay callback MchPayNotify:{}", JsonUtils.objectToJson(payNotify));
			// 签名验证
			if (SignatureUtil.validateSign(mapData, key)) {
				ThirdOrder thirdOrder = new ThirdOrder();
				thirdOrder.setOrderId(payNotify.getOut_trade_no());
				// 金额 单位（分）
				thirdOrder.setMoney(new BigDecimal(payNotify.getTotal_fee()));
				thirdOrder.setTransactionId(payNotify.getTransaction_id());
				// Return_code 表示通信结果
				// 通信成功
				if ("SUCCESS".equals(payNotify.getReturn_code())) {
					// Result_code 表示交易结果
					// 交易成功
					if ("SUCCESS".equals(payNotify.getResult_code())) {
						logger.info("wechat pay result success! orderId = {}", payNotify.getOut_trade_no());
						try {
							chargeService.wechatChargeSuccess(thirdOrder);
						} catch (Exception e) {
							logger.error("XXXXXXXXXXXXXXXX wechat pay execute err! msg = {}", e);
							e.printStackTrace();
						}
					}
					// 交易失败
					else {
						logger.error("XXXXXXXXXXXXXXXX wechat pay result fail! msg = {}", payNotify.getReturn_msg());
						thirdOrder.setErrCode(payNotify.getErr_code());
						thirdOrder.setErrCodeDes(payNotify.getErr_code_des());
						chargeService.wechatChargeFail(thirdOrder);
					}
					expireKey.add(payNotify.getTransaction_id());
					MchBaseResult baseResult = new MchBaseResult();
					baseResult.setReturn_code("SUCCESS");
					baseResult.setReturn_msg("OK");
					response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
				}
				// 通信失败
				else {
					logger.error("XXXXXXXXXXXXXXXX wechat pay return fail! msg = {}", payNotify.getErr_code());
				}
			} else {
				MchBaseResult baseResult = new MchBaseResult();
				baseResult.setReturn_code("FAIL");
				baseResult.setReturn_msg("ERROR");
				response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
				logger.error("XXXXXXXXXXXXXXXX wechat pay callback validateSign error");
			}
		} catch (Exception e) {
			logger.error("XXXXXXXXXXXXXXXX wechat pay unknow exception:{}", e);
			e.printStackTrace();
		}
	}

	/**
	 * 退款回调
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("refundCallback")
	public void refundCallback(HttpServletRequest request, HttpServletResponse response, ExpireKey expireKey) {
//		// 获取请求数据
//		try {
//			String xmlData = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
//			// 将XML转为MAP,确保所有字段都参与签名验证
//			Map<String, String> mapData = XMLConverUtil.convertToMap(xmlData);
//			// 转换数据对象
//			MchPayRefundNotify refundNotify = XMLConverUtil.convertToObject(MchPayRefundNotify.class, xmlData);
//			MchPayRefundNotifyReqInfo reqInfo = XMLConverUtil.convertToObject(MchPayRefundNotifyReqInfo.class, xmlData);
//			refundNotify.setReqInfo(reqInfo);
//			// 已处理 去重
//			if (expireKey.exists(refundNotify.getReqInfo().getTransaction_id())) {
//				return;
//			}
//			// 签名验证
//			if (SignatureUtil.validateSign(mapData, key)) {
//				expireKey.add(refundNotify.getReqInfo().getTransaction_id());
//				MchBaseResult baseResult = new MchBaseResult();
//				baseResult.setReturn_code("SUCCESS");
//				baseResult.setReturn_msg("OK");
//				response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
//			} else {
//				MchBaseResult baseResult = new MchBaseResult();
//				baseResult.setReturn_code("FAIL");
//				baseResult.setReturn_msg("ERROR");
//				response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
//			}
//		} catch (Exception e) {
//		}
	}

	public String getHostAddress() {
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}

	/**
	 * 组装参数
	 * @param order
	 * @param payReq
	 * @return
	 */
	private ThirdOrder createThirdOrder(Unifiedorder order, PayReq payReq) {
		ThirdOrder returnOrder = new ThirdOrder();
		returnOrder.setCustomerId(payReq.getCustomerId());
		if (payReq.getMeterMap() != null) {
			returnOrder.setMeterNumber(payReq.getMeterMap().size());
			returnOrder.setMeterAddr(JsonUtils.objectToJson(payReq.getMeterMap()));
			returnOrder.setMeterMap(payReq.getMeterMap());
		} else {
			returnOrder.setMeterNumber(1);
			returnOrder.setMeterAddr(payReq.getMeterAddr());
		}
		returnOrder.setOpenId(order.getOpenid());
		// returnOrder.setOrderSource(payReq.getOrderSource());
		returnOrder.setMoney(payReq.getPayMoney());
		// returnOrder.setPayType(payReq.getPayType());
		returnOrder.setSpbillCreateIp(order.getSpbill_create_ip());
		returnOrder.setTradeType(payReq.getTradeType());
		return returnOrder;
	}

	/**
	 * 
	 * @param tradeState
	 *            SUCCESS—支付成功 REFUND—转入退款 NOTPAY—未支付 CLOSED—已关闭
	 *            REVOKED—已撤销（刷卡支付） USERPAYING--用户支付中
	 *            PAYERROR--支付失败(其他原因，如银行返回失败)
	 * @return
	 */
	private String convertStatus(String tradeState) {
		if ("NOTPAY USERPAYING".contains(tradeState)) {
			return "0";// 初始
		} else if ("SUCCESS".equals(tradeState)) {
			return "1";// 支付成功
		} else if ("CLOSED REVOKED PAYERROR REFUND".contains(tradeState)) {
			return "2";// 支付失败
		} else {
			return "3";// 未知
		}
	}
	
	private String initPayConfig() throws Exception{
		JwtInfo jwt = JwtLocal.getJwt();
		if(jwt == null ){
			throw new Exception();
		}
		String orgId = (String) jwt.getExt("orgId");
		OrgPayConfig config = new OrgPayConfig();
		config.setConfigStatus(new Byte("0"));
		config.setPayType(SysCommons.payType.Wechat_Pay);
		config.setPayOrgId(orgId);
		
		List<OrgPayConfig> tempConfigs = orgPayConfigService.selectByEntity(config);
		if(tempConfigs == null || tempConfigs.size() ==0 ){
			throw new Exception();
		}
		config = tempConfigs.get(0);
		if(StringUtils.isEmpty(config.getPayConfigJson())){
			throw new Exception();
		}
		
		return config.getPayConfigJson();
		
	}



	
	
}
