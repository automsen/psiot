package cn.com.tw.common.utils.tools.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	
	public static String digest(String id){ 
		String md5_content = null; 
		try {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5"); 
		messageDigest.reset(); 
		messageDigest.update(id.getBytes());
		byte[] bytes = messageDigest.digest();
		md5_content = new String(bytesToHexString(bytes)); 
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace(); 
		}
		return md5_content;
	}
	
	private static String bytesToHexString(byte[] src){   
	    StringBuilder stringBuilder = new StringBuilder("");   
	    if (src == null || src.length <= 0) {   
	        return null;   
	    }   
	    for (int i = 0; i < src.length; i++) {   
	        int v = src[i] & 0xFF;   
	        String hv = Integer.toHexString(v);   
	        if (hv.length() < 2) {   
	            stringBuilder.append(0);   
	        }   
	        stringBuilder.append(hv);   
	    }   
	    return stringBuilder.toString();   
	}   


}
