package cn.com.tw.common.protocol.dlt645.application;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * 曲线数据读取类指令(后续请求)
 * 
 * @author Cheng Qi Peng
 *
 */
public class CurveReadFollow {

	/**
	 * 下发编码(有参数) 
	 * 请求帧的帧序号从1开始进行加一计数，应答帧的帧序号要与请求帧相同。帧序号占用一个字节，计数范围为1～255
	 * @return
	 */
	public static String send(Object... params) throws ProtocolException {
		// 数据域
		String dataField = "";
		// 数据标识
		String dataMarker = (String) params[2];
		dataMarker = ByteUtils.inverted(dataMarker);
		// 帧序号
		String seq = (String) params[3];
		// 16进制
		seq = Integer.toHexString(Integer.valueOf(seq)).toString();
		if (seq.length() == 1) {
			seq = "0" + seq;
		}
		dataField = dataField + dataMarker + seq ;
		return dataField;
	}

	/**
	 * 应答解码
	 * @param param
	 * @param thisformat
	 * @return
	 */
	public static Dlt645Data receive(Dlt645Data param, String formatStr){
		// 取出数据参数
		String dataValues = param.getDataValues();
		// 用于标记dataValues下次取值从第几位开始
		int startFlag = 0;
		// 解析完整格式
		String[] thisFormat = formatStr.split(":");
		// 每个数据点的长度
		int nodeLength = Integer.valueOf(thisFormat[0]);
		// 每个数据点内的格式
		String nodeFormat = thisFormat[1];
		// 数据点内每个数据项的格式
		String[] formatArray = nodeFormat.split(",");
		String datas = "";
		// 用于标记dataValues本次循环取值到第几位为止
		int limitFlag = startFlag;
		// 总长度-取值限制=未取值长度>每个数据点的长度
		while (dataValues.length()-limitFlag>=nodeLength){
			// 根据格式取出每一个数据项
			for (String temp : formatArray) {
				// 单数据项长度与小数位长度
				int itemLength, decimalLength;
				// 数据项
				String item = "";
				// 判断格式是否包含小数位
				int pointIndex = temp.indexOf(".");
				if (pointIndex > 0) {
					itemLength = Integer.valueOf(temp.substring(0, pointIndex));
					decimalLength = Integer.valueOf(temp.substring(pointIndex + 1));
					// 截取
					item = dataValues.substring(startFlag, startFlag + itemLength);
					// 倒装
					item = ByteUtils.inverted(item);
					// 插入小数点
					item = ByteUtils.insertPoint(item, decimalLength);
				} else {
					itemLength = Integer.valueOf(temp);
					// 截取
					item = dataValues.substring(startFlag, startFlag + itemLength);
					// 倒装
					item = ByteUtils.inverted(item);
				}
				startFlag += itemLength;
				datas = datas + item + ",";
			}
			datas = datas.substring(0, datas.length() - 1);
			datas = datas + ";";
			limitFlag = limitFlag + nodeLength;
		}
		if (datas.length()>0){
			datas = datas.substring(0, datas.length() - 1);
		}
		param.setDataValues(datas);
		String seq = dataValues.substring(startFlag);
		// 16进制
		seq = String.valueOf(Integer.parseInt(seq, 16));
		param.setSeq(seq);
		return param;
	}

}
