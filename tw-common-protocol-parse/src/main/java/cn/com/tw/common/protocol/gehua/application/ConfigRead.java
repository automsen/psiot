package cn.com.tw.common.protocol.gehua.application;

import java.math.BigInteger;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.exception.code.ProtocolCode;
import cn.com.tw.common.protocol.gehua.cons.RegisterAddr;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * 读参数
 * 
 * @author admin
 *
 */
public class ConfigRead {
	
	/**
	 * 下发
	 * @param params
	 * @return
	 */
	public static String send(Object... params) throws ProtocolException {
		// 首个参数
		String firstKey = (String)params[2];
		int addrFlag = 0;
		String dataField = "";
		for (int i = 0; i < RegisterAddr.AddArray.length; i++) {
			if (firstKey.toUpperCase().equals(RegisterAddr.AddArray[i])) {
				dataField += RegisterAddr.AddArray[i];
				addrFlag = i;
				break;
			}
		}
		if (dataField.equals("")){
			throw new ProtocolException(ProtocolCode.PARAM_ILLEGAL,"unknown param key");
		}
		// 参数个数
		String paramNumStr = (String)params[3];
		int paramNum = Integer.parseInt(paramNumStr);
		int paramLength = 0;
		for (int i = 0; i<paramNum;i++){
			String tempFormat = RegisterAddr.formatArray[addrFlag+i];
			// 判断格式是否为2进制
			int isBit = tempFormat.indexOf("b");
			if (isBit >= 0) {
				tempFormat = tempFormat.substring(1);
			}
			// 判断格式是否为16进制
			int isHex = tempFormat.indexOf("h");
			if (isHex >= 0) {
				tempFormat = tempFormat.substring(1);
			}
			paramLength += Integer.valueOf(tempFormat);
		}
		dataField += ByteUtils.strAddZero(Integer.toHexString(paramLength/2),2);
		return dataField;
	}

	/**
	 * 应答
	 * 
	 * @param param
	 * @return
	 */
	public static Dlt645Data receive(Dlt645Data param) {
		String code = param.getDataField();
		int flag = 4;
		// 是否成功
		String result = code.substring(flag, flag + 2);
		flag = flag + 2;
		// 成功
		if (result.equals("00")) {
			param.setSuccess(true);
			// 起始
			int addrFlag = 0;
			String dataValues = "";
			// 起始地址
			String startAddr = code.substring(flag, flag += 2);
			// 字符数(字节数*2)
			int dataLength = Integer.parseInt(code.substring(flag, flag += 2), 16) * 2;
			for (int i = 0; i < RegisterAddr.AddArray.length; i++) {
				if (startAddr.toUpperCase().equals(RegisterAddr.AddArray[i])) {
					addrFlag = i;
					break;
				}
			}
			//
			for (; flag < code.length(); addrFlag++) {
				String tempFormat = RegisterAddr.formatArray[addrFlag];
				// 判断格式是否为2进制
				int isBit = tempFormat.indexOf("b");
				if (isBit >= 0) {
					tempFormat = tempFormat.substring(1);
				}
				// 判断格式是否为16进制
				int isHex = tempFormat.indexOf("h");
				if (isHex >= 0) {
					tempFormat = tempFormat.substring(1);
				}
				int itemLength = Integer.valueOf(tempFormat);
				String temp = code.substring(flag, flag += itemLength);
				temp = ByteUtils.inverted(temp);
				if (isBit >= 0) {
					temp = ByteUtils.toByteStr(temp).substring(6, 8);
				}
				if (isHex >= 0) {
					BigInteger big = new BigInteger(temp.trim(),16);
					temp = String.valueOf(big);
				}
				dataValues += ",\"" + RegisterAddr.KeyArray[addrFlag] + "\":\"" + temp + "\"";
			}
			dataValues = "{" + dataValues.substring(1) + "}";
			param.setDataValues(dataValues);
		}
		// 失败
		else {
			param.setSuccess(false);
			param.setErrCode(result);
		}
		return param;
	}
}