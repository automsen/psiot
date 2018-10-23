package cn.com.tw.paas.monit.service.dlt645.idl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.paas.monit.entity.db.command.BaseCmdEXE;
import cn.com.tw.paas.monit.entity.db.command.BaseInnEXE;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;

import com.alibaba.fastjson.JSON;

/**
 * 快速生成各种指令
 * @author liming
 * 2017年9月15日21:26:13
 *
 */
public class CmdInnEXEUtil {

    
	/**
	 * <pre>
	 *  00000000	组合有功总电能	1
	 *	03330200	购电次数	1
	 *	00900100	剩余电量	1
	 *	00900200	剩余金额	1
	 *	02030000	瞬时总有功功率	1
	 *	02040000	瞬时总无功功率	1
	 *	02010100	电压A	1
	 *	02020100	电流A	1
	 * </pre>
	 * 不加密常规读操作类指令
	 * @param cmd       组成的命令
	 * @param priceEnum 枚举
	 * @param meter		仪表信息
	 * @param groupSort 指令排序
	 * @param status	执行状态
	 * @return
	 */
	public static BaseInnEXE noEncryptReadInnExe(BaseCmdEXE cmd,PriceEnum priceEnum,TerminalEquip meter,Integer groupSort,Byte status){
		String content = cmd.getMeterAddr()+","+priceEnum.getControlCode()+","+priceEnum.getIdentity();
		return initInnEXE(cmd, priceEnum, meter, groupSort,
				status, content);
	}


	private static BaseInnEXE initInnEXE(BaseCmdEXE cmd, PriceEnum priceEnum,
			TerminalEquip meter, Integer groupSort, Byte status, String content) {
		BaseInnEXE baseInnEXE = new BaseInnEXE();
		baseInnEXE.setInnId(CommUtils.getUuid());
		//组成各种命令和指令
		Map<String,String> requestMap = new HashMap<String,String>();
		// 判断网关类型，仪表状态
//		if(GatewayTypeEum.RS485_ETHERNET1.getValue().equals(meter.getNetTypeCode())){
//			requestMap.put("gwId", meter.getNetEquipNumber());
//		}else if(GatewayTypeEum.LORAWAN.getValue().equals(meter.getNetTypeCode()) ||
//				GatewayTypeEum.NB_IoT.getValue().equals(meter.getNetTypeCode())
//				){
//			requestMap.put("gwId", meter.getNetEquipNumber());
//		}else if(GatewayTypeEum.RS485_GPRS1.getValue().equals(meter.getNetTypeCode()) ||
//				GatewayTypeEum.RS485_GPRS2.getValue().equals(meter.getNetTypeCode()) ||
//				GatewayTypeEum.RS485_ETHERNET2.getValue().equals(meter.getNetTypeCode())
//				){
//			requestMap.put("gwId", meter.getNetEquipNumber());
//		}else{
//			requestMap.put("gwId", meter.getEquipId());
//		}
		requestMap.put("meterType", meter.getEquipTypeCode());
		requestMap.put("gatewayType", meter.getNetTypeCode());
		requestMap.put("cmdLvl", cmd.getCmdLevel());
		requestMap.put("meterAddr", cmd.getMeterAddr());
	
		requestMap.put("content", content);
		baseInnEXE.setCmdId(cmd.getCmdId());
		baseInnEXE.setCmdCode(priceEnum.getControlCode());
		baseInnEXE.setInnCode(priceEnum.getIdentity());
		baseInnEXE.setCommand(priceEnum);
		baseInnEXE.setCreateTime(new Date());
		baseInnEXE.setGroupSort(groupSort); //第一条指令
		baseInnEXE.setMeterAddr(cmd.getMeterAddr());
		baseInnEXE.setMeterId(cmd.getMeterId());
		baseInnEXE.setParam(JSON.toJSONString(requestMap));
		baseInnEXE.setStatus(status);  //0创建；1成功；2失败；3超时；10正在处理
		return baseInnEXE;
	}
	
	
	/**
	 *  加密常规写操作类指令
	 * <pre>
	 * 仪表地址	控制码	数据标识	  仪表密码	参数(1~n个)
	 *	参数		固定为03		参数		参数		参数
	 *	04001001	写报警金额1限值	1	nnnnnn.nn
	 *	04001002	写报警金额2限值	1	nnnnnn.nn
	 *	04001003	写透支金额限值	1	nnnnnn.nn
	 *	040501FF	写第一套尖峰平谷	4	nnnn.nnnn*4
	 *	04050101	写第一套费率1(尖)	1	nnnn.nnnn
	 *	04050102	写第一套费率1(峰)	1	nnnn.nnnn
	 *	04050103	写第一套费率1(平)	1	nnnn.nnnn
	 *	04050104	写第一套费率1(谷)	1	nnnn.nnnn
	 *	04000207	写阶梯数	1	nn
	 *	040600FF	写第一套阶梯值	4	nnnnnn.nn*4
	 *	04060001	写第一套第1阶梯值	1	nnnnnn.nn
	 *	04060002	写第一套第2阶梯值	1	nnnnnn.nn
	 *	04060003	写第一套第3阶梯值	1	nnnnnn.nn
	 *	040601FF	写第一套阶梯电价	5	nnnn.nnnn*5
	 *	04060101	写第一套阶梯电价1	1	nnnn.nnnn
	 *	04060102	写第一套阶梯电价2	1	nnnn.nnnn
	 *	04060103	写第一套阶梯电价3	1	nnnn.nnnn
	 *	04060104	写第一套阶梯电价4	1	nnnn.nnnn
	 *</pre>
	 * @param cmd       组成的命令
	 * @param priceEnum 枚举
	 * @param meter		仪表信息
	 * @param groupSort 指令排序
	 * @param status	执行状态
	 * @return
	 */
	public static BaseInnEXE encryptWriteInnExe(BaseCmdEXE cmd,PriceEnum priceEnum,TerminalEquip meter,Integer groupSort,Byte status,String...params){
		if(params == null){
			return null;
		}
		StringBuffer sdf = new StringBuffer();
		for (String string : params) {
			sdf.append(",").append(string);
		}
		String content = cmd.getMeterAddr()+","+priceEnum.getControlCode()+","+priceEnum.getIdentity()+","+meter.getCommPwd()+sdf.toString();
		return initInnEXE(cmd, priceEnum, meter, groupSort,
				status, content);
	}
	
	
	
	/**
	 *  非加密写入
	 * @param cmd
	 * @param priceEnum
	 * @param meter
	 * @param groupSort
	 * @param status
	 * @param params
	 * @return
	 */
	public static BaseInnEXE noEncryptWriteInnExe(BaseCmdEXE cmd,PriceEnum priceEnum,TerminalEquip meter,Integer groupSort,Byte status,String...params){
		if(params == null){
			return null;
		}
		StringBuffer sdf = new StringBuffer();
		for (String string : params) {
			sdf.append(",").append(string);
		}
		String content = cmd.getMeterAddr()+","+priceEnum.getControlCode()+","+priceEnum.getIdentity()+sdf.toString();
		return initInnEXE(cmd, priceEnum, meter, groupSort,
				status, content);
	}
	
	/**
	 * 
	 * 不加密控制类指令
	 * <pre>
	 * 示例：“content”:“000000200508, 1C,     00000002  ,1A”
	 * 					仪表地址		控制码	仪表密码	操作类型
	 *					参数		固定为1C		参数		参数
	 *	
		
		常用操作类型
		
		操作类型	含义
		1C	通电
		1A	断电
		3A	保电
		3B	保电解除
		4A	断水
		4C	通水
		
	 * 
	 *</pre>
	 * @param cmd       组成的命令
	 * @param innCmd    操作类型
	 * @param meter		仪表信息
	 * @param groupSort 指令排序
	 * @param status	执行状态
	 * @return
	 */
	public static BaseInnEXE noEncryptContrlInnExe(BaseCmdEXE cmd,PriceEnum enums,TerminalEquip meter,Integer groupSort,Byte status){
		String content = cmd.getMeterAddr()+","+enums.getControlCode()+","+meter.getCommPwd()+","+enums.getIdentity();
		return initInnEXE(cmd, enums, meter, groupSort,
				status, content);
	}
	
	/**
	 * 给网关绑定仪表
	 * 
	 * @param cmd
	 * @param enums
	 * @param meter
	 * @param groupSort
	 * @param status
	 * @param equipmentExpands 设备集合
	 * @return
	 */
	public static BaseInnEXE connectGatewayInnExe(BaseCmdEXE cmd,PriceEnum enums,TerminalEquip meter,Integer groupSort,Byte status,String[] equipmentExpands){
		String content = cmd.getMeterAddr()+","+enums.getControlCode()+","+enums.getIdentity()+","+meter.getCommPwd()+","+equipmentExpands.length;
		meter.setEquipId(meter.getNetEquipNumber());
		for (String commAddr : equipmentExpands) {
			if(!StringUtils.isEmpty(commAddr)){
				content = content + "," + commAddr + ",1";
			}
		}
		
		return initInnEXE(cmd, enums, meter, groupSort,
				status, content);
	}
	
	/**
	 * 读取采集器下仪表
	 * @param cmd
	 * @param enums
	 * @param meter
	 * @param groupSort
	 * @param status
	 * @return
	 */
	public static BaseInnEXE gatewayMeterGetInnExe(BaseCmdEXE cmd,PriceEnum enums,TerminalEquip meter,Integer groupSort,Byte status){
		meter.setEquipId(meter.getNetEquipNumber());
		String content = cmd.getMeterAddr()+","+enums.getControlCode()+","+enums.getIdentity();
		return initInnEXE(cmd, enums, meter, groupSort,
				status, content);
	}
	
	/**
	 * 数据项下发
	 * @param cmd
	 * @param enums
	 * @param meter
	 * @param groupSort
	 * @param status
	 * @param powerdatas
	 * @return
	 */
	public static BaseInnEXE sendingDataSetInnExe(BaseCmdEXE cmd,PriceEnum enums,TerminalEquip meter,Integer groupSort,Byte status,String [] powerdatas){
		meter.setEquipId(meter.getNetEquipNumber());
		String content = cmd.getMeterAddr()+","+enums.getControlCode()+","+enums.getIdentity()+","+meter.getCommPwd()+","+powerdatas.length;
		for (String command : powerdatas) {
			content = content + "," + command;
		}
		return initInnEXE(cmd, enums, meter, groupSort,
				status, content);
	}
	
	
	/**
	 * 
	 * 加密控制类指令
	 * <pre>
	 * 加密控制类指令

		content格式
		
		仪表地址	控制码	数据标识	仪表密码
		参数		固定为03	参数	参数
		示例：“content”:“000000200508,03,ABABABAA,00000002”
		
		常用数据标识
		
		数据标识	含义
		ABABABAA	通电
		ABABABBB	断电
		ABABABEE	强制通电并保持
		ABABABDD	强制断电并保持
		ABABABFF	解除强制状态
	 * 
	 *</pre>
	 * @param cmd       组成的命令
	 * @param innCmd    操作类型         请查看不加密的操作类型
	 * @param meter		仪表信息
	 * @param groupSort 指令排序
	 * @param status	执行状态
	 * @return
	 */
	public static BaseInnEXE encryptContrlInnExe(BaseCmdEXE cmd,PriceEnum priceEnum,TerminalEquip meter,Integer groupSort,Byte status){
		String content =  cmd.getMeterAddr()+","+priceEnum.getControlCode()+","+priceEnum.getIdentity()+","+meter.getCommPwd();
		return initInnEXE(cmd, priceEnum, meter, groupSort,
				status, content);
	}
	
	
	
	
	/**
	 *  购电类指令
	 *  <pre>
	 *  content格式

		仪表地址	控制码	数据标识	金额	充值次数
		参数	固定为03	参数	参数	参数
		示例：“content”:“000000211931,03,070102FF,20,2”
		
		常用数据标识
		
		数据标识	含义
		070101FF	首次充值
		070102FF	充值
		070103FF	余额清0
		返回数据dataItems内容
		
		当前剩余金额	当前透支金额	表内已购电次数	本次金额变动	表内累计电量
		示例：“dataItems”: “009959.90,000000.00,00000003,000020.00,000000.10”
		</pre>
	 * @param cmd
	 * @param priceEnum
	 * @param meter
	 * @param groupSort
	 * @param status   
	 * @param payMoney 充值金额
	 * @param payNum   充值次数
	 * 
	 * 	 * @return
	 */
	public static BaseInnEXE rechargeInnExe(BaseCmdEXE cmd,PriceEnum priceEnum,TerminalEquip meter,
			Integer groupSort,Byte status,BigDecimal payMoney,int payNum){
		//设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)   
		String payMoneys = "0";
		if(payMoney.compareTo( BigDecimal.ZERO) > 0 ){
			payMoney=payMoney.setScale(2, BigDecimal.ROUND_HALF_UP); 
			payMoneys = payMoney.toString();
		}
		String content =  cmd.getMeterAddr()+","+priceEnum.getControlCode()+","+priceEnum.getIdentity()
				+","+payMoneys
				+","+payNum;
		return initInnEXE(cmd, priceEnum, meter, groupSort,
				status, content);
	}


	/**
	 * 采集频率配置
	 * @param commandExe
	 * @param gatherhz
	 * @param orgEquipment
	 * @param i
	 * @param byte1
	 * @param time
	 * @return
	 */
	public static BaseInnEXE gatherHzSetInnExe(BaseCmdEXE cmd,PriceEnum enums, TerminalEquip meter, Integer groupSort,
			Byte status, String time) {
		meter.setEquipId(meter.getNetEquipNumber());
		String content = cmd.getMeterAddr()+","+enums.getControlCode()+","+enums.getIdentity()+","+meter.getCommPwd()+","+time;
		return initInnEXE(cmd, enums, meter, groupSort,
				status, content);
	}


	/**
	 * 读写采集指令读取
	 * @param cmd
	 * @param enums
	 * @param meter
	 * @param groupSort
	 * @param status
	 * @return
	 */
	public static BaseInnEXE getDataItemInnExe(BaseCmdEXE cmd,PriceEnum enums, TerminalEquip meter, Integer groupSort,
			Byte status) {
		meter.setEquipId(meter.getNetEquipNumber());
		String content = cmd.getMeterAddr()+","+enums.getControlCode()+","+enums.getIdentity();
		return initInnEXE(cmd, enums, meter, groupSort,
				status, content);
	}


	/**
	 * 读取上传周期
	 * @param commandExe
	 * @param getgatherhz
	 * @param orgEquipment
	 * @param i
	 * @param byte1
	 * @return
	 */
	public static BaseInnEXE getGatherHzInnExe(BaseCmdEXE cmd,PriceEnum enums, TerminalEquip meter, Integer groupSort,
			Byte status) {
		meter.setEquipId(meter.getNetEquipNumber());
		String content = cmd.getMeterAddr()+","+enums.getControlCode()+","+enums.getIdentity();
		return initInnEXE(cmd, enums, meter, groupSort,
				status, content);
	}
}
