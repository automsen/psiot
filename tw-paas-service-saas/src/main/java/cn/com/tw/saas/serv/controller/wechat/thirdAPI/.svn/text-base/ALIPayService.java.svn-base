//package cn.com.tw.saas.serv.controller.wechat.thirdAPI;
//
//import java.io.BufferedOutputStream;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.lang.time.DateUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import weixin.popular.bean.paymch.Unifiedorder;
//import weixin.popular.support.ExpireKey;
//import weixin.popular.util.PayUtil;
//import cn.com.tw.common.utils.CommUtils;
//import cn.com.tw.common.utils.tools.json.JsonUtils;
//import cn.com.tw.common.web.entity.Response;
//import cn.com.tw.saas.serv.controller.vo.ReturnCode;
//import cn.com.tw.saas.serv.controller.vo.request.PayReq;
//import cn.com.tw.saas.serv.entity.db.cust.ThirdOrder;
//import cn.com.tw.saas.serv.service.recharge.RechargeService;
//
//import com.alibaba.fastjson.JSONObject;
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayResponse;
//import com.alipay.api.domain.TradeFundBill;
//import com.alipay.api.internal.util.AlipaySignature;
//import com.alipay.api.response.AlipayTradePrecreateResponse;
//import com.alipay.api.response.AlipayTradeQueryResponse;
//import com.alipay.demo.trade.config.Configs;
//import com.alipay.demo.trade.model.ExtendParams;
//import com.alipay.demo.trade.model.GoodsDetail;
//import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
//import com.alipay.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
//import com.alipay.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
//import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
//import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
//import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
//import com.alipay.demo.trade.service.AlipayTradeService;
//import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
//import com.alipay.demo.trade.utils.Utils;
//
//@Service
//public class ALIPayService {
//	public final static Logger logger = LoggerFactory.getLogger(ALIPayService.class);
//	@Value("${alipay.payResultUrl}")
//	private String payResultUrl;
//	private static AlipayTradeService   tradeService;
//	@Autowired
//	private RechargeService rechargeService;
//	static{
//		Configs.init("alipayConfig.properties");
//		 tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
//	}
//	
//	private void dumpResponse(AlipayResponse response) {
//        if (response != null) {
//        	logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
//            if (StringUtils.isNotEmpty(response.getSubCode())) {
//            	logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
//                    response.getSubMsg()));
//            }
//            logger.info("body:" + response.getBody());
//        }
//    }
//	 /**
//	  * 扫码支付预下单，获取二维码
//	  * @return
//	  */
//	public Response tradePrecreate(PayReq payReq) {
//        String outTradeNo =CommUtils.getUuid();
//        payReq.setOutTradeNo(outTradeNo);
//        // 订单标题，粗略描述用户的支付目的。
//        String subject = "仪表充值  金额："+payReq.getPayMoney().toString()+"元";
//        // 订单总金额，单位为元，不能超过1亿元
//        String totalAmount = payReq.getPayMoney().toString();
//        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
//            .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
//            .setNotifyUrl(payResultUrl).setStoreId("0001TEST");
//        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
//        Response response=null;
//        switch (result.getTradeStatus()) {
//            case SUCCESS:
//                logger.info("支付宝预下单成功: )");
//                AlipayTradePrecreateResponse alRe = result.getResponse();
//                ThirdOrder thirdOrder = createThirdOrder(payReq);
//                Response<?> atmRe = null;
//                if(payReq.getMeterMap() !=null && payReq.getMeterMap().size()>0){
//					atmRe=rechargeService.rechargeOrderCreateMulti(thirdOrder);
//				}else{
//					atmRe=rechargeService.rechargeOrderCreate(thirdOrder);
//				}
//				if(atmRe.getStatusCode().equals("000000")){
//					Map<String,String> map=new HashMap<String,String>();
//					map.put("code_url", alRe.getQrCode());
//					map.put("outTradeNo", thirdOrder.getThirdOrderId());
//					response= Response.ok(map);
//				}else{
//					response= Response.retn(ReturnCode.ERROR_BUS_CREATE.getCode(),ReturnCode.ERROR_BUS_CREATE.getMsg(),atmRe);
//				}
//                dumpResponse(alRe);
//                break;
//            case FAILED:
//                logger.error("支付宝预下单失败!!!");
//                response=Response.retn(ReturnCode.ERROR_ALI_FAILED.getCode(), ReturnCode.ERROR_ALI_FAILED.getMsg());
//                break;
//
//            case UNKNOWN:
//                logger.error("系统异常，预下单状态未知!!!");
//                response=Response.retn(ReturnCode.ERROR_ALI_UNKNOW.getCode(), ReturnCode.ERROR_ALI_UNKNOW.getMsg());
//                break;
//
//            default:
//                logger.error("不支持的交易状态，交易返回异常!!!");
//                response=Response.retn(ReturnCode.ERROR_ALI_FAILED.getCode(), ReturnCode.ERROR_ALI_FAILED.getMsg());
//                break;
//        }
//        return response;
//    }
//	/**
//	 * 查询订单
//	 * @param outTradeNo
//	 * @return
//	 */
//    public Response tradeQuery(String outTradeNo) {
//    	AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder()
//      .setOutTradeNo(outTradeNo);
//        AlipayF2FQueryResult result = tradeService.queryTradeResult(builder);
//        Response response=null;
//        AlipayTradeQueryResponse alRe = result.getResponse();
//        Map<String,String> map=new HashMap<String,String>();
//        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
//        switch (result.getTradeStatus()) {
//            case SUCCESS:
//                logger.info("查询返回该订单支付成功: )");
//				map.put("orderStatus",this.myConvertStatus(alRe.getTradeStatus()));
//				map.put("msg", alRe.getSubMsg());
//				map.put("outTradeNo", outTradeNo);
//				map.put("totalFee", alRe.getTotalAmount());
//				String date=alRe.getSendPayDate() ==null ? "":format.format(alRe.getSendPayDate());
//				map.put("timeExpire",date);
//                response=Response.ok(map);
//                break;
//
//            case FAILED:
//            	if(alRe !=null){
//            		map=new HashMap<String,String>();
//    				map.put("orderStatus",this.myConvertStatus(alRe.getTradeStatus()));
//    				map.put("msg", alRe.getSubMsg());
//    				map.put("outTradeNo", outTradeNo);
//    				map.put("totalFee", alRe.getTotalAmount());
//    				alRe.getSendPayDate();
//    				date=alRe.getSendPayDate() ==null ? "":format.format(alRe.getSendPayDate());
//    				map.put("timeExpire",date);
//                    response=Response.ok(map);
//            	}else{
//            		response=Response.retn(ReturnCode.ERROR_ALI_FAILED.getCode(), ReturnCode.ERROR_ALI_FAILED.getMsg());
//            	}
//            	break;
//
//            case UNKNOWN:
//                logger.error("系统异常，订单支付状态未知!!!");
//                response=Response.retn(ReturnCode.ERROR_ALI_UNKNOW.getCode(), ReturnCode.ERROR_ALI_UNKNOW.getMsg());
//                break;
//
//            default:
//                logger.error("不支持的交易状态，交易返回异常!!!");
//                response=Response.retn(ReturnCode.ERROR_ALI_FAILED.getCode(), ReturnCode.ERROR_ALI_FAILED.getMsg());
//                break;
//        }
//        return response;
//    }
//    
//    public Response tradeRefund(String outTradeNo,String refundAmount) {
//        String outRequestNo =outTradeNo;
//        String refundReason = "正常退款";
//        String storeId = "0001TEST";
//
//        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
//            .setOutTradeNo(outTradeNo).setRefundAmount(refundAmount).setRefundReason(refundReason)
//            .setOutRequestNo(outRequestNo).setStoreId(storeId);
//
//        AlipayF2FRefundResult result = tradeService.tradeRefund(builder);
//        switch (result.getTradeStatus()) {
//            case SUCCESS:
//            	logger.info("支付宝退款成功: )");
//                break;
//
//            case FAILED:
//            	logger.error("支付宝退款失败!!!");
//                break;
//
//            case UNKNOWN:
//            	logger.error("系统异常，订单退款状态未知!!!");
//                break;
//
//            default:
//            	logger.error("不支持的交易状态，交易返回异常!!!");
//                break;
//        }
//        return Response.ok(result);
//    }
//    
//    public void alipayCallback(HttpServletRequest request, HttpServletResponse response,ExpireKey expireKey) {
//		String  message = "failed";
//		Map<String, String> params = new HashMap<String, String>();
//		Enumeration<String> parameterNames = request.getParameterNames();
//		while (parameterNames.hasMoreElements()) {
//			String parameterName = parameterNames.nextElement();
//			params.put(parameterName, request.getParameter(parameterName));
//		}
//		String trade_no=params.get("trade_no");
//		if(expireKey.exists(trade_no)){
//			return;
//		}
//		boolean signVerified = false;
//		try {
//			signVerified = AlipaySignature.rsaCheckV1(params, Configs.getAlipayPublicKey(), "UTF-8",Configs.getSignType());
//		} catch (AlipayApiException e) {
//			e.printStackTrace();
//		}
//		if (signVerified) {
//			if (!Configs.getAppid().equals(params.get("app_id"))) {
//				logger.info("与付款时的appid不同，此为异常通知，应忽略！");
//			}else{
//				String outtradeno = params.get("out_trade_no");
//				String tradeStatus = params.get("trade_status");
//				String totalAmount = params.get("total_amount");
//				ThirdOrder thirdOrder = new ThirdOrder();
//				thirdOrder.setThirdOrderId(outtradeno);
//				thirdOrder.setTotalFee(new BigDecimal(totalAmount).multiply(new BigDecimal("100")));
//				thirdOrder.setTransactionId(trade_no);
//				try {
//					if("WAIT_BUYER_PAY".equals(tradeStatus)){//交易创建，等待买家付款
//	                	
//	                }else if("TRADE_CLOSED".equals(tradeStatus)){//未付款交易超时关闭，或支付完成后全额退款
//	                	
//	                }else if("TRADE_SUCCESS".equals(tradeStatus) ||"TRADE_FINISHED".equals(tradeStatus)){//交易支付成功或者交易结束不可退款
//	                	rechargeService.rechargeOrderSuccess(thirdOrder);
//	                	message="success";
//	                }else{
//	                	
//	                }
//					expireKey.add(trade_no);
//				} catch (Exception e) {
//					logger.error("##tw-web-corp SERVICE## rechargeOrderSuccess error {}",e.getMessage());
//					e.printStackTrace();
//				}
//				
//			}
//		} else { 
//			logger.info("验证签名失败！");
//		}
//		try {
//			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//			out.write(message.getBytes());
//			out.flush();
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
//    
//    public String getHostAddress(){
//		String ip="";
//		try {
//			ip=InetAddress.getLocalHost().getHostAddress();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
//		return ip;
//	}
//	
//	private ThirdOrder createThirdOrder(PayReq payReq) {
//		ThirdOrder returnOrder = new ThirdOrder();
//		returnOrder.setThirdOrderId(payReq.getOutTradeNo());
//		returnOrder.setCustomerNo(payReq.getCustomerNo());
//		if(payReq.getMeterMap() !=null){
//			returnOrder.setMeterNumber(payReq.getMeterMap().size());
//			returnOrder.setMeterAddr(JsonUtils.objectToJson(payReq.getMeterMap()));
//			returnOrder.setMeterMap(payReq.getMeterMap());
//		}else{
//			returnOrder.setMeterNumber(1);
//			returnOrder.setMeterAddr(payReq.getMeterId());
//		}
//		returnOrder.setOpenId(payReq.getOpenId());
//		returnOrder.setOrderSource(payReq.getOrderSource());
//		returnOrder.setTotalFee(payReq.getPayMoney());
//		returnOrder.setPayType(payReq.getPayType());    
//		returnOrder.setSpbillCreateIp(this.getHostAddress());
//		returnOrder.setTimeStart(new Date());
//		returnOrder.setTradeType(payReq.getTradeType());
//		return returnOrder;
//	}
//	
//	/**
//	 * 
//	 * @param tradeState
//	 * WAIT_BUYER_PAY（交易创建，等待买家付款）
//		TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）
//		TRADE_SUCCESS（交易支付成功）
//		TRADE_FINISHED（交易结束，不可退款）
//	 * @return
//	 */
//	private String myConvertStatus(String tradeState){
//		if(tradeState ==null || "WAIT_BUYER_PAY".equals(tradeState)){
//			return "0";//初始
//		}else if("TRADE_SUCCESS TRADE_FINISHED".contains(tradeState)){
//			return "1";//支付成功
//		}else if("TRADE_CLOSED".equals(tradeState)){
//			return "2";//支付失败
//		}else{
//			return "3";//未知
//		}
//	}
//
//}
