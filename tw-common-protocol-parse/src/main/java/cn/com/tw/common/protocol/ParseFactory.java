package cn.com.tw.common.protocol;

import cn.com.tw.common.protocol.dlt645.Dlt645Parse;
import cn.com.tw.common.protocol.gehua.GehuaParse;

public class ParseFactory {
	
	public static ParseOpera build(ParseEnum parse){
		
		switch (parse) {
		
		case DLT645V2007:
			return new Dlt645Parse();
			
		case GEHUA:
			return new GehuaParse();

		default:
			break;
		}
		return null;
	}
	
}
