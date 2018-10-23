package cn.com.tw.common.utils.tools.security;

/**
 * 字符串處理工具套件
 * @author shine
 *
 */
public class StringUtil {

	/**
	 * 字節數組轉換成16進制字符串
	 * @param b 待轉換的字節數組
	 * @return String 轉換之後的字符串
	 */
    public static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b!=null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}
    
    /**
     * 16進制字節數組轉換成字節數組
     * @param b 16進制字節數組
     * @return byte[] 字節數組
     */
    public static byte[] hex2byte(byte[] b) {
        if((b.length%2)!=0)
            throw new IllegalArgumentException();
		byte[] b2 = new byte[b.length/2];
		for (int n = 0; n < b.length; n+=2) {
		    String item = new String(b,n,2);
		    b2[n/2] = (byte)Integer.parseInt(item,16);
		}
        return b2;
    }
    
    /**
     * 16進制字符串轉換成字節數組
     * @param b 16進制字符串
     * @return byte[] 字節數組
     */
    public static byte[] hex2byte(String b){
    	return hex2byte(new byte[b.length()/2]);
    }
    
    /**
     * 將16進制字符串轉換為字符串（兼容多字節字符，比如漢字）
     * @param content 待轉換的16進制字符串
     * @return String 轉換後的字符串
     */
    public static String hex2String(String content){
    	  String enUnicode=null;
    	  String deUnicode=null;
    	  for(int i=0;i<content.length();i++){
    	      if(enUnicode==null){
    	       enUnicode=String.valueOf(content.charAt(i));
    	      }else{
    	       enUnicode=enUnicode+content.charAt(i);
    	      }
    	      if(i%4==3){
    	       if(enUnicode!=null){
    	        if(deUnicode==null){
    	         deUnicode=String.valueOf((char)Integer.valueOf(enUnicode, 16).intValue());
    	        }else{
    	         deUnicode=deUnicode+String.valueOf((char)Integer.valueOf(enUnicode, 16).intValue());
    	        }
    	       }
    	       enUnicode=null;
    	      }    	      
    	     }
    	  return deUnicode;
    	 }
    
    	/**
    	 * 將普通字符串轉換成16進制字符串
    	 * @param content 待輪換的普通字符串
    	 * @return String 轉換後的16進制字符串
    	 */
    	 public static String string2Hex(String content){
    	  String enUnicode=null;
    	  for(int i=0;i<content.length();i++){
    	   if(i==0){
    	       enUnicode=getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
    	      }else{
    	       enUnicode=enUnicode+getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
    	      }
    	  }
    	  return enUnicode;
    	 }
    	 
    	 /**
    	  * 16進制字符串轉換（補位操作）
    	  * @param hexString 16進制字符串
    	  * @return String 轉換後的16進制字符串
    	  */
    	 private static String getHexString(String hexString){
    	      String hexStr="";
    	      for(int i=hexString.length();i<4;i++){
    	       if(i==hexString.length())
    	        hexStr="0";
    	       else
    	        hexStr=hexStr+"0";
    	      }
    	      return hexStr+hexString;
    	 }
    	
    	 /**
    		 * ASCII码字節數組转BCD码字節數組 
    		 * BCD码（Binary-Coded Decimal‎）亦称二进码十进数或二-十进制代码。
    		 * 用4位二进制数来表示1位十进制数中的0~9这10个数码。是一种二进制的
    		 * 数字编码形式，用二进制编码的十进制代码。
    		 * 最常用的BCD碼編碼方式是8421法，即個位數權重是1，十位權重是2，百位權重是4，千位權重是8
    		 * 
    		 * @param ascii 待转码的ASCII码数组
    		 * @param asc_len ASCII码数组的长度
    		 * @return byte[] 转码后的BCD码数组
    		 */
    	    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {  
    	        byte[] bcd = new byte[asc_len / 2];  
    	        int j = 0;  
    	        for (int i = 0; i < (asc_len + 1) / 2; i++) {  
    	            bcd[i] = asc_to_bcd(ascii[j++]);  
    	            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));  
    	        }  
    	        return bcd;  
    	    } 
    	    
    	    /**
    	     * ASCII码转BCD
    	     * @param asc ascii码字节
    	     * @return byte 转码后的BCD字节
    	     */
    	    public static byte asc_to_bcd(byte asc) {  
    	        byte bcd;  
    	  
    	        if ((asc >= '0') && (asc <= '9'))  
    	            bcd = (byte) (asc - '0');  
    	        else if ((asc >= 'A') && (asc <= 'F'))  
    	            bcd = (byte) (asc - 'A' + 10);  
    	        else if ((asc >= 'a') && (asc <= 'f'))  
    	            bcd = (byte) (asc - 'a' + 10);  
    	        else  
    	            bcd = (byte) (asc - 48);  
    	        return bcd;  
    	    }  
    	    
    		/**
    		 * BCD转字符串
    		 * @param bytes 待转码的BCD字节数组
    		 * @return String 转换后的字符串
    		 */
    		public static String bcd2Str(byte[] bytes) {
    			char temp[] = new char[bytes.length * 2], val;

    			for (int i = 0; i < bytes.length; i++) {
    				val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
    				temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

    				val = (char) (bytes[i] & 0x0f);
    				temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
    			}
    			return new String(temp);
    		}
    	 
    	    /**
    	     * 拆分字符串
    	     * @param string 待拆分的字符串
    	     * @param len 待拆分的字符串的长度
    	     * @return String[] 字符串数组
    	     */
    		public static String[] splitString(String string, int len) {
    			int x = string.length() / len;
    			int y = string.length() % len;
    			int z = 0;
    			if (y != 0) {
    				z = 1;
    			}
    			String[] strings = new String[x + z];
    			String str = "";
    			for (int i = 0; i < x + z; i++) {
    				if (i == x + z - 1 && y != 0) {
    					str = string.substring(i * len, i * len + y);
    				} else {
    					str = string.substring(i * len, i * len + len);
    				}
    				strings[i] = str;
    			}
    			return strings;
    		} 
    		
    		/**
    		 * 拆分数组
    		 * @param data 待拆分的字节数组
    		 * @param len 待拆分的字节数组的长度
    		 * @return byte[][] 拆分后的二维字节数组
    		 */
    		public static byte[][] splitArray(byte[] data, int len) {
    			int x = data.length / len;
    			int y = data.length % len;
    			int z = 0;
    			if (y != 0) {
    				z = 1;
    			}
    			byte[][] arrays = new byte[x + z][];
    			byte[] arr;
    			for (int i = 0; i < x + z; i++) {
    				arr = new byte[len];
    				if (i == x + z - 1 && y != 0) {
    					System.arraycopy(data, i * len, arr, 0, y);
    				} else {
    					System.arraycopy(data, i * len, arr, 0, len);
    				}
    				arrays[i] = arr;
    			}
    			return arrays;
    		}
}
