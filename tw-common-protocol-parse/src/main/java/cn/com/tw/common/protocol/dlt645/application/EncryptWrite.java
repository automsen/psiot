package cn.com.tw.common.protocol.dlt645.application;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.dlt645.cons.InstructionFormat;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.exception.code.ProtocolCode;
import cn.com.tw.common.protocol.utils.ByteUtils;

/**
 * 加密写数据
 * @author Cheng Qi Peng
 *
 */
public class EncryptWrite {
	
	private static HashMap<String, String> format = InstructionFormat.simpleFormatMap();

	/**
	 * 下发编码
	 * 
	 * @return
	 */
	public static String send(Object... params) throws ProtocolException{
		// 数据标识
		String dataMarker = (String) params[2];
		// 密码
		String password = (String) params[3];
		// 操作者编码(暂无相关需求，可为定长的任意内容)
		String operatorCode = "7e563412";
		//数据域
		String dataField=ByteUtils.inverted(dataMarker) + ByteUtils.inverted(password) + operatorCode;
		String thisformat = "";
		try{//寻找数据标识对应的格式
			thisformat=format.get(dataMarker);
		}
		catch(Exception e){
			throw new ProtocolException(ProtocolCode.UNKNOWN_DATAMARKER,"unknown datamarker");
		}
		//没找到格式
		if(StringUtils.isEmpty(thisformat)){
			throw new ProtocolException(ProtocolCode.UNKNOWN_DATAMARKER,"unknown datamarker");
		}
		else{
			// 获取每个数据项的格式
			String[] formatArray=thisformat.split(",");
			//格式包含的数据项数量
			int formatNum=formatArray.length;
			//参数数量
			int paramNum=params.length-4;
			//参数不匹配
			if(formatNum!=paramNum){
				throw new ProtocolException(ProtocolCode.PARAM_MISSING,"param lose");
			}
			//用于标记dataValues被读取到第几位
//			int flag=0;
			// 将每一个数据项按格式编码
			for(int i=0 ;i<paramNum;i++){
				//数据项
				String item = (String) params[i+4];
				int itemValue;
				//数据项总长度
				int itemLength;
				//判断格式是否包含小数位
				int pointIndex=formatArray[i].indexOf(".");
				if(pointIndex>0){
					itemLength=Integer.valueOf(formatArray[i].substring(0,pointIndex));
					//小数位长度
					int decimalLength=Integer.valueOf(formatArray[i].substring(pointIndex+1));
					//根据小数位数将原数据乘以10的decimalLength次方
					double multiple=Math.pow(10,decimalLength);
					BigDecimal bg = new BigDecimal(item);
					itemValue = bg.setScale(decimalLength, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(multiple)).intValue();
					item= String.valueOf(itemValue);
				}
				else{
					itemLength=Integer.valueOf(formatArray[i]);
				}
				//补0	
				item = strAddZero(item,itemLength);
				item = ByteUtils.inverted(item);
				dataField +=item;
			}
		}
		return dataField;
	}

	/**
	 * 回应解码
	 * 
	 * @param code
	 * @return
	 */
	public static Dlt645Data receive(Dlt645Data param) throws ProtocolException{
		return param;
	}
	
	/**
	 * 字符串补0
	 * @param length
	 * @return
	 */
    private static String strAddZero(String str,int length) {  
        char[] zeroStr = new char[length-str.length()];  
        for (int i = 0; i < length-str.length(); i++) {   
        	zeroStr[i] = '0';  
        }      
        return String.valueOf(zeroStr)+str;  
    }  
}
