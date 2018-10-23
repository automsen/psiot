package cn.com.tw.engine.core.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.protocol.ParseEnum;
import cn.com.tw.common.protocol.ParseFactory;
import cn.com.tw.common.protocol.ParseOpera;
import cn.com.tw.common.protocol.dlt645.Dlt645Data;
import cn.com.tw.common.protocol.exception.ProtocolException;
import cn.com.tw.common.protocol.utils.ByteUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.engine.core.debug.DebugManager;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.exception.code.EngineCode;

public class Cl645ProtocolDecoder extends ByteToMessageDecoder{

	private static Logger logger = LoggerFactory.getLogger(Cl645ProtocolDecoder.class);
	
	//private String header = "FEFEFEFE";
	//FE FE FE FE 68 81 36 19 12 16 00 68 91 0A 33 74 33 33 33 35 33 33 33 33 A4 16
	private String header = "FE68";
	
	//(FE688136191216006891)length + (0A)length + (33 74 33 33)length + (33 35 33 33 33 33)忽略 + (A416)length
	private int baseLength = 10 + 1 + 2;
	
	@Override
	protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> obj) throws Exception {

		try {
			logger.info("enter into method decode(). decoding 645 protocol data");
			
			int readableLength = byteBuf.readableBytes();
			logger.info("readableBytes legnth = " + readableLength);
			if (readableLength < baseLength){
				return;
			}
			
			//记录包头开始的index
			int beginReader;
			byte[] headerBytes = new byte[2];
			while (true){
				
				beginReader = byteBuf.readerIndex();
				
				byteBuf.markReaderIndex();
				
				byteBuf.readBytes(headerBytes);
				
				String headerStr = ByteUtils.bytes2hex(headerBytes);
				
				if (header.equals(headerStr.toUpperCase())){
					break;
				}
				
				//未读到包头，略过一个字节
				byteBuf.resetReaderIndex();
				byteBuf.readByte();
				
				//长度变得不满足
				if(byteBuf.readableBytes() < baseLength){
					//System.out.println("????? " + byteBuf.readableBytes());
					return;
				}
				
			}
			
			byte headbytes = headerBytes[1];
			
			//获取68
			byte[] code0 = new byte[1];
			code0[0] = headbytes;
			
			int indexOfLength = 8;
			byte[] code1 = new byte[indexOfLength];
			byteBuf.readBytes(code1);
			byte[] code2= new byte[1];
			byteBuf.readBytes(code2);
			//int length=(int)code2[0];
			String legHex = ByteUtils.bytes2hex(code2);
			int length = Integer.parseInt(legHex, 16);
			
			//判断请求数据包数据是否到齐
			if(byteBuf.readableBytes() < length+2){
				//还原读指针
				byteBuf.readerIndex(beginReader);
				return;
			}
			
			byte[] code3=new byte[length+2];
			byteBuf.readBytes(code3);
			byte[] code=new byte[1 + indexOfLength + 1 + length + 2];
			System.arraycopy(code0, 0, code, 0, 1);
			System.arraycopy(code1, 0, code, 1, code1.length);  
			System.arraycopy(code2, 0, code, code1.length + 1, 1); 
			System.arraycopy(code3, 0, code, code1.length +2, code3.length); 
			ParseOpera opera = ParseFactory.build(ParseEnum.DLT645V2007);
			
			String hexCode = ByteUtils.bytes2hex(code);
			
			logger.debug("decode 645 protocol hex data = {}", hexCode);
			//暂时加上这，用来调测
			DebugManager.build().put(">> " + hexCode);
			Dlt645Data data=(Dlt645Data)opera.decode(code);
			if (data == null) {
				throw new ProtocolException("", "decode result data is null!");
			}
			CmdResponse rep=new CmdResponse();
			rep.setStatusCode(ResultCode.OPERATION_IS_SUCCESS);
			rep.setData(data);
			rep.setUniqueId(data.getAddr() + ":" + new Date().getTime());
			obj.add(rep);
			logger.info("exit out method decode(). decodeing 645 protocol data completed!!! Object data = " + (rep == null ? "" : rep.toString()));
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			logger.error("decode data error, DLT645ProtocolException, e = {}", e);
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

}
