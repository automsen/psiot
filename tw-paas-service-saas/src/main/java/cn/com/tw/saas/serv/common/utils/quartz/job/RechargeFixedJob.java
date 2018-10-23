package cn.com.tw.saas.serv.common.utils.quartz.job;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.saas.serv.common.utils.WeixinUtil;
import cn.com.tw.saas.serv.common.utils.cons.SysCommons;
import cn.com.tw.saas.serv.entity.db.cust.OrderRecharge;
import cn.com.tw.saas.serv.entity.db.cust.ThirdOrder;
import cn.com.tw.saas.serv.entity.org.OrgPayConfig;
import cn.com.tw.saas.serv.mapper.cust.OrderRechargeMapper;
import cn.com.tw.saas.serv.mapper.org.OrgPayConfigMapper;
import cn.com.tw.saas.serv.service.cust.ChargeService;
import weixin.popular.api.PayMchAPI;
import weixin.popular.bean.paymch.MchOrderInfoResult;
import weixin.popular.bean.paymch.MchOrderquery;

/**
 * 充值补救定时任务
 * @author liming
 * 2018年9月11日 15:21:44
 */
@Component
public class RechargeFixedJob   {
	
    private static Logger logger = LoggerFactory.getLogger(RechargeFixedJob.class);

	private ChargeService chargeService;
	
	private OrgPayConfigMapper orgPayConfigMapper; 
	
	private OrderRechargeMapper orderRechargeMapper;
    
	
	public RechargeFixedJob() {
		chargeService = (ChargeService) SpringContext.getBean("chargeServiceImpl");
		orgPayConfigMapper = (OrgPayConfigMapper) SpringContext.getBean("orgPayConfigMapper");
		orderRechargeMapper = (OrderRechargeMapper) SpringContext.getBean("orderRechargeMapper");
		
	}
    

    @Scheduled(cron = "0 0 0/2 * * ?")
	public void scheduled(){
    	logger.debug("################## init RechargeFixedJob ################");
    	try {
    		List<OrderRecharge> orders = null;
    		
    		// 1. 查询第三方未完成订单
    		OrderRecharge queryOrder = new OrderRecharge();
    		queryOrder.setStatus(new Byte("0")); // 未完成订单
    		queryOrder.setLastHours(5);          // 近5小时的
    		queryOrder.setOrderSource(SysCommons.orderSource.wechat_order); // 微信订单
    		orders = orderRechargeMapper.selectByEntity(queryOrder);
    		// 2. 通过微信API 判断当前订单状态，并更新到本地
    		if(orders!= null && orders.size() >0){
    			for (OrderRecharge orderRecharge : orders) {
    				loadWeixinOrderDetail(orderRecharge);
    			}
    		}
    		// 3. 如果出现回调失败，本地没有充值成功。采用补救措施
		} catch (Exception e) {
			logger.debug("######################rechargeFixedJob Error Exception:{}",e);
		}
	}
    
    /**
     *  初始化配置
     * @param orgId
     * @return
     */
    private String initPayConfig(String orgId)  {
		OrgPayConfig config = new OrgPayConfig();
		config.setConfigStatus(new Byte("0"));
		config.setPayType(SysCommons.payType.Wechat_Pay);
		config.setPayOrgId(orgId);
		
		List<OrgPayConfig> tempConfigs = orgPayConfigMapper.selectByEntity(config);
		if(tempConfigs == null || tempConfigs.size() ==0 ){
			return null;
		}
		config = tempConfigs.get(0);
		return config.getPayConfigJson();
	}
    
    /**
     * @param orderRecharge
     * @return
     */
    private void loadWeixinOrderDetail(OrderRecharge orderRecharge) {
    	String bussinesNo = orderRecharge.getExternalNo();
		String configJson = initPayConfig(orderRecharge.getOrgId()); 
		if(StringUtils.isEmpty(configJson)){
			logger.debug("############### order({}) config not ready ##########################",bussinesNo);
			return ;
		}
		JSONObject wechatConfig = JSONObject.parseObject(configJson);
		String mchid = wechatConfig.getString("mchid");
		String key = wechatConfig.getString("key");
		String appId = wechatConfig.getString("appId");
		String signType = wechatConfig.getString("signType");
		if(StringUtils.isEmpty(mchid) || StringUtils.isEmpty(key) || StringUtils.isEmpty(appId) || StringUtils.isEmpty(signType)){
			logger.debug("############### order({}) weixin config error ##########################",bussinesNo);
			return ;
		}
		MchOrderquery query = new MchOrderquery();
		query.setAppid(appId);
		query.setMch_id(mchid);
		query.setSign_type(signType);
		query.setNonce_str(WeixinUtil.getRandomStringByLength(32));
		query.setOut_trade_no(bussinesNo);
		MchOrderInfoResult result = null;
		try {
			result = PayMchAPI.payOrderquery(query, key);
			// 通讯结果
			if ("SUCCESS".equals(result.getReturn_code())) {
				// 返回码正常
				if ("SUCCESS".equals(result.getResult_code())) {
					ThirdOrder thirdOrder = new ThirdOrder();
					thirdOrder.setOrderId(result.getOut_trade_no());
					logger.debug("############### order({}) status:{} ##########################",bussinesNo,result.getResult_code());
					// 订单交易成功
					if("SUCCESS".equals(result.getTrade_state())){
						// 金额 单位（分）
						thirdOrder.setMoney(new BigDecimal(result.getTotal_fee()));
						thirdOrder.setTransactionId(result.getTransaction_id());
						chargeService.wechatChargeSuccess(thirdOrder);
					}else{
						thirdOrder.setErrCode(result.getErr_code());
						thirdOrder.setErrCodeDes(result.getErr_code_des());
						chargeService.wechatChargeFail(thirdOrder);
					}
				}else{
					logger.debug("############### order({}) query order By weixin error##########################",bussinesNo);
				}
			}else{
				logger.debug("############### order({})  connect weixin service error,errorCode:{}##########################",bussinesNo,result.getReturn_code());
			}
		} catch (Exception e) {
			logger.debug("############### order({}) query order By weixin exception:{}##########################",bussinesNo,e);
			e.printStackTrace();
		}
	}

	
	
}
