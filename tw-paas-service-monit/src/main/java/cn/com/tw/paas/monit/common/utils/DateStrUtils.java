package cn.com.tw.paas.monit.common.utils;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
/**
 * Date与String互转
 * @author Cheng Qi Peng
 *
 */
public class DateStrUtils {

	public static String yearFormat="yyyy";
	public static String monthFormat="yyyyMM";
	public static String dayFormat="yyyyMMdd";
	public static String minFormat="yyyyMMddHHmm";
	public static String secondFormat="yyyyMMddHHmmss";
	public static String zhFormat="MM月dd日HH时mm分";

	public static String fullFormat="yyyy-MM-dd HH:mm:ss";

	public static String dateToTd(Date date,String format){
		FastDateFormat fdf = FastDateFormat.getInstance(format); 
		return fdf.format(date);
	}
	
	public static Date tdToDate(String td,String format){
		String partterns[] = new String[1];
		partterns[0] = format;
		try {
			return DateUtils.parseDate(td, partterns);
		} catch (ParseException e) {
			return null;
		}
	}
	
}
