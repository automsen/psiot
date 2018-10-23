package cn.com.tw.common.protocol.dlt645.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.exception.code.ProtocolCode;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * 常规读取类指令
 * 
 * @author Cheng Qi Peng
 *
 */
public class SimpleRead {

	private static String[] EventName =   
        {"负荷开关误动或拒动","ESAM错误","内卡初始化错误","时钟电池电压低",  
        "内部程序错误","存储器故障或损坏","保留","时钟故障",  
        "停电抄表电池欠压","透支状态","开表盖","开端钮盖",  
        "恒定磁场干扰","电源异常","跳闸成功","合闸成功",
        "A相失压","A相欠压","A相过压","A相失流",
        "A相过流","A相过载","A相功率反向","A相断相",
        "A相断流","保留","保留","保留",
        "保留","保留","保留","保留",
        "B相失压","B相欠压","B相过压","B相失流",
        "B相过流","B相过载","B相功率反向","B相断相",
        "B相断流","保留","保留","保留",
        "保留","保留","保留","保留",
        "C相失压","C相欠压","C相过压","C相失流",
        "C相过流","C相过载","C相功率反向","C相断相",
        "C相断流","保留","保留","保留",
        "保留","保留","保留","保留",
        "电压逆相序","电流逆相序","电压不平衡","电流不平衡",
        "辅助电源失电","掉电","需量超限","总功率因数超下限",
        "电流严重不平衡","潮流反向","全失压","保留",
        "保留","保留","保留","保留",
        "编程","电表清零","需量清零","事件清零",
        "校时","时段表编程","时区表编程","周休日编程",
        "节假日编程","有功组合方式编程","无功组合方式1编程","无功组合方式2编程",
        "结算日编程","费率参数编程","阶梯表编程","密钥更新"}; 
	/**
	 * 请求编码(无参数)
	 * 
	 * @return
	 */
	public static String send(Object... params) throws ProtocolException {
		// 数据域
		String dataField = "";
		String dataMarker = (String) params[2];
		dataField += ByteUtils.inverted(dataMarker);
		// 读档案个数 或 读表档案(分区)
		if (dataMarker.equals("04700107")||dataMarker.equals("04700108")){
			for(int i = 3;i<params.length;i++){
				String temp = (String) params[i];
				Integer tempNum = Integer.valueOf(temp);
				temp = Integer.toHexString(tempNum).toUpperCase();
				temp = ByteUtils.strAddZero(temp, 2);
				temp = ByteUtils.inverted(temp);
				dataField += temp;
			}
		}
		return dataField;
	}

	/**
	 * 应答解码
	 * 
	 * @param param
	 * @param thisformat
	 * @return
	 */
	public static Dlt645Data receive(Dlt645Data param, String thisformat) {
		String dataMarker = param.getDataMarker();
		// 取出数据参数
		String dataValues = param.getDataValues();
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
		// 扩展读电量及电网参数
		// 扩展读单三相表计费控表电量及金额
		if (dataMarker.equals("04601001") || dataMarker.equals("04601003") || dataMarker.equals("04601202")
				|| dataMarker.equals("04601203")) {
			int idx = param.getDataValues().lastIndexOf(",");
			String statusStr = ByteUtils.toByteStr(param.getDataValues().substring(idx + 1));
			param.setDataValues(param.getDataValues().substring(0, idx) + "," + statusStr.substring(3, 4));
		}
		else if (dataMarker.equals("04601206")){
			String[] values = param.getDataValues().split(",");
			values[14] = ByteUtils.toByteStr(values[14]).substring(3, 4);
			String temp = "";
			for (int i = 0;i<values.length;i++){
				temp = temp +","+ values[i];
			}
			param.setDataValues(temp.substring(1, temp.length() - 1));
		}
		// 读取仪表状态字指令的特殊处理
		// 将返回的两个字节转为16位二进制字符串，根据状态字含义截取
		// 电表状态字3
		else if (dataMarker.equals("04000503")) {
			byte[] bytes = ByteUtils.toByteArray(param.getDataValues());
			param.setDataValues(ByteUtils.byteToBits(bytes, new int[] { 7, 14 }));
		}
		// 水表止码与状态字
		else if (dataMarker.equals("00004100")) {
			int idx = param.getDataValues().indexOf(",");
			byte[] bytes = ByteUtils.toByteArray(param.getDataValues().substring(idx + 1));
			param.setDataValues(
					param.getDataValues().substring(0, idx) + "," + ByteUtils.byteToBits(bytes, new int[] { 7 }));
		}
		else if (dataMarker.equals("00004200")) {
			int idx1 = param.getDataValues().indexOf(",");
			int idx2 = param.getDataValues().lastIndexOf(",");
			String waterWord = param.getDataValues().substring(idx1 + 1,idx2);
			byte[] bytes = ByteUtils.toByteArray(waterWord);
			String temp = param.getDataValues().substring(idx2+1);
			// 电池电压乘系数
			BigDecimal waterBattery = new BigDecimal(temp).multiply(new BigDecimal("15.37"));
			param.setDataValues(
					param.getDataValues().substring(0, idx1) + "," + ByteUtils.byteToBits(bytes, new int[] { 7 })
					+ "," + waterBattery);
		}
		return param;
	}

	/**
	 * 集中器主动上传配置专用解码
	 * 
	 * @param param
	 * @param thisformat
	 * @return
	 */
	public static Dlt645Data receiveConcentrator(Dlt645Data param) {
		String dataMarker = param.getDataMarker();
		// 取出数据参数
		String dataValues = param.getDataValues();
		String datas = "";
		// 读上传周期
		if (dataMarker.equals("04700102")) {
			int itemLength = 4;
			String cycle = dataValues.substring(0, itemLength);
			cycle = ByteUtils.inverted(cycle);
			datas = cycle;
			param.setDataValues(datas);
			return param;
		// 读表档案(无分区)
		} else if (dataMarker.equals("04700103")) {
			int itemLength = 2;
			int flag = 2;
			// 返回数据
			String itemNumStr = dataValues.substring(0, itemLength);
			itemNumStr = ByteUtils.inverted(itemNumStr);
			datas += itemNumStr;
			int itemNum = Integer.valueOf(itemNumStr);
			// 返回数据长度异常
			if (itemNum * 14 + 2 != dataValues.length()) {
				throw new ProtocolException(ProtocolCode.PARAM_MISSING, "param lose");
			} else {
				for (int i = 0; i < itemNum; i++) {
					String item1 = dataValues.substring(flag, flag + 12);
					// 倒装
					item1 = ByteUtils.inverted(item1);
					flag += 12;
					datas += "," +item1;
					String item2 = dataValues.substring(flag, flag + 2);
					// 倒装
					item2 = ByteUtils.inverted(item2);
					flag += 2;
					datas += "," +item2;
				}
			}
			param.setDataValues(datas);
			return param;
		// 读档案个数
		} else if (dataMarker.equals("04700107")) {
			// 档案区号(0,1~8)
			String areaCode = dataValues.substring(0, 2);
			areaCode = ByteUtils.inverted(areaCode);
			datas += areaCode;
			// 档案个数
			String itemNumStr = dataValues.substring(2, 6);
			itemNumStr = ByteUtils.inverted(itemNumStr);
			Integer itemNum = Integer.parseInt(itemNumStr,16);
			datas += "," + String.valueOf(itemNum);
			param.setDataValues(datas);
			return param;
		// 读表档案(分区)
		} else if (dataMarker.equals("04700108")) {
			// 档案区号(0,1~8)
			String areaCode = dataValues.substring(0, 2);
			areaCode = ByteUtils.inverted(areaCode);
			datas += areaCode;
			// 档案起始序号
			String startIndexStr = dataValues.substring(2, 4);
			startIndexStr = ByteUtils.inverted(startIndexStr);
			Integer startIndex = Integer.parseInt(startIndexStr,16);
			datas += "," + String.valueOf(startIndex);
			// 档案个数
			String itemNumStr = dataValues.substring(4, 6);
			itemNumStr = ByteUtils.inverted(itemNumStr);
			Integer itemNum = Integer.parseInt(itemNumStr,16);
			datas += "," + String.valueOf(itemNum);
			int flag = 6;
			// 返回数据长度异常
			if (itemNum * 14 + 6 != dataValues.length()) {
				throw new ProtocolException(ProtocolCode.PARAM_MISSING, "param lose");
			} else {
				for (int i = 0; i < itemNum; i++) {
					String item1 = dataValues.substring(flag, flag + 12);
					// 倒装
					item1 = ByteUtils.inverted(item1);
					flag += 12;
					datas += "," + item1;
					String item2 = dataValues.substring(flag, flag + 2);
					// 倒装
					item2 = ByteUtils.inverted(item2);
					flag += 2;
					datas += "," + item2;
				}
			}
			param.setDataValues(datas);
			return param;
		// 读采集指令		
		} else if (dataMarker.equals("04700201")) {
			int itemLength = 2;
			int flag = 2;
			// 返回数据
			String itemNumStr = dataValues.substring(0, itemLength);
			itemNumStr = ByteUtils.inverted(itemNumStr);
			int itemNum = Integer.valueOf(itemNumStr);
			// 返回数据长度异常
			if (itemNum * 8 + 2 != dataValues.length()) {
				throw new ProtocolException(ProtocolCode.PARAM_MISSING, "param lose");
			} else {
				for (int i = 0; i < itemNum; i++) {
					String item1 = dataValues.substring(flag, flag + 8);
					// 倒装
					item1 = ByteUtils.inverted(item1);
					flag += 8;
					datas += item1 + ",";
				}
			}
			param.setDataValues(datas);
			return param;
		}
		return param;
	}

	/**
	 * 深圳事件上报专用解码
	 * 
	 * @param param
	 * @param thisformat
	 * @return
	 */
	public static Dlt645Data receiveEvent(Dlt645Data param) {
		String dataMarker = param.getDataMarker();
		// 取出数据参数
		String dataValues = param.getDataValues();
		String datas = "";
		// 主动上报模式字
		if (dataMarker.equals("04001104")) {
			int wordLength = 16;
			String temp = dataValues.substring(0, wordLength);
			temp = ByteUtils.inverted(temp);
			datas = ByteUtils.toByteStr(temp);
			param.setDataValues(datas);
			return param;
		}
		// 主动上报状态字
		else if (dataMarker.equals("04001501")) {
			int wordLength = 24;
			// 返回数据
			String temp = dataValues.substring(0, wordLength);
			temp = ByteUtils.inverted(temp);
			datas = ByteUtils.toByteStr(temp);
			// 查找对应事件名称
			List<String> detailList = new ArrayList<String>();
			String details = "";
			for (int i = 0; i < datas.length(); i++) {
				if (datas.charAt(i) == '1') {
					detailList.add(EventName[i]);
				}
			}
			int j = 0;
			for (int i = wordLength; dataValues.length() - i > 0; i += 2) {
				temp = dataValues.substring(i, i + 2);
				temp = ByteUtils.inverted(temp);
				if (!temp.equals("aa")) {
					temp = String.valueOf(Integer.parseInt(temp, 16));
					datas += "," + temp;
					details += detailList.get(j) + "发生" + temp + "次;";
					j++;
				}
			}

			param.setDataValues(datas);
			param.setDetail(details);
			return param;
		}
		return param;
	}

}
