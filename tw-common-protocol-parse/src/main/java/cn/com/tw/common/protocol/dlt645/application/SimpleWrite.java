package cn.com.tw.common.protocol.dlt645.application;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.dlt645.cons.InstructionFormat;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.exception.code.ProtocolCode;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * 不加密写数据
 * 
 * @author Cheng Qi Peng
 *
 */
public class SimpleWrite {

	private static HashMap<String, String> format = InstructionFormat.simpleFormatMap();
	/**
	 *  深圳事件上报专用编码
	 * @param params
	 * @return
	 * @throws ProtocolException
	 */
	public static String sendEvent(Object... params) throws ProtocolException {
		// 数据标识
		String dataMarker = (String) params[2];
		// 密码
		String password = (String) params[3];
		// 操作者编码(暂无相关需求，可为定长的任意内容)
		String operatorCode = "7e563412";
		// 数据域
		String dataField = ByteUtils.inverted(dataMarker) + ByteUtils.inverted(password) + operatorCode;
		// 主动上报模式字
		if (dataMarker.equals("04001104")) {
			String word = (String) params[4];
			if(word.length()!=64){
				throw new ProtocolException(ProtocolCode.PARAM_ILLEGAL,"param value illegal");
			}
			word = ByteUtils.toHexStr(word);
			dataField += ByteUtils.inverted(word);
			return dataField;
		} 
		// 复位主动上报状态字
		else if (dataMarker.equals("04001503")) {
			String word = (String) params[4];
			if(word.length()!=96){
				throw new ProtocolException(ProtocolCode.PARAM_ILLEGAL,"param value illegal");
			}
			word = ByteUtils.toHexStr(word);
			dataField += ByteUtils.inverted(word);
			return dataField;
		}
		return dataField;
	}
	
	/**
	 * 集中器主动上传配置专用编码
	 * @param params
	 * @return
	 * @throws ProtocolException
	 */
	public static String sendConcentrator(Object... params) throws ProtocolException {
		// 数据标识
		String dataMarker = (String) params[2];
		// 密码
		String password = (String) params[3];
		// 操作者编码(暂无相关需求，可为定长的任意内容)
		String operatorCode = "7e563412";
		// 数据域
		String dataField = ByteUtils.inverted(dataMarker) + ByteUtils.inverted(password) + operatorCode;
		// 写上传周期
		if (dataMarker.equals("04700102")) {
			int itemLength = 4;
			String cycle = (String) params[4];
			// 补0
			cycle = ByteUtils.strAddZero(cycle, itemLength);
			dataField += ByteUtils.inverted(cycle);
			return dataField;
		// 写表档案(无分区)
		} else if (dataMarker.equals("04700103")) {
			String itemNumStr = (String) params[4];
			int itemNum = Integer.valueOf(itemNumStr);
			itemNumStr = ByteUtils.strAddZero(itemNumStr, 2);
			dataField += ByteUtils.inverted(itemNumStr);
			for (int i = 0; i < itemNum; i++) {
				// 数据项
				String item1 = (String) params[5 + 2 * i];
				item1 = ByteUtils.strAddZero(item1, 12);
				dataField += ByteUtils.inverted(item1);
				String item2 = (String) params[6 + 2 * i];
				item2 = ByteUtils.strAddZero(item2, 2);
				dataField += ByteUtils.inverted(item2);
			}
			return dataField;
		// 新增/删除/全覆盖更新表档案(分区)
		} else if (dataMarker.equals("04700104")||dataMarker.equals("04700105")
				||dataMarker.equals("04700109")){
			// 档案区号(0,1~8)
			String areaCode = (String) params[4];
			areaCode = ByteUtils.strAddZero(areaCode, 2);
			dataField += ByteUtils.inverted(areaCode);
			// 档案起始序号
			String startIndexStr = (String) params[5];
			Integer startIndex = Integer.valueOf(startIndexStr);
			startIndexStr = Integer.toHexString(startIndex).toUpperCase();
			startIndexStr = ByteUtils.strAddZero(startIndexStr, 2);
			dataField += ByteUtils.inverted(startIndexStr);
			// 当前帧档案个数
			String itemNumStr = (String) params[6];
			Integer itemNum = Integer.valueOf(itemNumStr);
			itemNumStr = Integer.toHexString(itemNum).toUpperCase();
			itemNumStr = ByteUtils.strAddZero(itemNumStr, 2);
			dataField += ByteUtils.inverted(itemNumStr);
			for (int i = 0; i < itemNum; i++) {
				// 仪表地址
				String item1 = (String) params[7 + 2 * i];
				item1 = ByteUtils.strAddZero(item1, 12);
				dataField += ByteUtils.inverted(item1);
				// 分组号
				String item2 = (String) params[8 + 2 * i];
				item2 = ByteUtils.strAddZero(item2, 2);
				dataField += ByteUtils.inverted(item2);
			}
			return dataField;
		// 清空表档案（分区）
		} else if (dataMarker.equals("04700106")){
			// 档案区号(0,1~8)
			String areaCode = (String) params[4];
			areaCode = ByteUtils.strAddZero(areaCode, 2);
			dataField += ByteUtils.inverted(areaCode);
			dataField += "00000000";
			return dataField;
		// 指定覆盖更新表档案（分区）
		} else if (dataMarker.equals("0470010A")){
			// 档案区号(0,1~8)
			String areaCode = (String) params[4];
			areaCode = ByteUtils.strAddZero(areaCode, 2);
			dataField += ByteUtils.inverted(areaCode);
			// 档案起始序号
			String startIndexStr = (String) params[5];
			Integer startIndex = Integer.valueOf(startIndexStr);
			startIndexStr = Integer.toHexString(startIndex).toUpperCase();
			startIndexStr = ByteUtils.strAddZero(startIndexStr, 2);
			dataField += ByteUtils.inverted(startIndexStr);
			// 当前帧档案个数
			String itemNumStr = (String) params[6];
			Integer itemNum = Integer.valueOf(itemNumStr)*2;
			itemNumStr = Integer.toHexString(itemNum).toUpperCase();
			itemNumStr = ByteUtils.strAddZero(itemNumStr, 2);
			dataField += ByteUtils.inverted(itemNumStr);
			for (int i = 0; i < itemNum; i++) {
				// 仪表地址
				String item1 = (String) params[7 + 2 * i];
				item1 = ByteUtils.strAddZero(item1, 12);
				dataField += ByteUtils.inverted(item1);
				// 分组号
				String item2 = (String) params[8 + 2 * i];
				item2 = ByteUtils.strAddZero(item2, 2);
				dataField += ByteUtils.inverted(item2);
			}
			return dataField;
		}
		// 写采集指令
		else if (dataMarker.equals("04700201")||dataMarker.equals("04700301")) {
			String itemNumStr = (String) params[4];
			int itemNum = Integer.valueOf(itemNumStr);
			itemNumStr = ByteUtils.strAddZero(itemNumStr, 2);
			dataField += ByteUtils.inverted(itemNumStr);
			for (int i = 0; i < itemNum; i++) {
				// 数据项
				String item1 = (String) params[5 + i];
				item1 = ByteUtils.strAddZero(item1, 8);
				dataField += ByteUtils.inverted(item1);
			}
			return dataField;
		}
		// TODO 未定稿
		else if (dataMarker.equals("04700205")){
			// 分组号
			String groupNoStr = (String) params[4];
			groupNoStr = ByteUtils.strAddZero(groupNoStr, 2);
			dataField += ByteUtils.inverted(groupNoStr);
			String itemNumStr = (String) params[5];
			itemNumStr = ByteUtils.strAddZero(itemNumStr, 2);
			dataField += ByteUtils.inverted(itemNumStr);
			// 读取地址
			String memoryAddr = (String) params[6];
			memoryAddr = ByteUtils.strAddZero(memoryAddr, 8);
			dataField += ByteUtils.inverted(memoryAddr);
			// 数据长度
			String memoryLength = (String) params[7];
			memoryLength = ByteUtils.strAddZero(memoryLength, 8);
			dataField += ByteUtils.inverted(memoryLength);
			return dataField;
		}
		return dataField;
	}

	/**
	 * 下发编码
	 * 
	 * @return
	 */
	public static String send(Object... params) throws ProtocolException {
		// 数据标识
		String dataMarker = (String) params[2];
		// 密码
		String password = (String) params[3];
		// 操作者编码(暂无相关需求，可为定长的任意内容)
		String operatorCode = "7e563412";
		// 数据域
		String dataField = ByteUtils.inverted(dataMarker) + ByteUtils.inverted(password) + operatorCode;

		String thisformat = "";
		try {// 寻找数据标识对应的格式
			thisformat = format.get(dataMarker);
		} catch (Exception e) {
			throw new ProtocolException(ProtocolCode.UNKNOWN_DATAMARKER, "unknown datamarker");
		}
		// 没找到格式
		if (StringUtils.isEmpty(thisformat)) {
			throw new ProtocolException(ProtocolCode.UNKNOWN_DATAMARKER, "unknown datamarker");
		} else {
			// 获取每个数据项的格式
			String[] formatArray = thisformat.split(",");
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
				// 数据项
				String item = (String) params[i + 4];
				int itemValue;
				// 数据项总长度
				int itemLength;
				// 判断格式是否包含小数位
				int pointIndex = formatArray[i].indexOf(".");
				if (pointIndex > 0) {
					itemLength = Integer.valueOf(formatArray[i].substring(0, pointIndex));
					// 小数位长度
					int decimalLength = Integer.valueOf(formatArray[i].substring(pointIndex + 1));
					// 根据小数位数将原数据乘以10的decimalLength次方
					double multiple = Math.pow(10, decimalLength);
					BigDecimal bg = new BigDecimal(item);
					itemValue = bg.setScale(decimalLength, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(multiple))
							.intValue();
					item = String.valueOf(itemValue);
				} else {
					itemLength = Integer.valueOf(formatArray[i]);
				}
				// 补0
				item = ByteUtils.strAddZero(item, itemLength);
				dataField += ByteUtils.inverted(item);
			}
		}

		return dataField;
	}
	
	/**
	 * 回应解码
	 * Rola水表通断专用
	 * @param code
	 * @return
	 */
	public static Dlt645Data receiveWaterRola(Dlt645Data param) throws ProtocolException {
		String dataMarker = param.getDataMarker();
		// 取出数据参数
		String dataValues = param.getDataValues();
		String thisformat = "8.2,4";
		// 获取每个数据项的格式
		String[] formatArray = thisformat.split(",");
		String datas = "";
		// 用于标记dataValues被读取到第几位
		int flag = 0;
		// 根据格式取出每一个数据项
		for (String temp : formatArray) {
			// 数据项总长度与小数位长度
			int itemLength = 0, decimalLength = 0;
			// 数据项
			String item = "";
			// 判断格式是否包含符号位
			int plusOrMinus = temp.indexOf("±");
			if (plusOrMinus >= 0) {
				temp = temp.substring(1);
			}
			// 判断格式是否为16进制
			int isHex = temp.indexOf("h");
			if (isHex >= 0) {
				temp = temp.substring(1);
			}
			// 判断格式是否为2进制
			int isBit = temp.indexOf("b");
			if (isBit >= 0) {
				temp = temp.substring(1);
			}
			// 判断格式是否包含小数位
			int pointIndex = temp.indexOf(".");
			if (pointIndex > 0) {
				itemLength = Integer.valueOf(temp.substring(0, pointIndex));
				decimalLength = Integer.valueOf(temp.substring(pointIndex + 1));
			} else {
				itemLength = Integer.valueOf(temp);
			}
			// 截取
			item = dataValues.substring(flag, flag + itemLength);
			// 倒装
			item = ByteUtils.inverted(item);
			// 需返回二进制字符串
			if (isBit >= 0) {
				item = ByteUtils.toByteStr(item);
			} 
			else {
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
			flag += itemLength;
			datas = datas + item + ",";
		}
		datas = datas.substring(0, datas.length() - 1);
		param.setDataValues(datas);
		
		// 读取仪表状态字指令的特殊处理
		// 将返回的两个字节转为16位二进制字符串，根据状态字含义截取
		// 水表止码与状态字
		
			int idx = param.getDataValues().indexOf(",");
			byte[] bytes = ByteUtils.toByteArray(param.getDataValues().substring(idx + 1));
			param.setDataValues(
					param.getDataValues().substring(0, idx) + "," + ByteUtils.byteToBits(bytes, new int[] { 7 }));
		
		return param;
	}

	/**
	 * 回应解码
	 * 
	 * @param code
	 * @return
	 */
	public static Dlt645Data receive(Dlt645Data param) throws ProtocolException {
		return param;
	}

}
