package cn.com.tw.common.protocol.gehua.application;

import java.math.BigDecimal;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.exception.code.ProtocolCode;
import cn.com.tw.common.protocol.gehua.cons.ExtendInsFormat;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * 写参数
 * 
 * @author admin
 *
 */
public class ExtendWrite {

	/**
	 * 下发
	 * 
	 * @param params
	 * @return
	 */
	public static String send(Object... params) {
		// 厂家id
		String factoryId = (String) params[2];
		// 数据标识
		String dataMark645 = (String) params[3];
		String dataField = factoryId + "00" + ByteUtils.inverted(dataMark645);
		if (dataMark645.equals("04700402") || dataMark645.equals("04700407")) {
			// 任务个数
			String taskNumStr = (String) params[4];
			int taskNum = Integer.parseInt(taskNumStr);
			dataField += ByteUtils.strAddZero(Integer.toHexString(taskNum), 2);
			for (int i = 0; i < taskNum; i++) {
				String eid = (String) params[5 + i * 4];
				dataField += ByteUtils.strAddZero(eid, 2);
				String cmd = (String) params[6 + i * 4];
				dataField += ByteUtils.inverted(ByteUtils.strAddZero(cmd, 8));
				String length = (String) params[7 + i * 4];
				dataField += ByteUtils.strAddZero(length, 2);
				// 16进制原文
				String temp = (String) params[8 + i * 4];
				dataField += ByteUtils.strAddZero(temp, 2);
			}
		} else {
			String formatStr = ExtendInsFormat.formatArray.get(dataMark645);
			// 获取每个数据项的格式
			String[] formatArray = formatStr.split(",");
			// 格式包含的数据项数量
			int formatNum = formatArray.length;
			// 参数数量
			int paramNum = params.length - 4;
			// 参数不匹配
			if (formatNum != paramNum) {
				throw new ProtocolException(ProtocolCode.PARAM_MISSING, "param lose");
			}
			// 用于标记dataValues被读取到第几位
			// int flag=0;
			// 将每一个数据项按格式编码
			for (int i = 0; i < paramNum; i++) {

				String tempFormat = formatArray[i];
				// 数据项
				String item = (String) params[i + 4];
				int itemValue;
				// 数据项总长度
				int itemLength;
				int isHex = tempFormat.indexOf("h");
				if (isHex >= 0) {
					tempFormat = tempFormat.substring(1);
				}
				// 判断格式是否包含小数位
				int pointIndex = tempFormat.indexOf(".");
				if (pointIndex > 0) {
					itemLength = Integer.valueOf(tempFormat.substring(0, pointIndex));
					// 小数位长度
					int decimalLength = Integer.valueOf(tempFormat.substring(pointIndex + 1));
					// 根据小数位数将原数据乘以10的decimalLength次方
					double multiple = Math.pow(10, decimalLength);
					BigDecimal bg = new BigDecimal(item);
					itemValue = bg.setScale(decimalLength, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(multiple))
							.intValue();
					item = String.valueOf(itemValue);
				} else {
					itemLength = Integer.valueOf(tempFormat);
				}
				if (isHex >= 0) {
					item = Integer.toHexString(Integer.parseInt(item));
				}
				// 补0
				item = ByteUtils.strAddZero(item, itemLength);
//				dataField += ByteUtils.inverted(item);
				dataField += item;
			}

		}
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
		}
		// 失败
		else {
			param.setSuccess(false);
			param.setErrCode(result);
		}
		return param;
	}
}
