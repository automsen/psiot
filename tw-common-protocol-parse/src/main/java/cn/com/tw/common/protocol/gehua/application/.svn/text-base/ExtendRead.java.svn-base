package cn.com.tw.common.protocol.gehua.application;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.exception.code.ProtocolCode;
import cn.com.tw.common.protocol.gehua.cons.ExtendInsFormat;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * 读参数
 * 
 * @author admin
 *
 */
public class ExtendRead {

	/**
	 * 下发
	 * 
	 * @param params
	 * @return
	 */
	public static String send(Object... params) throws ProtocolException {
		// 厂家id
		String factoryId = (String) params[2];
		// 数据标识
		String dataMark645 = (String) params[3];
		String dataField = factoryId + "00" + ByteUtils.inverted(dataMark645);
		if (dataMark645.equals("04700401") || dataMark645.equals("04700406")) {
			// 起始序号
			String startIndex = (String) params[4];
			dataField += ByteUtils.strAddZero(startIndex, 2);
			// 任务个数
			String taskNumStr = (String) params[5];
			int taskNum = Integer.parseInt(taskNumStr);
			dataField += ByteUtils.strAddZero(Integer.toHexString(taskNum), 2);
		}
		return dataField;
	}

	/**
	 * 应答
	 * 
	 * @param param
	 * @return
	 */
	public static Dlt645Data receive(Dlt645Data param) throws ProtocolException {
		String code = param.getDataField();
		int flag = 4;
		// 是否成功
		String result = code.substring(flag, flag += 2);
		// 成功
		if (result.equals("00")) {
			param.setSuccess(true);
			String dataValues = "";
			// 厂家id
			String factoryId = code.substring(flag, flag += 2);
			dataValues += "{\"factoryId\":\"" + factoryId + "\"";
			// 保留位
			flag = flag + 2;
			// 数据标识
			String dataMark645 = ByteUtils.inverted(code.substring(flag, flag += 8));
			dataValues += ",\"dataMark645\":\"" + dataMark645 + "\"";
			// 任务单详情
			if (dataMark645.equals("04700401") || dataMark645.equals("04700406")) {
				String startIndex = code.substring(flag, flag += 2);
				dataValues += ",\"startIndex\":\"" + startIndex + "\"";
				String taskNumStr = code.substring(flag, flag += 2);
				int taskNum = Integer.parseInt(taskNumStr, 16);
				dataValues += ",\"taskNum\":\"" + taskNum + "\"";
				for (int i = 0; i < taskNum; i++) {
					String eid = code.substring(flag, flag += 2);
					String cmd = ByteUtils.inverted(code.substring(flag, flag += 8));
					String length = code.substring(flag, flag += 2);
					String temp = code.substring(flag, flag += 2);
					dataValues += ",\"task" + (i + 1) + "\":\"" + eid + " " + cmd + " " + length + " " + temp + "\"";
				}
			} else {
				String keyStr = ExtendInsFormat.keyArray.get(dataMark645);
				String formatStr = ExtendInsFormat.formatArray.get(dataMark645);
				if (null == keyStr || null == formatStr) {
					throw new ProtocolException(ProtocolCode.UNKNOWN_DATAMARKER, "unknown 645 dataMark");
				}
				// 获取每个数据项的格式
				String[] keyArray = keyStr.split(",");
				String[] formatArray = formatStr.split(",");
				for (int i = 0; i < keyArray.length; i++) {
					String tempKey = keyArray[i];
					String tempFormat = formatArray[i];
					// 数据项总长度与小数位长度
					int itemLength = 0, decimalLength = 0;
					// 数据项
					String item = "";
					// 判断格式是否包含符号位
					int plusOrMinus = tempFormat.indexOf("±");
					if (plusOrMinus >= 0) {
						tempFormat = tempFormat.substring(1);
					}
					// 判断格式是否为16进制
					int isHex = tempFormat.indexOf("h");
					if (isHex >= 0) {
						tempFormat = tempFormat.substring(1);
					}
					// 判断格式是否为2进制
					int isBit = tempFormat.indexOf("b");
					if (isBit >= 0) {
						tempFormat = tempFormat.substring(1);
					}
					// 判断格式是否包含小数位
					int pointIndex = tempFormat.indexOf(".");
					if (pointIndex > 0) {
						itemLength = Integer.valueOf(tempFormat.substring(0, pointIndex));
						decimalLength = Integer.valueOf(tempFormat.substring(pointIndex + 1));
					} else {
						itemLength = Integer.valueOf(tempFormat);
					}
					// 截取
//					item = ByteUtils.inverted(code.substring(flag, flag += itemLength));
					item = code.substring(flag, flag += itemLength);
					// 需返回二进制字符串
					if (isBit >= 0) {
						item = ByteUtils.toByteStr(item);
					} else {
						String first = item.substring(0, 1);
						if (first.equals("f")) {
							// f时表示仪表内对应数据为null值
						}
						// 需10进制转16进制
						else if (isHex >= 0) {
							item = String.valueOf(Integer.parseInt(item, 16));
						}
						// 包含符号位
						else if (plusOrMinus >= 0) {
							// 首位大于等于8为负数
							if (Integer.valueOf(first) >= 8) {
								item = "-" + String.valueOf(Integer.valueOf(first) - 8) + item.substring(1, itemLength);
							}
						}
						// 含小数位
						if (pointIndex >= 0 && decimalLength > 0) {
							// 插入小数点
							item = ByteUtils.insertPoint(item, decimalLength);
						}
					}
					dataValues += ",\"" + tempKey + "\":\"" + item + "\"";
				}
			}

			dataValues += "}";
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