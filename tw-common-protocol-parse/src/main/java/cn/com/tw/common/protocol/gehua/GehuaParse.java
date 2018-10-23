package cn.com.tw.common.protocol.gehua;

import cn.com.tw.common.protocol.ParseEnum;
import cn.com.tw.common.protocol.ParseOpera;
import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.exception.code.ProtocolCode;
import cn.com.tw.common.protocol.gehua.application.ConfigRead;
import cn.com.tw.common.protocol.gehua.application.ConfigWrite;
import cn.com.tw.common.protocol.gehua.application.ExtendRead;
import cn.com.tw.common.protocol.gehua.application.ExtendWrite;
import cn.com.tw.common.protocol.gehua.application.Heartbeat;
import cn.com.tw.common.protocol.gehua.application.Monitor;
import cn.com.tw.common.protocol.gehua.application.Warning;
import cn.com.tw.common.protocol.utils.ByteUtils;
/**
 * 歌华独占协议
 * 无应答数据：上行：控制字+数据标识+数据
 * 有应答数据(未实现)：下行：控制字+数据标识+寄存器起始地址+数据字节数+数据
 * 		         上行：控制字+数据标识+执行结果+寄存器起始地址+数据字节数+数据
 * 扩展数据(未实现)：控制字+数据标识+厂家id+数据
 * @author admin
 *
 */
public class GehuaParse implements ParseOpera  {

	@Override
	public byte[] encode(Object... params) {
		if (params.length < 3) {
			throw new ProtocolException(ProtocolCode.PARAM_MISSING, "param missing");
		}
		// 控制字
		String cotrolWord = (String)params[0];
		// 数据标识
		String dataMarker = (String)params[1];
		if (cotrolWord.length() > 2||dataMarker.length()>2) {
			throw new ProtocolException(ProtocolCode.PROTOCOL_VALID, "cotrolWord or dataMarker length too long");
		}
		String dataFeild = "";
		// 写参数
		if ((cotrolWord+dataMarker).equals("C201")){
			dataFeild = ConfigWrite.send(params);
		}
		// 读参数
		else if ((cotrolWord+dataMarker).equals("8201")){
			dataFeild = ConfigRead.send(params);
		}
		// 扩展写
		else if ((cotrolWord+dataMarker).equals("D055")){
			dataFeild = ExtendWrite.send(params);
		}
		// 扩展读
		else if ((cotrolWord+dataMarker).equals("9055")){
			dataFeild = ExtendRead.send(params);
		}
		else {
			throw new ProtocolException(ProtocolCode.UNKNOWN_DATAMARKER, "unknown cotrolWord or dataMarker");
		}
		String code = (cotrolWord + dataMarker + dataFeild).toUpperCase();
		// 输出结果
		System.out.println("编码完成\ncode=" + code);
		return ByteUtils.toByteArray(code);
	}

	/**
	 * 上行解码
	 */
	@Override
	public Object decode(byte[] bytes) {
		// 原文
		String code = ByteUtils.bytes2hex(bytes).toUpperCase();
		// 控制字
		String cotrolWord = code.substring(0, 2);
		// 数据标识
		String dataMarker = code.substring(2, 4);
		Dlt645Data result = new Dlt645Data();
		result.setProtocolType(ParseEnum.GEHUA);
		result.setDataField(code);
		result.setCotrolCode(cotrolWord);
		result.setDataMarker(dataMarker);
		// 监测数据上报
		if ((cotrolWord+dataMarker).equals("2102")){
			result = Monitor.receive(result);
		}
		// 心跳上报
		else if ((cotrolWord+dataMarker).equals("2103")){
			result = Heartbeat.receive(result);
		}
		// 告警上报
		else if ((cotrolWord+dataMarker).equals("2104")){
			result = Warning.receive(result);
		}
		// 写参数应答
		else if ((cotrolWord+dataMarker).equals("6201")||(cotrolWord+dataMarker).equals("4201")){
			result = ConfigWrite.receive(result);
		}
		// 读参数应答
		else if ((cotrolWord+dataMarker).equals("2201")||(cotrolWord+dataMarker).equals("0201")){
			result = ConfigRead.receive(result);
		}
		// 扩展写应答
		else if ((cotrolWord+dataMarker).equals("7055")||(cotrolWord+dataMarker).equals("5055")){
			result = ExtendWrite.receive(result);
		}
		// 扩展读应答
		else if ((cotrolWord+dataMarker).equals("3055")||(cotrolWord+dataMarker).equals("1055")){
			result = ExtendRead.receive(result);
		}
		System.out.println("解码完成 \n" + result.toString());
		return result;
	}

}
