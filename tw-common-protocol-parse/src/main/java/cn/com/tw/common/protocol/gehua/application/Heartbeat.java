package cn.com.tw.common.protocol.gehua.application;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * DTU心跳数据（只有上行）
 * @author admin
 *
 */
public class Heartbeat {
	
	public static Dlt645Data receive(Dlt645Data param) {
		String code = param.getDataField();
		String dataValues = "{";
		int flag = 4;
		// 工作模式
		String WMD = code.substring(flag, flag += 2);
		dataValues += "\"WMD\":\""+WMD+"\"";

		// DTU功能位
		String DFB = code.substring(flag, flag += 2);
		dataValues += ",\"DFB\":\""+DFB+"\"";

		// DTU波特率
		String Baud = code.substring(flag, flag += 2);
		dataValues += ",\"Baud\":\""+Baud+"\"";

		// 心跳间隔
		String HBI = ByteUtils.inverted(code.substring(flag, flag += 4));
		dataValues += ",\"HBI\":\""+Integer.parseInt(HBI, 16)+"\"";
		
		// 设备数量
		String ENUM = code.substring(flag, flag += 2);
		dataValues += ",\"ENUM\":\""+Integer.parseInt(ENUM, 16)+"\"";
		
		// 环境温度
		String ENT = temperatureTrans(code.substring(flag, flag += 4));
		dataValues += ",\"ENT\":\""+ENT+"\"";
		
		// 电池电量
		String battery = code.substring(flag, flag += 2);
		dataValues += ",\"BTY\":\""+Integer.parseInt(battery, 16)+"\"";
		
		// 故障代码
		String DWAC = code.substring(flag, flag += 2);
		dataValues += ",\"DWAC\":\""+DWAC+"\"";
		dataValues += "}";
		param.setDataValues(dataValues);
		param.setSuccess(true);
		return param;
	}
	
	private static String temperatureTrans(String temperature) {
		if (temperature.toUpperCase().equals("FFFF")) {
			return "";
		} else {
			temperature = ByteUtils.inverted(temperature);
			int temp = Integer.parseInt(temperature, 16) - 1000;
			return ByteUtils.insertPoint(String.valueOf(temp), 1);
		}
	}
}