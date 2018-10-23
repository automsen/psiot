package cn.com.tw.common.protocol.dlt645.application;

import java.math.BigDecimal;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * 加密费控电表类指令
 * 
 * @author Cheng Qi Peng
 *
 */
public class EncryptChargeElec {
	/**
	 * 下发编码
	 * 
	 * @return
	 */
	public static String send(Object... params) throws ProtocolException{
		// 用于格式化前面补0
		String format = "00000000";
		// 数据标识
		String dataMarker = (String) params[2];
		// 操作者编码(暂无相关需求，可为定长的任意内容)
		String operatorCode = "00000000";
		// 购电金额（xxxxxx.xx）需要乘100后转16进制
		String money = (String) params[3];
		BigDecimal bg = new BigDecimal(money);
		int moneyNum = bg.setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).intValue();
		String temp = format + Integer.toHexString(moneyNum);
		money = temp.substring(temp.length() - format.length());
		// 购电次数(8位)需要转16进制
		String payTime = (String) params[4];
		int payNum = Integer.valueOf(payTime);
		String temp2 = format + Integer.toHexString(payNum);
		payTime = temp2.substring(temp2.length() - format.length());
		// MAC1(8位)
		String mac1 = "00000000";
		// 客户编号(12位)与地址码相同
		String customerCode = (String) params[0];
		customerCode = ByteUtils.inverted(customerCode, 12);
		// MAC2(8位)
		String mac2 = "00000000";
		return ByteUtils.inverted(dataMarker) + ByteUtils.inverted(operatorCode) + ByteUtils.inverted(money)
				+ ByteUtils.inverted(payTime) + mac1 + customerCode + mac2;
	}

	/**
	 * 正常返回
	 * 
	 * @param param
	 * @return
	 */
	public static Dlt645Data receive(Dlt645Data param) throws ProtocolException{
		// 取出数据参数
		String dataValues = param.getDataValues();
		String datas = "";
		// 当前剩余金额（xxxxxx.xx）
		String lastMoney = ByteUtils.inverted(dataValues.substring(0, 8));
		datas += ByteUtils.insertPoint(lastMoney, 2) + ",";
		// 当前透支金额（xxxxxx.xx）
		String overMoney = ByteUtils.inverted(dataValues.substring(8, 16));
		datas += ByteUtils.insertPoint(overMoney, 2) + ",";
		// 表内已购电次数（xxxxxxxx）
		String payTime = ByteUtils.inverted(dataValues.substring(16, 24));
		datas += payTime + ",";
		// 本次剩余金额变动（xxxxxx.xx）
		String changeMoney = ByteUtils.inverted(dataValues.substring(24, 32));
		datas += ByteUtils.insertPoint(changeMoney, 2) + ",";
		// 表内累计电量xxxxxx.xx
		String elecEne = ByteUtils.inverted(dataValues.substring(32, 40));
		datas += ByteUtils.insertPoint(elecEne, 2);
		param.setDataValues(datas);
		return param;
	}

	/**
	 * 异常返回
	 * 
	 * @param param
	 * @return
	 */
	public static Dlt645Data receiveErr(Dlt645Data param) {
		String dataField = param.getDataField();
		// 取出错误码(只有一个字节两位)
		param.setErrCode(dataField.substring(8, 8 + 2));
		// 取出数据参数(跳过不使用的两位从第12位开始取数据域)
		String dataValues = dataField.substring(12);
		String datas = "";
		// 当前剩余金额（xxxxxx.xx）
		String lastMoney = ByteUtils.inverted(dataValues.substring(0, 8));
		datas += ByteUtils.insertPoint(lastMoney, 2) + ",";
		// 当前透支金额（xxxxxx.xx）
		String overMoney = ByteUtils.inverted(dataValues.substring(8, 16));
		datas += ByteUtils.insertPoint(overMoney, 2) + ",";
		// 表内已购电次数（xxxxxxxx）
		String payTime = ByteUtils.inverted(dataValues.substring(16, 24));
		datas += payTime + ",";
		// 本次剩余金额变动（xxxxxx.xx）
		String changeMoney = ByteUtils.inverted(dataValues.substring(24, 32));
		datas += ByteUtils.insertPoint(changeMoney, 2) + ",";
		// 表内累计电量xxxxxx.xx
		String elecEne = ByteUtils.inverted(dataValues.substring(32, 40));
		datas += ByteUtils.insertPoint(elecEne, 2);
		param.setDataValues(datas);
		return param;
	}
}
