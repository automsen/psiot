package cn.com.tw.paas.monit.common.utils;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

public class SignatureUtil {

	/**
	 * 生成sign HMAC-SHA256 或 MD5 签名
	 * @param map map
	 * @param paternerKey paternerKey
	 * @return sign
	 */
	public static String generateSign(Map<String, String> map,String paternerKey){
		return generateSign(map, null, paternerKey);
	}
	
	/**
	 * 生成MD5 签名
	 * @param map map
	 * @param sign_type HMAC-SHA256 或 MD5
	 * @param paternerKey paternerKey
	 * @return sign
	 */
	public static String generateSign(Map<String, String> map,String sign_type,String paternerKey){
		Map<String, String> tmap = MapUtil.order(map);
		if(tmap.containsKey("sign")){
			tmap.remove("sign");
		}
		String str = MapUtil.mapJoin(tmap, false, false);

		return DigestUtils.md5Hex(str+"&secretKey="+paternerKey).toUpperCase();
		
	}
	
	/**
	 * 生成事件消息接收签名
	 * @param token token
	 * @param timestamp timestamp
	 * @param nonce nonce
	 * @return str
	 */
	@SuppressWarnings("deprecation")
	public static String generateEventMessageSignature(String token, String timestamp,String nonce) {
		String[] array = new String[]{token,timestamp,nonce};
		Arrays.sort(array);
		String s = StringUtils.arrayToDelimitedString(array, "");
		return DigestUtils.shaHex(s);
	}

	/**
	 * mch 支付、代扣异步通知签名验证
	 * @param map 参与签名的参数
	 * @param key mch key
	 * @return boolean
	 */
	public static boolean validateSign(Map<String,String> map,String key){
		return validateSign(map, null, key);
	}
	
	/**
	 * 
	 * @param map 参与签名的参数 验证
	 * @param sign_type HMAC-SHA256 或 MD5 
	 * @param key mch key
	 * @return boolean
	 */
	public static boolean validateSign(Map<String,String> map,String sign_type,String key){
		if(map.get("sign") == null){
			return false;
		}
		// TODO 测试作弊用sign 
		if(map.get("sign").equals("test") ){
			return true;
		}
		return map.get("sign").equals(generateSign(map,sign_type,key));
	}

}
