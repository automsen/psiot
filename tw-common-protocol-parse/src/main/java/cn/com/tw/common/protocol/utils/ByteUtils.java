package cn.com.tw.common.protocol.utils;

public class ByteUtils {
	private static String hexStrTemp = "0123456789ABCDEF";
	private static String[] binaryArray =
	        {"0000","0001","0010","0011",
	        "0100","0101","0110","0111",
	        "1000","1001","1010","1011",
	        "1100","1101","1110","1111"};
	
	/**
	 * 字符串插入小数点
	 * @param str
	 * @return
	 */
	public static String insertPoint(String str,int index){
		StringBuffer num = new StringBuffer(str);
		if (str.length()>index){
			num.insert(str.length() - index, '.');
		}
		else if (str.length()<=index){
			num.insert(0, "0.");
			while (num.length()-2<index){
				num.insert(2, '0');
			}
		}
		return num.toString();
	}

	/**
	 * 右移
	 * @param data
	 * @param shiftVal
	 * @return
	 */
	public static byte[] rightShift(byte [] data , int shiftVal){  
        byte[] out = new byte[data.length];  
        int offset = 0;  
        byte first = (byte) ( ((data[0] & 0xff )>> shiftVal) );  
        out[offset++] = first;  
        for (int i = 0; i < data.length-1 ; i++){  
            byte overflowed = (byte) ((data[i] & 0xff) << (8 - shiftVal));  
            byte next = (byte) (overflowed | ((data[i+1] & 0xff) >> shiftVal));  
            out[offset++] = next;  
        }
        return out;  
    }
	
	/**
	 * 循环右移
	 * @param data
	 * @param shiftVal
	 * @return
	 */
	public static byte[] circularRightShift(byte [] data , int shiftVal){  
        byte[] out = new byte[data.length];  
        byte first = (byte) ( ((data[0] & 0xff )>> shiftVal) );
        byte last = (byte) ( ((data[data.length - 1] & 0xff ) << (8 - shiftVal)) );
        out[0] = (byte) (first | last);  
        for (int i = 0; i < data.length-1 ; i++){  
            byte overflowed = (byte) ((data[i] & 0xff) << (8 - shiftVal));  
            byte next = (byte) (overflowed | ((data[i+1] & 0xff) >> shiftVal));  
            out[i + 1] = next;  
        }
        return out;  
    }
	
	/**
	 * 左移
	 * @param data
	 * @param shiftVal
	 * @return
	 */
	public static byte[] leftShift(byte [] data , int shiftVal){  
        byte [] out = new byte[data.length];  
        for (int i = 0; i < data.length-1; i++){  
            byte overflowed = (byte) ((data[i]&0xff) << shiftVal);  
            byte next = (byte) (overflowed | ((data[i+1] & 0xff) >> (8-shiftVal)));  
            out[i] = next;  
        }
        out[data.length-1] = (byte)((data[data.length-1] & 0xff) << shiftVal);
        return out;  
    }
	
	/**
	 * 循环左移
	 * @param data
	 * @param shiftVal
	 * @return
	 */
	public static byte[] circularLeftShift(byte [] data , int shiftVal){  
        byte[] out = new byte[data.length];
        byte first = (byte) ( ((data[0] & 0xff ) >> (8 - shiftVal)));
        for (int i = 0; i < data.length-1; i++){  
            byte overflowed = (byte) ((data[i] & 0xff) << shiftVal);  
            byte next = (byte) (overflowed | ((data[i+1] & 0xff) >> (8-shiftVal)));  
            out[i] = next;  
        }
        out[data.length-1] = (byte)(first | (data[data.length-1] & 0xff) << shiftVal);
        return out;  
    } 

	
	/**
	 * 加密
	 * @param plainText
	 * @param sKey
	 * @param meterAddr
	 * @return
	 */
	public static String EncryptStr(String plainText, String sKey, String meterAddr)
	{
		byte[] text = ByteUtils.toByteArray(plainText);
		byte[] addr = ByteUtils.toByteArray(meterAddr);
		//高低位倒转
		byte[] key = ByteUtils.toByteArray(ByteUtils.inverted(sKey));
		//明文循环左移5位
		byte[] tempText = circularLeftShift(text, 5);
		//通讯地址循环右移5位
		byte[] tempAddr = circularRightShift(addr, 5);
		byte[] secret = new byte[addr.length];
		//高低位倒转
		for (int i = 0; i < 6; i++){
			secret[i] = tempAddr[5 - i];
		}
		//异或运算得到最终密钥
		for (int i = 0; i < 6; i++){
			secret[i] = (byte) ((secret[i] & 0xff) ^ (key[i] & 0xff));
		}
		//异或运算加密
		for (int i = 0; i < tempText.length; i++) {
			tempText[i] = (byte) ((tempText[i] & 0xff) ^ (secret[i % 6] & 0xff));
		}
		String cipherText=ByteUtils.bytes2hex(tempText);
		return cipherText.toUpperCase();
	}

	/**
	 * 发送数据每字节+33
	 * 
	 * @param data
	 * @return
	 */
	public static String dataUp(String data) {
		String result = "";
		for (int i = 0; i < data.length(); i = i + 2) {
			String dataChar;
			dataChar = Integer
					.toHexString((Integer.parseInt((data.substring(i, i + 2)), 16)
						+ Integer.parseInt(("33"), 16))%256);
			if(dataChar.length()==1){
				dataChar="0"+dataChar;
			}
			result = result + dataChar;
		}
		return result;
	}

	/**
	 * 接收数据每字节-33
	 * 
	 * @param data
	 * @return
	 */
	public static String dataDown(String data) {
		String result = "";
		for (int i = 0; i < data.length(); i = i + 2) {
			String temp;
			temp = Integer
				.toHexString(Integer.parseInt(data.substring(i, i + 2), 16) 
					- Integer.parseInt(("33"), 16));
			if (temp.length() == 1) {
				temp = "0" + temp;
			}
			//如果减成负数后会转为"FFFFFFFF"的字符串
			else if(temp.length()>2){
				temp=temp.substring(6, 8);
			}
			result = result + temp;
		}
		return result;
	}

	/**
	 * 编码高低位倒装
	 * 
	 * @param code
	 * @param length
	 * @return
	 */
	public static String inverted(String hexStr) {
		char[] out = new char[hexStr.length()];
		char[] in = hexStr.toCharArray();
		int i = 0;
		for (; i < hexStr.length(); i = i + 2) {
			out[i] = in[hexStr.length() - i - 2];
			out[i + 1] = in[hexStr.length() - i - 1];
		}
		for (; i < hexStr.length(); i++) {
			out[i] = '0';
		}
		return String.valueOf(out);
	}
	
	/**
	 * 编码高低位倒装
	 * 有固定长度要求，不足的补0
	 * @param code
	 * @param length
	 * @return
	 */
	public static String inverted(String hexStr,int length) {
		char[] out = new char[length];
		char[] in = hexStr.toCharArray();
		int i = 0;
		for (; i < hexStr.length(); i = i + 2) {
			out[i] = in[hexStr.length() - i - 2];
			out[i + 1] = in[hexStr.length() - i - 1];
		}
		for (; i < length; i++) {
			out[i] = '0';
		}
		return String.valueOf(out);
	}

	/**
	 * 数组转化成16进制
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2hex(byte[] bytes) {
		final String HEX = "0123456789abcdef";
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			// 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
			sb.append(HEX.charAt((b >> 4) & 0x0f));
			// 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
			sb.append(HEX.charAt(b & 0x0f));
		}
		return sb.toString();
	}

	/**
	 * 16进制的字符串表示转成字节数组
	 * 
	 * @param hexString
	 *            16进制格式的字符串
	 * @return 转换后的字节数组
	 **/
	public static byte[] toByteArray(String hexString) {
		hexString = hexString.toLowerCase();
		final byte[] byteArray = new byte[hexString.length() / 2];
		int k = 0;
		for (int i = 0; i < byteArray.length; i++) {
			// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
			byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
			byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
			byteArray[i] = (byte) (high << 4 | low);
			k += 2;
		}
		return byteArray;
	}
	
	/**
	 * 16进制字符串转2进制字符串
	 * @param hexString
	 * @return
	 */
	public static String toByteStr(String hexString) {
		hexString = hexString.toUpperCase();
		String byteStr="";
		int k = 0;
		for (int i = 0; i < hexString.length(); i++) {
			 k=hexStrTemp.indexOf(String.valueOf(hexString.charAt(i)));
			 byteStr=byteStr.concat(binaryArray[k]);
		}
		return byteStr;
	}  
	
	/**
	 * 2进制字符串转16进制字符串
	 * @param byteStr
	 * @return
	 */
	public static String toHexStr(String byteStr) {
		String hexStr="";
		int k = 0;
		String temp = "";
		for (int i = 0; i+4 <= byteStr.length(); i+=4) {
			temp = byteStr.substring(i, i+4);
			k = Integer.parseInt(temp, 2);
			hexStr += String.valueOf(hexStrTemp.charAt(k));
		}
		return hexStr;
	}
	
	public static String getBooleanArray(byte[] bs) {
		String ret = "";
		for (byte b : bs){
			ret = ret + byteToBit(b);
		}
		ret = ret.substring(ret.indexOf(",") + 1);
		return ret;
    }  
	
	public static String byteToBit(byte b) {  
        return ","  
                + (byte) ((b >> 7) & 0x1) + "," + (byte) ((b >> 6) & 0x1)  + "," 
                + (byte) ((b >> 5) & 0x1) + "," + (byte) ((b >> 4) & 0x1)  + ","
                + (byte) ((b >> 3) & 0x1) + "," + (byte) ((b >> 2) & 0x1)  + ","
                + (byte) ((b >> 1) & 0x1) + "," + (byte) ((b >> 0) & 0x1);  
    }  
	
	/**
	 * 字节转数位字符串
	 * @param bs
	 * @param mergeNum
	 * @return
	 */
	public static String byteToBits(byte[] bs, int[] mergeNum) {

		String bits = "";
		int a = 0;
		byte b = bs[a];
		for (int i = bs.length * 8 - 1; i >= 0; i--) {

			int index = i % 8;

			boolean isMerge = false;

			if (mergeNum != null) {
				for (int merge : mergeNum) {
					if ((bs.length * 8 - i - 1) == merge) {
						isMerge = true;
					}
				}
			}

			if (!isMerge) {
				bits = bits + "," + (byte) ((b >> index) & 0x1);
			} else {
				bits = bits + (byte) ((b >> index) & 0x1);
			}

			if (i % 8 == 0) {
				a++;
				if (a >= bs.length) {
					break;
				}
				b = bs[a];
			}
		}

		bits = bits.substring(bits.indexOf(",") + 1);

		return bits;

	}

	/**
	 * 计算DLT645校验位
	 * 
	 * @param data
	 * @return
	 */
	public static String checkDLT645(String data) {
		if (data == null || data.equals("")) {
			return "";
		}
		int total = 0;
		int len = data.length();
		int num = 0;
		while (num < len) {
			String s = data.substring(num, num + 2);
			total += Integer.parseInt(s, 16);
			num = num + 2;
		}
		// 用256求余最大是255，即16进制的FF
		int mod = total % 256;
		String hex = Integer.toHexString(mod);
		len = hex.length();
		// 如果不够校验位的长度，补0,这里用的是两位校验
		if (len < 2) {
			hex = "0" + hex;
		}
		return hex;
	}
	
	/**
	 * 字符串补0
	 * 
	 * @param length
	 * @return
	 */
	public static String strAddZero(String str, int length) {
		char[] zeroStr = new char[length - str.length()];
		for (int i = 0; i < length - str.length(); i++) {
			zeroStr[i] = '0';
		}
		return String.valueOf(zeroStr) + str;
	}

}
