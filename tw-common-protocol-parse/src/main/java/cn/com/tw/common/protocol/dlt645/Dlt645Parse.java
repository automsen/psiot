package cn.com.tw.common.protocol.dlt645;

import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

import cn.com.tw.common.protocol.ParseEnum;
import cn.com.tw.common.protocol.ParseOpera;
import cn.com.tw.common.protocol.dlt645.application.CurveRead;
import cn.com.tw.common.protocol.dlt645.application.CurveReadFollow;
import cn.com.tw.common.protocol.dlt645.application.EncryptChargeElec;
import cn.com.tw.common.protocol.dlt645.application.EncryptControl;
import cn.com.tw.common.protocol.dlt645.application.EncryptWrite;
import cn.com.tw.common.protocol.dlt645.application.LongRead;
import cn.com.tw.common.protocol.dlt645.application.SimpleControl;
import cn.com.tw.common.protocol.dlt645.application.SimpleRead;
import cn.com.tw.common.protocol.dlt645.application.SimpleWrite;
import cn.com.tw.common.protocol.dlt645.cons.Dlt645ControlCode;
import cn.com.tw.common.protocol.dlt645.cons.Dlt645DataMarker;
import cn.com.tw.common.protocol.dlt645.cons.InstructionFormat;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.exception.code.ProtocolCode;
import cn.com.tw.common.protocol.utils.ByteUtils;
import cn.com.tw.common.protocol.utils.EnumHelperUtil;

/**
 * DLT645_2007协议解析 
 * 编码=前导+头+地址域(高低倒装)+头+控制码+数据长度+数据域(单项内部高低倒装，整体+33)+校验位+尾
 * code=pre+head+addr+controlCode+dataLength+data+cs+tail
 * 数据域=数据标识+密码+操作者代码+数据项(都是非必须) 
 * data=dataMarker+password+operatorCode+dataValues
 * 
 * @author Cheng Qi Peng
 *
 */
public class Dlt645Parse implements ParseOpera {
	
	/**
	 * 前导
	 */
	private static final String pre = "FEFEFEFE";

	/**
	 * 头
	 */
	private static final String head = "68";

	/**
	 * 尾
	 */
	private static final String tail = "16";

	private static final String methodName = "getValue";
	/**
	 * 加密的基础密钥
	 */
	private static final String sKey = "8317227ce8dd";

	private static HashMap<String, String> simpleFormat = InstructionFormat.simpleFormatMap();
	
	private static HashMap<String, String> curveFormat = InstructionFormat.curveFormatMap();

	private static HashMap<String, String> longFormat = InstructionFormat.longFormatMap();

	
	/**
	 * 编码 
	 * 参数1为仪表地址 
	 * param one is meterAddr 
	 * 参数2为控制码 
	 * param two is controlCode
	 * 参数3以后的为其它数据，根据操作类型可能有0~n个 
	 * other params(3~n) is dataParams(0~n) 
	 * 至少有参数1、2
	 * params must have two param
	 */
	@Override
	public byte[] encode(Object... params) {
		if (params.length < 2) {
			throw new ProtocolException(ProtocolCode.PARAM_MISSING, "param missing");
		}
		// 获取仪表地址
		String meterAddr = (String) params[0];
		if (meterAddr.length() > 12) {
			throw new ProtocolException(ProtocolCode.PROTOCOL_VALID, "address length too long");
		}
		// 高低位倒装地址域(6字节)
		String addr = ByteUtils.inverted(meterAddr, 12);
		// 编码控制码与数据域
		String cotrolAndData = enCotrolAndData(params);
		String temp = head + addr + head + cotrolAndData;
		// 校验位(1字节)
		String cs = ByteUtils.checkDLT645(temp);
		String code = (pre + temp + cs + tail).toUpperCase();
		// 输出结果
		System.out.println("编码完成\ncode=" + code);
		return ByteUtils.toByteArray(code);
	}

	/**
	 * 编码控制码与数据域 
	 * controlCode+dataLength+data 
	 * 数据域=数据标识+密码+操作者代码+数据项(都是非必须)
	 * data=dataMarker+password+operatorCode+dataValues
	 * 
	 * @param params
	 * @return
	 */
	private static String enCotrolAndData(Object... params) {
		// 控制码(占1字节)
		String controlCode = (String) params[1];
		controlCode = controlCode.toUpperCase();
		String temp = "";
		String methodName = "getValue";
		Dlt645ControlCode controlEnum =EnumHelperUtil.getByStringTypeCode(Dlt645ControlCode.class, methodName, controlCode);
		switch (controlEnum){
		// 读（11）
		case READ:{
			String dataMarker = (String) params[2];
			// 常规读取类指令
			if(params.length == 3||(dataMarker.equals("04700107")||dataMarker.equals("04700108"))){
				temp = SimpleRead.send(params);
			}
			// 曲线数据读取类指令
			else if (params.length == 5){
				temp = CurveRead.send(params);
			}
		};break;
		// 读后续（12）
		case READ_FOLLOW:{
			temp = CurveReadFollow.send(params);
		};break;
		// 常规控制（1C）
		case CONTROL:{
			temp = SimpleControl.send(params);
		};break;
		// 不加密写（14）
		case WRITE:{
			// 数据标识
			String dataMarker = (String) params[2];
			// 集中器主动上传配置专用编码
			if(dataMarker.equals("04700102")||dataMarker.equals("04700103")
					||dataMarker.equals("04700104")||dataMarker.equals("04700105")
					||dataMarker.equals("04700106")
					||dataMarker.equals("04700109")||dataMarker.equals("0470010A")
					||dataMarker.equals("04700201")||dataMarker.equals("04700204")){
				temp = SimpleWrite.sendConcentrator(params);
			}
			// 深圳事件上报专用编码
			else if(dataMarker.equals("04001104")||dataMarker.equals("04001503")){
				temp = SimpleWrite.sendEvent(params);
			}else {
				temp = SimpleWrite.send(params);
			}
		};break;
		// 加密（03）
		case ENCRYPT:{
			// 获取仪表地址
			String meterAddr = (String) params[0];
			String dataMarker = (String) params[2];
			// 加密控制类
			if (dataMarker.substring(0, 6).equals(Dlt645DataMarker.EncryptControl.getValue())) {
				temp = EncryptControl.send(params);
			}
			// 加密费控类
			else if (dataMarker.substring(0, 4).equals(Dlt645DataMarker.EncryptPay.getValue())) {
				temp = EncryptChargeElec.send(params);
			}
			// 电表计量重置
			else if (dataMarker.toUpperCase().equals("AAAAC119")){
				temp = EncryptControl.reset(params);
			}
			// 加密写
			else {
				temp = EncryptWrite.send(params);
			}
			// 整体加密
			temp = ByteUtils.EncryptStr(temp, sKey, meterAddr);			
		};break;
		// 登录与心跳回复消息（91）
		case READ_OK:{
			temp = (String) params[2];
			if(temp.indexOf("#")>=0){
				temp = temp.substring(temp.indexOf("#")+1);
			}
			else {
				temp = "";
			}			
		};break;
		case MODBUSDOWN:{
			temp = (String) params[2];
		}
		default:
			break;
		}
		// 数据长度(字节数)(占1字节)
		String dataLength = Integer.toHexString(temp.length() / 2);
		if (dataLength.length() == 1) {
			dataLength = "0" + dataLength;
		}
		// 数据标识与数据长度变，数据域每字节+33
		return controlCode + dataLength + ByteUtils.dataUp(temp);
	}

	/**
	 * 解码
	 */
	@Override
	public Object decode(byte[] bytes) {
		String code = ByteUtils.bytes2hex(bytes);
//		logger.info("bytes to hexStr : " + code);
		
		Dlt645Data result = new Dlt645Data();
		result.setProtocolType(ParseEnum.DLT645V2007);
		int start = code.indexOf(head);
		int middle = code.lastIndexOf(head);
		int end = code.lastIndexOf(tail);
		// 校验开头中间结尾
		if (start < 0 || end < 0 || middle < 0 || start == middle) {
			throw new ProtocolException(ProtocolCode.PROTOCOL_VALID, "format error");
		} else {
			// 截取编码(保留头"68"，去掉尾"16")
			code = code.substring(start, end);
			// 取校验位
			String cs = code.substring(code.length() - 2);
			// 取其它
			code = code.substring(0, code.length() - 2);
//			logger.debug("check code: " + cs.toString());
			// 验证校验位
			if (!ByteUtils.checkDLT645(code).equals(cs)) {
//				logger.debug("calculate to check code：" + ByteUtils.checkDLT645(code));
				throw new ProtocolException(ProtocolCode.PROTOCOL_VALID, "checkCode error");
			} else {
				// 仪表地址
				String metarAddr = ByteUtils.inverted(code.substring(2, 14), 12);
				// 控制码
				String cotrolCode = code.substring(16, 18);
				// 数据域长度(*2为字符数而非字节数)
				int dataLength = Integer.parseInt(code.substring(18, 20),16) * 2;
				// 数据域
				String dataField = ByteUtils.dataDown(code.substring(20));
				// 校验数据长度
				if (dataLength != dataField.length()) {
					throw new ProtocolException(ProtocolCode.PROTOCOL_VALID, "dataLength error");
				} else {
					result.setAddr(metarAddr);
					result.setCotrolCode(cotrolCode.toUpperCase());
					result.setDataLength(dataLength);
					result.setDataField(dataField);
					result = deDataField(result);
					System.out.println("解码完成 \n" + result.toString());
					return result;
				}
			}
		}
	}

	/**
	 * 解码数据域 
	 * 数据域=数据标识+密码+操作者代码+数据项(都是非必须)
	 * data=dataMarker+password+operatorCode+dataValues
	 * 
	 * @param param
	 * @return
	 */
	private static Dlt645Data deDataField(Dlt645Data param) {
		// 控制码(占1字节)
		String controlCode = param.getCotrolCode();
		// 数据域
		String dataField = param.getDataField();
		// 数据域长度(*2为字符数而非字节数)
		int dataLength = param.getDataLength();

		Dlt645ControlCode controlEnum =EnumHelperUtil.getByStringTypeCode(Dlt645ControlCode.class, methodName, controlCode);
		switch (controlEnum){
		// 读成功（91）
		case READ_OK:{
			// 数据标识
			String dataMarker = ByteUtils.inverted(dataField.substring(0, 8)).toUpperCase();;
			param.setDataMarker(dataMarker);
			String dataValues = dataField.substring(8, dataLength);
			param.setDataValues(dataValues);
			// 智能手环解码
//			if (dataMarker.equals("0460AA00")){
//				param = SmartBand.receive(param);
//			}
			String thisformat = "";
			// 在简单指令中寻找数据标识对应的格式
			thisformat = simpleFormat.get(dataMarker);
			// 确定为简单指令
			if (!StringUtils.isEmpty(thisformat)) {
				param = SimpleRead.receive(param,thisformat);
			}
			// 集中器主动上传配置专用解码
			else if(dataMarker.equals("04700102")||dataMarker.equals("04700103")||dataMarker.equals("04700201")
					||dataMarker.equals("04700107")||dataMarker.equals("04700108")){
				param = SimpleRead.receiveConcentrator(param);
			}
			// 深圳事件上报专用解码
			else if(dataMarker.equals("04001104")||dataMarker.equals("04001501")){
				param = SimpleRead.receiveEvent(param);
			}else {
				// 在曲线指令中寻找数据标识对应的格式
				thisformat = curveFormat.get(dataMarker);
				// 确定为曲线指令
				if (!StringUtils.isEmpty(thisformat)) {
					param = CurveRead.receive(param,thisformat);
				}else {
					throw new ProtocolException(ProtocolCode.UNKNOWN_DATAMARKER, "unknown datamarker");
				}
			}
			param.setSuccess(true);	
		};break;
		// 读异常（D1）
		case READ_ERR:{
			if(dataField.length()>8){
				// 数据标识
				String dataMarker = ByteUtils.inverted(dataField.substring(0, 8)).toUpperCase();
				param.setDataMarker(dataMarker);
				// 错误码
				param.setErrCode(dataField.substring(8));
			}
			else{
				// 错误码
				param.setErrCode(dataField);
			}
			param.setSuccess(false);		
		};break;
		// 读成功有后续（B1）
		case READ_CONTINUE:{
			// 数据标识
			String dataMarker = ByteUtils.inverted(dataField.substring(0, 8)).toUpperCase();;
			param.setDataMarker(dataMarker);
			String dataValues = dataField.substring(8, dataLength);
			param.setDataValues(dataValues);
			String thisformat = "";
			// 在超长指令中寻找数据标识对应的格式
			thisformat = longFormat.get(dataMarker);
			if(!StringUtils.isEmpty(thisformat)){
				param = LongRead.receive(param, thisformat);
			}else {
				// 在曲线指令中寻找数据标识对应的格式
				thisformat = curveFormat.get(dataMarker);
				if(!StringUtils.isEmpty(thisformat)){
					param = CurveRead.receive(param,thisformat);
				}else{
					throw new ProtocolException(ProtocolCode.UNKNOWN_DATAMARKER, "unknown datamarker");
				}
			}		
			param.setSuccess(true);	
		};break;
		// 读后续异常（D2）
		case READ_FOLLOW_ERR:{
			if(dataField.length()>8){
				// 数据标识
				String dataMarker = ByteUtils.inverted(dataField.substring(0, 8)).toUpperCase();
				param.setDataMarker(dataMarker);
				// 错误码
				param.setErrCode(dataField.substring(8));
			}
			else{
				// 错误码
				param.setErrCode(dataField);
			}
			param.setSuccess(false);		
		};break;
		// 读后续有后续（B2）
		case READ_FOLLOW_CONTINUE:{
			// 数据标识
			String dataMarker = ByteUtils.inverted(dataField.substring(0, 8)).toUpperCase();;
			param.setDataMarker(dataMarker);
			String dataValues = dataField.substring(8, dataLength);
			param.setDataValues(dataValues);
			String thisformat = "";
			// 在超长指令中寻找数据标识对应的格式
			thisformat = longFormat.get(dataMarker);
			if(!StringUtils.isEmpty(thisformat)){
				param = LongRead.receiveFollow(param, thisformat);
			}else {
				// 在曲线指令中寻找数据标识对应的格式
				thisformat = curveFormat.get(dataMarker);
				if(!StringUtils.isEmpty(thisformat)){
					param = CurveReadFollow.receive(param,thisformat);
				}else{
					throw new ProtocolException(ProtocolCode.UNKNOWN_DATAMARKER, "unknown datamarker");
				}
			}		
			param.setSuccess(true);			
		};break;
		// 读后续结束（92）
		case READ_FOLLOW_OVER:{
			// 数据标识
			String dataMarker = ByteUtils.inverted(dataField.substring(0, 8)).toUpperCase();;
			param.setDataMarker(dataMarker);
			String dataValues = dataField.substring(8, dataLength);
			param.setDataValues(dataValues);
			String thisformat = "";
			// 在超长指令中寻找数据标识对应的格式
			thisformat = longFormat.get(dataMarker);
			if(!StringUtils.isEmpty(thisformat)){
				param = LongRead.receiveFollow(param, thisformat);
			}else {
				// 在曲线指令中寻找数据标识对应的格式
				thisformat = curveFormat.get(dataMarker);
				if(!StringUtils.isEmpty(thisformat)){
					param = CurveReadFollow.receive(param,thisformat);
				}else{
					throw new ProtocolException(ProtocolCode.UNKNOWN_DATAMARKER, "unknown datamarker");
				}
			}		
			param.setSuccess(true);			
		};break;
		// 写成功（94）
		case WRITE_OK:{
			if (dataField.length()>=8){
				// 数据标识
				String dataMarker = ByteUtils.inverted(dataField.substring(0, 8)).toUpperCase();;
				param.setDataMarker(dataMarker);
				// 特殊指令 rola水表通断返回数据
				if (dataMarker.equals("0460AA02")){
					String dataValues = dataField.substring(8, dataLength);
					param.setDataValues(dataValues);
					param = SimpleWrite.receiveWaterRola(param);
				}
			}
			// 无数据
			param.setSuccess(true);		
		};break;
		// 写异常（D4）
		case WRITE_ERR:{
			if (dataField.length()>=8){
				// 数据标识
				String dataMarker = ByteUtils.inverted(dataField.substring(0, 8)).toUpperCase();;
				param.setDataMarker(dataMarker);
				// 只有错误码
				param.setErrCode(dataField.substring(8));
			}
			else {
				// 只有错误码
				param.setErrCode(dataField);
			}
			param.setSuccess(false);		
		};break;
		// 控制成功（9C）
		case CONTROL_OK:{
			// 无数据
			param.setSuccess(true);		
		};break;
		// 控制异常（DC）
		case CONTROL_ERR:{
			// 只有错误码
			param.setErrCode(dataField.substring(0));
			param.setSuccess(false);		
		};break;
		// 加密成功（83）
		case ENCRYPT_OK:{
			// 数据标识
			String dataMarker = ByteUtils.inverted(dataField.substring(0, 8)).toUpperCase();
			param.setDataMarker(dataMarker);
			String dataValues = dataField.substring(8, dataLength);
			param.setDataValues(dataValues);
			// 3.1加密控制类
			if (dataMarker.substring(0, 6).equals(Dlt645DataMarker.EncryptControl.getValue())) {
				// 无数据
			}
			// 加密费控类
			else if (dataMarker.substring(0, 4).equals(Dlt645DataMarker.EncryptPay.getValue())) {
				param = EncryptChargeElec.receive(param);
			}
			else {
				// 无数据
			}
			param.setSuccess(true);
		};break;
		// 加密异常（C3）
		case ENCRYPT_ERR:{
			// 数据标识
			String dataMarker = ByteUtils.inverted(dataField.substring(0, 8)).toUpperCase();
			param.setDataMarker(dataMarker);
			// 加密控制类
			if (dataMarker.substring(0, 6).equals(Dlt645DataMarker.EncryptControl.getValue())) {
				// 只有错误码
				param.setErrCode(dataField.substring(8));
			}
			// 加密费控类
			else if (dataMarker.substring(0, 4).equals(Dlt645DataMarker.EncryptPay.getValue())) {
				param = EncryptChargeElec.receiveErr(param);
			}
			else{
				// 只有错误码
				param.setErrCode(dataField.substring(8));
			}
			param.setSuccess(false);		
		};break;
		case MODBUSUP:{
			param.setSuccess(true);
		};break;
		case MODBUSUP_ERR:{
			param.setSuccess(false);
		};break;
		default:
			break;
		}
		return param;
	}

}
