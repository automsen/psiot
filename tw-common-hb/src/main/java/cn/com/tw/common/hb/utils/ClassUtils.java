package cn.com.tw.common.hb.utils;

import org.apache.hadoop.hbase.util.Bytes;

public class ClassUtils {

	public static byte[] obj2Byte(Object obj) {
		byte[] valBts = null;

		if (obj instanceof Integer) {
			valBts = Bytes.toBytes((int) obj);
		} else if (obj instanceof String) {
			valBts = Bytes.toBytes((String) obj);
		} else if (obj instanceof Double) {
			valBts = Bytes.toBytes((double) obj);
		} else if (obj instanceof Float) {
			valBts = Bytes.toBytes((float) obj);
		} else if (obj instanceof Long) {
			valBts = Bytes.toBytes((long) obj);
		} else if (obj instanceof Boolean) {
			valBts = Bytes.toBytes((boolean) obj);
		} else {
		}

		return valBts;

	}

}
