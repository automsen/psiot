package cn.com.tw.engine.core.utils.cons;


public class Cons {
	
	public final static int OPEN = 0;
	
	public final static int CLOSE = 1;
	
	/**
	 * 收集指令级别
	 */
	public final static int LVL_CMD_COLLECT = 10;
	
	/**
	 * 控制质量级别
	 */
	public final static int LVL_CMD_CONTROL = 20;
	/**
	 * 645-07标准
	 */
	public final static String P645_2007_BASE = "0401";
	
	/**
	 * 645-07阿迪克扩展
	 */
	public final static String P645_2007_ADIKE = "0403";

	
	/**
	 * 电表
	 */
	public final static String ELEC_METER_TYPE = "0301";
	
	/**
	 * 冷水表
	 */
	public final static String WATER_METER_TYPE = "0302";
	
	/**
	 * 透传采集器
	 */
	public final static String COLLECT_GW_TYPE = "0901";
	
	/**
	 * 3g 4G网关 0902
	 */
	public final static String COLLECT_GW_3G4G = "0902";
	
	/** cache config **/
	public final static String CACHE_NAME = "local";
	
	public final static String CACHE_DATAITEMS_METER_KEY = "cache:dataItems:meter";

}
