package cn.com.tw.paas.monit.common.utils.cons;

import cn.com.tw.paas.monit.common.utils.EnumHelperUtil;
/**
 * 能源分类
 * @author Cheng Qi Peng
 *
 */
public enum EnergyCategEum {
	/**
	 * 电
	 */
	ELEC("110000"),
	/**
	 * 水
	 */
	WATER("120000"),
	/**
	 * 天然气
	 */
	GAS("130000"),
	/**
	 * 热
	 */
	HEAT("140000");
	
	
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
