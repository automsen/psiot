package cn.com.tw.engine.core.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.protocol.ParseEnum;
import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.utils.ByteUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.engine.core.debug.DebugManager;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.exception.code.EngineCode;
import cn.com.tw.engine.core.handler.eum.ProtocolEum;
import cn.com.tw.engine.core.utils.CRCCheck;

public class MonBusProtocolDecoder extends ByteToMessageDecoder{

	private static Logger logger = LoggerFactory.getLogger(MonBusProtocolDecoder.class);
	
	//private String header = "FEFEFEFE";
	//01 03 02 00 01 79 84
	//(0103)length + (02)length + (0001)length + (7984)length
	private int baseLength = 2 + 1 + 2;
	
	@Override
	protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> obj) throws Exception {

		try {
			logger.info("enter into method decode(). decoding monbus protocol data");
			
			int readableLength = byteBuf.readableBytes();
			logger.info("readableBytes legnth = " + readableLength);
			if (readableLength < baseLength){
				return;
			}
			
			CmdResponse rep = new CmdResponse();
			byte[] allDataByte = null;
			String compCheckHexStr = "";
			
			//记录包头开始的index
			//获取设备号，code
			while (true) {
				
				int beginReader = byteBuf.readerIndex();
				
				byteBuf.markReaderIndex();
				
				byte[] termNo = new byte[1];
				byteBuf.readBytes(termNo);
				
				byte[] codeByte = new byte[1];
				byteBuf.readBytes(codeByte);
				
				String codeStr = ByteUtils.bytes2hex(codeByte);
				
				byte[] termNoAndCodeBytes = new byte[2];
				termNoAndCodeBytes[0] = termNo[0];
				termNoAndCodeBytes[1] = codeByte[0];
				logger.debug("termNoAndCodeBytes: " + ByteUtils.bytes2hex(termNoAndCodeBytes));
				
				//如果是 “05” ,标准格式 "表号" + "控制码" + "内容" + "校验位"
				//如果是“03”，标准格式"表号" + "控制码" + "内容长度" + "内容" + "校验位"
				if ("05".equals(codeStr)) {
					
					//如果剩余的长度 小于 计算的长度，则数据帧不完整重新读取
					if(byteBuf.readableBytes() < 4 + 2){
						//还原读指针
						byteBuf.readerIndex(beginReader);
						return;
					}
					
					//读取数据字节
					byte[] dataBytes = new byte[4];
					byteBuf.readBytes(dataBytes);
					
					//读取校验位字节
					byte[] checkBytes = new byte[2];
					byteBuf.readBytes(checkBytes);
					
					String checkHexStr = ByteUtils.bytes2hex(checkBytes);
					
					//获取除了校验位的其他数据
	                allDataByte = new byte[2 + 4];
					System.arraycopy(termNoAndCodeBytes, 0, allDataByte, 0, 2);
					System.arraycopy(dataBytes, 0, allDataByte, 2, dataBytes.length);
					compCheckHexStr = CRCCheck.makeCRC(allDataByte);
					
					//判断整个读取的数据是否完整,校验校验位是否正确，如果不正确，返回首位，从第二位开始
					if (!checkHexStr.equals(compCheckHexStr)) {
						//未读到包头，略过一个字节
						byteBuf.resetReaderIndex();
						byteBuf.readByte();
						continue;
					}
					
					break;
				}else {
					//获取数据长度字节
					byte[] dataLenthBytes = new byte[1];
					byteBuf.readBytes(dataLenthBytes);
					String legHex = ByteUtils.bytes2hex(dataLenthBytes);
					int length = Integer.parseInt(legHex, 16);
					
					//如果剩余的长度 小于 计算的长度，则数据帧不完整重新读取
					if(byteBuf.readableBytes() < length + 2){
						//还原读指针
						byteBuf.readerIndex(beginReader);
						return;
					}
					
					//读取数据字节
					byte[] dataBytes = new byte[length];
					byteBuf.readBytes(dataBytes);
					
					//读取校验位字节
					byte[] checkBytes = new byte[2];
					byteBuf.readBytes(checkBytes);
					
					String checkHexStr = ByteUtils.bytes2hex(checkBytes);
					
					//获取除了校验位的其他数据
					allDataByte = new byte[2 + 1 + length];
					System.arraycopy(termNoAndCodeBytes, 0, allDataByte, 0, 2);
					System.arraycopy(dataLenthBytes, 0, allDataByte, 2, 1);
					System.arraycopy(dataBytes, 0, allDataByte, 3, dataBytes.length);
					
					compCheckHexStr = CRCCheck.makeCRC(allDataByte);
					
					//判断整个读取的数据是否完整,校验校验位是否正确，如果不正确，返回首位，从第二位开始
					if (!checkHexStr.equals(compCheckHexStr)) {
						byteBuf.readerIndex(beginReader);
						//滤过一个字节
						byteBuf.readByte();
						continue;
					}
					
					break;
				}
			}
			
			String data = allDataByte == null ? "" : ByteUtils.bytes2hex(allDataByte) + compCheckHexStr;
			logger.info("decode hex string: " + data);
			//暂时加上这，用来调测
			DebugManager.build().put(">> " + data);
			
			Dlt645Data modBusData = decoder(data);
			
			//如果读取数据是完整的，则返回
			rep.setStatusCode(ResultCode.OPERATION_IS_SUCCESS);
			rep.setData(modBusData);
			rep.setUniqueId(modBusData.getAddr() + ":" + new Date().getTime());
			obj.add(rep);
			
			logger.info("exit out method decode(). decodeing modbus protocol data completed!!! Object data = " + (rep == null ? "" : rep.toString()));
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			logger.error("decode data error, ModbusProtocolException, e = {}", e);
			e.printStackTrace();
			CmdResponse rep=new CmdResponse();
			rep.setStatusCode(e.getCode());
			rep.setMessage(e.getMessage());
			obj.add(rep);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("decode data error, Exception, e = {}", e);
			e.printStackTrace();
			CmdResponse rep=new CmdResponse();
			rep.setStatusCode(EngineCode.CHANNEL_PARSE_ERROR);
			rep.setMessage("protocol data decoder error");
			obj.add(rep);
		} finally {
			
		}
		
		
		//判断byteBuf为什么协议数据
		//如果判断是该协议，将协议解析转成obj对象
		//如果判断不是该协议，执行 ctx.fireChannelRead(in); 
       
	}
	
	private Dlt645Data decoder(String data){
		Dlt645Data modBusData = new Dlt645Data();
		String addHex = data.substring(0,2);
		modBusData.setAddr(String.valueOf(Integer.parseInt(addHex, 16)));
		String controlCode = data.substring(2, 4);
		modBusData.setCotrolCode(controlCode);
		
		if ("03".equals(controlCode)) {
			String dataLength = data.substring(4, 6);
			int length = Integer.parseInt(dataLength, 16);
			modBusData.setDataLength(length);
			
			String dataValue = data.substring(6, 6 + length * 2);
			modBusData.setDataValues(dataValue);
			modBusData.setSuccess(true);
			modBusData.setProtocolType(ParseEnum.MODBUS);
			return modBusData;
		} else if ("05".equals(controlCode)) {
			String dataMarker = data.substring(4, 8);
			int dataMarkerInt = Integer.parseInt(dataMarker, 16);
			modBusData.setDataMarker(String.valueOf(dataMarkerInt));
			String dataContent = data.substring(8, 12);
			modBusData.setDataValues(dataContent);
			modBusData.setSuccess(true);
			modBusData.setProtocolType(ParseEnum.MODBUS);
			return modBusData;
		} else {
			String dataLength = data.substring(4, 6);
			int length = Integer.parseInt(dataLength, 16);
			modBusData.setDataLength(length);
			
			String dataValue = data.substring(6, 6 + length * 2);
			modBusData.setDataValues(dataValue);
			modBusData.setSuccess(true);
			modBusData.setProtocolType(ParseEnum.MODBUS);
			return modBusData;
		}
		
		
	}

}
