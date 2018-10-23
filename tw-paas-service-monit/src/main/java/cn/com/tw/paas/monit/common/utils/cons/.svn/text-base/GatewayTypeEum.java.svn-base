package cn.com.tw.paas.monit.common.utils.cons;

import cn.com.tw.paas.monit.common.utils.EnumHelperUtil;
/**
 * 网关类型
 * @author  liming
 *
 */
@Deprecated
public enum GatewayTypeEum {
	/**
	 * RS485转以太网-I
	 */
	RS485_ETHERNET1("0901"),
	/**
	 * RS485转以太网-II
	 */
	RS485_ETHERNET2("0902"),
	/**
	 * RS485转GPRS-I
	 */
	RS485_GPRS1("0903"),
	/**
	 * RS485转GPRS-II
	 */
	RS485_GPRS2("0904"),
	
	/**
	 * LORAWAN
	 */
	LORAWAN("0905"),
	
	/**
	 * NB-IoT
	 */
	NB_IoT("0906"),
	
	/**
	 * 以太网直连
	 */
	ETHERNET("0907"),
	/**
	 * GPRS直连
	 */
	GPRS("0908"),
	/**
	 * NB-UDP直连
	 */
	NB_UDP("0909"),
	/**
	 * Wi/Fi直连
	 */
	Wi_Fi("0910");
	
	
	private String value;
	
	GatewayTypeEum(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public GatewayTypeEum getEnum(String value){
		return EnumHelperUtil.getByStringTypeCode(GatewayTypeEum.class, "getValue", value);
	}
	
}
