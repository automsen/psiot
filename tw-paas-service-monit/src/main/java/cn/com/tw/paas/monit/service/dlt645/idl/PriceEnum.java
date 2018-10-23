package cn.com.tw.paas.monit.service.dlt645.idl;


/**
 * 开户计价相关指令
 * @author admin
 *
 */
public enum PriceEnum {
	/**
	 * 读取电表当前止码
	 */
	readElecActual("11","00000000"),
	
	/**
	 * 读取电表止码数据块
	 */
	readElecActualBlock("11","0000FF00"),
	/**
	 * 读取电表状态
	 */
	readElecStatus("11","04000503"),
	
	/**
	 * 读取水表状态
	 */
	readWaterStatus("11","00004100"),
	
	/**
	 * 读取瞬时总有功功率
	 */
	readActivePower("11","0203FF00"),
	/**
	 * 读取电压A
	 */
	readVoltage("11","0201FF00"),
	/**
	 * 读取电流a
	 */
	readElectricity("11","0202FF00"),
	/**
	 * 读取水表当前止码
	 */
	readWaterActual("11","00004100"),
	
	/**
	 * 读取当前充值次数
	 */
	readpaynum("11","03330200"),
	/**
	 * 读取当前仪表剩余金额
	 */
	readbalance("11","00900200"),

	/**
	 * 读取已透支金额
	 */
	readoverdraft("11","00900201"),
	
	/**
	 * 写过载事件有功功率触发下限
	 */
	writeOverloadPower("14","04090B01"),
	
	/**
	 * 网关仪表绑定指令
	 */
	connectGatewayMeter("14","04700103"),
	
	/**
	 * 读取网关下绑定仪表指令
	 */
	gatewayMeterGet("11","04700103"),
	
	/**
	 * 数据项下发
	 */
	sendingDataSet("14","04700201"),
	
	/**
	 *数据项读取 
	 */
	getDataItem("11","04700201"),
	
	/**
	 * 采集频率
	 */
	gatherHz("14","04700102"),
	
	/**
	 * 读取采集频率
	 */
	getGatherHz("11","04700102"),
	
	/**
	 * 首冲
	 */
	firstrecharge("03","070101FF"),
	
	/**
	 * 一般充值
	 */
	normrecharge("03","070102FF"),
	/**
	 * 撤销上次充值
	 */
	cancelRecharge("03","070104FF"),
	/**
	 * 清0操作
	 */
	setZero("03","070103FF"),
	
	/**
	 * 预警指令  nnnnnn.nn
	 */
	writealarm1("03","04001001"),
	writealarm2("03","04001002"),
	/**
	 * 设置透支限额	  nnnnnn.nn	
	 */
	overdraw("03","04001003"),
	/**
	 * 写一套尖峰平谷  nnnn.nnnn*4
	 */
	qfpgset("03","040501FF"),

	/**
	 * 写阶梯数  nn
	 */
	laddernum("03","04000207"),
	/**
	 * 写阶梯值  nnnn.nnnn*4
	 */
	laddervalue("03","040600FF"),
	
	/**
	 * 写阶梯电价  nnnn.nnnn*4
	 */
	ladderprice("03","040601FF"),
	
	/**
	 * 不加密通电
	 */
	NoEncryptElecOn("1C","1B"),
	/**
	 * 不加密通电2
	 */
	NoEncryptElecOn2("1C","1C"),
	/**
	 * 不加密断电
	 */
	NoEncryptElecOff("1C","1A"),
	
	/**
	 * 不加密保电
	 */
	NoEncryptElecKeep("1C","3A"),
	
	/**
	 * 不加密保电解除
	 */
	NoEncryptElecKeepRelieve("1C","3B"),
	
	/**
	 * 不加密水表开闸
	 */
	NoEncryptWaterOn("1C","4C"),
	/**
	 * 不加密水表关闸
	 */
	NoEncryptWaterOff("1C","4A"),
	/**
	 * 不加密水表开闸
	 */
	EncryptWaterOn("1C","4C"),
	/**
	 * 不加密水表关闸
	 */
	EncryptWaterOff("1C","4A"),
	
	/**
	 * 加密解除绑定,断电
	 */
	EncryUnElecBind("03","ABABABFF"),
	EncryptElecOff("03","ABABABBB"),
	/**
	 * 加密开闸
	 */
	EncryptElecOn("03","ABABABAA"),
	/**
	 * 预警
	 */
	EncryptPriceAlarm01("03","04001001"),
	EncryptPriceAlarm02("03","04001002"),
	/**
	 * 透支
	 */
	EncryptPriceOverdraft("03","04001003")
	
	;
	private String controlCode;
	

	
	private String identity;
	
	
	
	 PriceEnum(String controlCode,String identity){
		this.setControlCode(controlCode);
		this.setIdentity(identity);
	}



	public String getControlCode() {
		return controlCode;
	}



	public void setControlCode(String controlCode) {
		this.controlCode = controlCode;
	}



	public String getIdentity() {
		return identity;
	}



	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
	
}
