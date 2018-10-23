package cn.com.tw.engine.core.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.protocol.utils.ByteUtils;
import cn.com.tw.engine.core.debug.DebugManager;
import cn.com.tw.engine.core.entity.CmdRequest;
import cn.com.tw.engine.core.utils.CRCCheck;

public class MonBusProtocolEncoder extends ChannelOutboundHandlerAdapter{

	private static Logger logger = LoggerFactory.getLogger(MonBusProtocolEncoder.class);
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		try {
			
			logger.debug("enter into method encode() . encoding modbus protocol data. data = " + (msg != null ? msg.toString() : null));
			
			CmdRequest cmd = (CmdRequest)msg;
			//ParseOpera opera = ParseFactory.build(ParseEnum.DL6452007);
			//content : 表号，功能码（03），数据标识（205）, 读取寄存器的位数（0001），校验位
			String content = cmd.getContent();
			String[] contents = content.split(",");
			byte[] codeBuf = content(contents);
			logger.info("get encode params contents is " + Arrays.toString(contents));
			//byte[] codeBuf = opera.encode(contents);
			//logger.info("get encode byte data is " + Arrays.toString(codeBuf));
			String hexCode = ByteUtils.bytes2hex(codeBuf);
			logger.info("get encode hex data is " + hexCode);
			
			//方便调测暂时放在这
			DebugManager.build().put("<< " + hexCode);
			
			ByteBuf buf = Unpooled.buffer();
			buf.writeBytes(codeBuf);
			ctx.write(buf, promise);
			ctx.flush();
			logger.debug("exit out method encode() . encode 645 protocol data completed !");
		} catch (Exception e) {
			logger.error("encode data error !, e = ", e);
			e.printStackTrace();
		}
	}
	
	private byte[] content(String... contents){
		
		String termNoHexStr = StringUtils.leftPad(Integer.toHexString(Integer.parseInt(contents[0])), 2, "0");
		
		String funcHexStr = StringUtils.leftPad(contents[1], 2, "0");
		
		String markerHexStr = StringUtils.leftPad(Integer.toHexString(Integer.parseInt(contents[2])), 4, "0").toUpperCase();
		
		String hexContent = "";
		if ("05".equals(funcHexStr)) {
			hexContent = StringUtils.leftPad(contents[3], 4, "0").toUpperCase();
		} else {
			hexContent = StringUtils.leftPad(Integer.toHexString(Integer.parseInt(contents[3])), 4, "0").toUpperCase();
		}
		
		StringBuffer sb = new StringBuffer(termNoHexStr).append(funcHexStr).append(markerHexStr).append(hexContent);
		byte[] dataAll = ByteUtils.toByteArray(sb.toString());
		
		String check = getCrc(dataAll);
		sb.append(check);
		return ByteUtils.toByteArray(sb.toString());
		
	}
	
	 /**
     * 计算CRC16校验码
     *
     * @param bytes 字节数组
     * @return {@link String} 校验码
     * @since 1.0
     */
	private static String getCrc(byte[] data) {
		return CRCCheck.makeCRC(data);
	}

	
}
