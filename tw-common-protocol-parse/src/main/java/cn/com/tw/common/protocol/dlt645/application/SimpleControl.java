package cn.com.tw.common.protocol.dlt645.application;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.time.FastDateFormat;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * 不加密控制类指令
 * 
 * @author Cheng Qi Peng
 *
 */
public class SimpleControl {

	private static final FastDateFormat fdf = FastDateFormat.getInstance("ssmmHHddMMyy");

	/**
	 * 下发编码
	 * 
	 * @return
	 */
	public static String send(Object... params) throws ProtocolException{
		// 密码
		String password = (String) params[2];
		// 操作者编码(暂无相关需求，可为定长的任意内容)
		String operatorCode = "30303030";
		// 操作类型
		String controlType = (String) params[3];
		String dateStr = getDateStr();
		//"00"为固定值
		String dataValues = controlType + "00" + dateStr;
		return ByteUtils.inverted(password) + operatorCode + dataValues;
	}

	/**
	 * 正常返回无数据项
	 * 无需解析
	 * @param param
	 * @return
	 */
	public static Dlt645Data receive(Dlt645Data param) {
		return param;
	}

	/**
	 * 获取当日最后一秒的时间字符串 用于编码
	 * 
	 * @return
	 */
	private static String getDateStr() {
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		String dateStr = fdf.format(cal.getTime());
		return dateStr;
	}
}
