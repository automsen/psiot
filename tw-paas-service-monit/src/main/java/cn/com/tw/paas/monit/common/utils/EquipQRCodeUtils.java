package cn.com.tw.paas.monit.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 设备二维码解析
 * 
 * @author admin
 *
 */
public class EquipQRCodeUtils {

	public static Map<String, String> translate(String code) {
		int keyIndex1 = 0, keyIndex2 = 0, valueIndex1 = 0, valueIndex2 = 0;
		Map<String, String> map = new HashMap<String, String>();
		while (!StringUtils.isEmpty(code)) {
			keyIndex2 = code.indexOf(":");
			if (keyIndex2 > 1) {
				String key = code.substring(keyIndex1, keyIndex2);
				String value = "";
				valueIndex1 = keyIndex2 + 1;
				valueIndex2 = code.indexOf(",");
				if (valueIndex2 > 1) {
					value = code.substring(valueIndex1, valueIndex2);
				} else {
					valueIndex2 = code.length() - 1;
					value = code.substring(valueIndex1);
				}
				code = code.substring(valueIndex2 + 1);
				map.put(key.trim(), value.trim());
			} else {
				break;
			}
		}

		return map;
	}

}
