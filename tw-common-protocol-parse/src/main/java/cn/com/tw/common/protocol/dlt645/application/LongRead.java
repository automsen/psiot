package cn.com.tw.common.protocol.dlt645.application;

import java.util.HashMap;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.dlt645.cons.InstructionFormat;
import cn.com.tw.common.protocol.utils.ByteUtils;

public class LongRead {
	private static HashMap<String, String> longFormat = InstructionFormat.longFormatMap();

	/**
	 * 第一次应答
	 * @param param
	 * @param thisformat
	 * @return
	 */
	public static Dlt645Data receive(Dlt645Data param, String formatStr){
		param.setSeq("0");
		// 保留数据域其它内容等组装后再解析
		param.setLonger(true);
		return param;
	}
	/**
	 * 后续应答
	 * @param param
	 * @param thisformat
	 * @return
	 */
	public static Dlt645Data receiveFollow(Dlt645Data param, String formatStr){
		// 取出数据参数
		String dataValues = param.getDataValues();

		// 取出帧序号
		String seq = dataValues.substring(dataValues.length()-2);
		// 16进制
		seq = String.valueOf(Integer.parseInt(seq, 16));
		param.setSeq(seq);
		// 保留数据域其它内容等组装后再解析
		param.setDataValues(dataValues.substring(0,dataValues.length()-2));
		param.setLonger(true);
		return param;
	}
	
	/**
	 * 完整数据解码
	 * @param param
	 * @param thisformat
	 * @return
	 */
	public static String decode(String dataMarker,String dataValues){

		String thisformat = longFormat.get(dataMarker);
		// 获取每个数据项的格式
		String[] formatArray = thisformat.split(",");
		String datas = "";
		// 用于标记dataValues被读取到第几位
		int flag = 0;
		// 根据格式取出每一个数据项
		for (String temp : formatArray) {
			// 数据项总长度与小数位长度
			int itemLength, decimalLength;
			// 数据项
			String item = "";
			// 判断格式是否包含符号位
			int plusOrMinus = temp.indexOf("±");
			if (plusOrMinus >= 0) {
				temp = temp.substring(1);
			}
			// 判断格式是否包含小数位
			int pointIndex = temp.indexOf(".");
			if (pointIndex > 0) {
				itemLength = Integer.valueOf(temp.substring(0, pointIndex));
				decimalLength = Integer.valueOf(temp.substring(pointIndex + 1));
				// 截取
				item = dataValues.substring(flag, flag + itemLength);
				// 倒装
				item = ByteUtils.inverted(item);
				if (plusOrMinus >= 0) {
					int first = Integer.valueOf(item.substring(0, 1));
					if (first >= 8) {
						first = first - 8;
						item = "-" + String.valueOf(first) + item.substring(1, itemLength);
					}
				}
				// 插入小数点
				item = ByteUtils.insertPoint(item, decimalLength);
			} else {
				itemLength = Integer.valueOf(temp);
				// 截取
				item = dataValues.substring(flag, flag + itemLength);
				// 倒装
				item = ByteUtils.inverted(item);
				if (plusOrMinus >= 0) {
					int first = Integer.valueOf(item.substring(0, 1));
					if (first >= 8) {
						first = first - 8;
						item = "-" + String.valueOf(first) + item.substring(1, itemLength);
					}
				}
			}
			flag += itemLength;
			datas = datas + item + ",";
		}
		datas = datas.substring(0, datas.length() - 1);
		return datas;
	}
}
