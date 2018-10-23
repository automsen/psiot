package cn.com.tw.saas.serv.common.utils.cons;

import cn.com.tw.saas.serv.common.utils.EnumHelperUtil;
/**
 * paas设备分类
 * @author Cheng Qi Peng
 *
 */
public enum EnergyCategEum {
	/**
	 * 电表
	 */
	ELEC("110000"),
	/**
	 * 水表
	 */
	WATER("120000"),
	/**
	 * 天然气
	 */
	GAS("0203"),
	/**
	 * 热
	 */
	HEAT("0204");
	
	
	private String value;

	EnergyCategEum(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public EnergyCategEum getEnum(String value){
		return EnumHelperUtil.getByStringTypeCode(EnergyCategEum.class, "getValue", value);
	}
	
}
