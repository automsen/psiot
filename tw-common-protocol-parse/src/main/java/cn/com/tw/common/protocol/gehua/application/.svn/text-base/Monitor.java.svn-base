package cn.com.tw.common.protocol.gehua.application;

import java.math.BigInteger;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * 终端监测数据（只有上行）
 * 
 * @author admin
 *
 */
public class Monitor {

	public static Dlt645Data receive(Dlt645Data param) {
		String code = param.getDataField();
		String dataValues = "{";
		int flag = 4;
		if (code.length() == 42||code.length() == 54) {
			// 设备Id
			String eId = ByteUtils.inverted(code.substring(flag, flag += 8));
			BigInteger big = new BigInteger(eId.trim(),16);
			eId = String.valueOf(big);
			dataValues += "\"eId\":\"" + eId + "\",";
		}
		// 时间戳，秒
		String uTm = ByteUtils.inverted(code.substring(flag, flag += 8));
		if (uTm.toUpperCase().equals("FFFFFFFF")) {
			dataValues += "\"uTm\":\"\"";
		} else {
			dataValues += "\"uTm\":\"" + String.valueOf(Integer.parseInt(uTm, 16)) + "\"";
		}

		// 室内温度
		String IDT = temperatureTrans(code.substring(flag, flag += 4));
		dataValues += ",\"IDT\":\"" + IDT + "\"";

		// 室外温度
		String ODT = temperatureTrans(code.substring(flag, flag += 4));
		dataValues += ",\"ODT\":\"" + ODT + "\"";

		// 设定温度
		String STT = temperatureTrans(code.substring(flag, flag += 4));
		dataValues += ",\"STT\":\"" + STT + "\"";

		// 入水温度
		String IWT = temperatureTrans(code.substring(flag, flag += 4));
		dataValues += ",\"IWT\":\"" + IWT + "\"";

		// 出水温度
		String OWT = temperatureTrans(code.substring(flag, flag += 4));
		dataValues += ",\"OWT\":\"" + OWT + "\"";

		// 状态字
		String status = ByteUtils.toByteStr(code.substring(flag, flag += 2));
		// 开关机状态
		String IF = status.substring(0, 3);
		// 关
		if (IF.equals("000")) {
			IF = "001";
		}
		// 开
		else if (IF.equals("001")) {
			IF = "000";
		}
		dataValues += ",\"IF\":\"" + String.valueOf(Integer.parseInt(IF, 2)) + "\"";
		// 运行模式
		String runMode = status.substring(3, 6);
		dataValues += ",\"RMD\":\"" + runMode + "\"";

		// 是否包含水电数据
		boolean haveWater = false;
		if (runMode.charAt(1) == '1') {
			haveWater = true;
		}
		boolean haveElec = false;
		if (runMode.charAt(2) == '1') {
			haveElec = true;
		}
		if (flag >= code.length()) {
		} else {
			if (haveWater) {
				// 水读数
				String WF = ByteUtils.inverted(code.substring(flag, flag += 4));
				if (WF.toUpperCase().equals("FFFF")) {
					dataValues += ",\"WF\":\"\"";
				} else {
					dataValues += ",\"WF\":\"" + String.valueOf(Integer.parseInt(WF, 16)) + "\"";
				}
			}
			if (haveElec) {
				// 电读数
				String TAE = ByteUtils.inverted(code.substring(flag, flag += 8));
				if (TAE.toUpperCase().equals("FFFFFFFF")) {
					dataValues += ",\"TAE\":\"\"";
				} else {
					dataValues += ",\"TAE\":\"" + String.valueOf(Integer.parseInt(TAE, 16)) + "\"";
				}
			}

		}
		dataValues += "}";
		param.setDataValues(dataValues);
		param.setSuccess(true);
		return param;
	}

	/**
	 * 实际温度=（上传温度-1000）/10
	 * 
	 * @param temperature
	 * @return
	 */
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