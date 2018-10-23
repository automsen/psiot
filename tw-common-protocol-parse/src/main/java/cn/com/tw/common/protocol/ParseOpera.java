package cn.com.tw.common.protocol;

public interface ParseOpera {
	
	byte[] encode(Object... params);
	
	Object decode(byte[] bytes);

}
