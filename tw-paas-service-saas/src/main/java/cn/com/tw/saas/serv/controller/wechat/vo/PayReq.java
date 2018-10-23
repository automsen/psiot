package cn.com.tw.saas.serv.controller.wechat.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 微信下单请求参数
 * 
 * @author admin
 *
 */
public class PayReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6788287022513726653L;
	/**
	 * 客户id
	 */
	private String customerId;
	/**
	 * 客户的注册手机号
	 */
	private String cellphone;
	private String meterAddr;
	/**
	 * 仪表编号
	 * <meterId,chargeMoney>的Map数组。[{meterId:000000191878,chargeMoney:108.35},{
	 * meterId:000000200878,chargeMoney:199.05}]
	 */
	private Map<String, String> meterMap;
	/**
	 * 内部订单流水号，对应t_base_third_order主键
	 */
	private String outTradeNo;
	/**
	 * 充值金额(单位：元)
	 */
	private BigDecimal payMoney;
	/**
	 * 订单来源：(APP：移动客户端,WX：微信公众号,ATM：ATM机)
	 */
	private String orderSource;
	/**
	 * 第三方支付的内部交易类型： 微信交易类型(JSAPI，NATIVE，APP)
	 * 支付宝(当面付：FACE_TO_FACE_PAYMENT、APP支付：QUICK_MSECURITY_PAY、手机网页支付：
	 * QUICK_WAP_PAY、电脑网站支付：FAST_INSTANT_TRADE_PAY)
	 */
	private String tradeType;

	/**
	 * 支付方式: 1401:现金、 1402:银行转账、 1403:第三方支付(微信)、1404:免费补助、 1405:第三方支付(支付宝)
	 */
	private String payType;
	/**
	 * 第三方客户识别号
	 */
	private String openId;

	/**
	 * 第三方平台的业务ID
	 */
	private String transactionId;

	// private ReqToken token;

	public String getOrderSource() {
		return orderSource;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMeterAddr() {
		return meterAddr;
	}

	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}

	public Map<String, String> getMeterMap() {
		return meterMap;
	}

	public void setMeterMap(Map<String, String> meterMap) {
		this.meterMap = meterMap;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public BigDecimal getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	// public ReqToken getToken() {
	// return token;
	// }
	//
	// public void setToken(ReqToken token) {
	// this.token = token;
	// }

}
