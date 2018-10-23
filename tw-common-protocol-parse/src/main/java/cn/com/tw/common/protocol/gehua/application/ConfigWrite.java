package cn.com.tw.common.protocol.gehua.application;

import java.math.BigInteger;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.exception.code.ProtocolCode;
import cn.com.tw.common.protocol.gehua.cons.RegisterAddr;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * 写参数
 * @author admin
 *
 */
public class ConfigWrite {
	
	/**
	 * 下发
	 * @param params
	 * @return
	 */
	public static String send(Object... params){
		// 首个参数
		String firstKey = (String)params[2];
		int addrFlag = 0;
		String dataField = "";
		String paramField = "";
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
		int paramNum = params.length - 3;
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
			int itemLength = Integer.valueOf(tempFormat);
			String itemStr = (String)params[3+i];
			if (isBit >= 0) {
				itemStr = String.valueOf(Integer.parseInt(itemStr,2));
			}
			if (isHex >= 0) {
				BigInteger b = new BigInteger(itemStr,10);
				itemStr = b.toString(16);
			}
			paramField += ByteUtils.inverted(ByteUtils.strAddZero(itemStr,itemLength));
			paramLength += itemLength;
		}
		dataField += ByteUtils.strAddZero(Integer.toHexString(paramLength/2),2) + paramField;
		return dataField;
	}
	
	/**
	 * 应答
	 * @param param
	 * @return
	 */
	public static Dlt645Data receive(Dlt645Data param) {
		String code = param.getDataField();
		int flag = 4;
		// 是否成功
		String result = code.substring(flag, flag += 2);
		// 成功
		if (result.equals("00")){
			param.setSuccess(true);
		}
		// 失败
		else {
			param.setSuccess(false);
			param.setErrCode(result);
		}
		return param;
	}
}
