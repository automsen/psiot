package cn.com.tw.common.protocol.gehua.application;

import java.math.BigInteger;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * 终端告警数据（只有上行）
 * 
 * @author admin
 *
 */
public class Warning {

	public static Dlt645Data receive(Dlt645Data param) {
		String code = param.getDataField();
		String dataValues = "{";
		int flag = 4;
		// 设备Id
		String eId = ByteUtils.inverted(code.substring(flag, flag += 8));
		BigInteger big = new BigInteger(eId.trim(),16);
		eId = String.valueOf(big);
		dataValues += "\"eId\":\"" + eId + "\"";

		// 告警时间
		String warnTime = ByteUtils.inverted(code.substring(flag, flag += 8));
		dataValues += ",\"WAT\":\"" + String.valueOf(Integer.parseInt(warnTime, 16)) + "\"";

		// 故障标识
		String warnFlag = code.substring(flag, flag += 2);
		dataValues += ",\"WAF\":\"" + warnFlag + "\"";

		// 有故障
		if (!warnFlag.equals("00")) {
			// 故障代码
			String warnCode = ByteUtils.inverted(code.substring(flag));
			dataValues += ",\"WAC\":\"" + warnCode + "\"";
		}
		// 无故障
		else {
		}
		dataValues += "}";
		param.setDataValues(dataValues);
		param.setSuccess(true);
		return param;
	}
}