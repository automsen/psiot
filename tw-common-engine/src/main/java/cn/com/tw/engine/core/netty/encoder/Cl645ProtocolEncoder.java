package cn.com.tw.engine.core.netty.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.tw.common.protocol.ParseEnum;
import cn.com.tw.common.protocol.ParseFactory;
import cn.com.tw.common.protocol.ParseOpera;
import cn.com.tw.common.protocol.utils.ByteUtils;
import cn.com.tw.engine.core.debug.DebugManager;
import cn.com.tw.engine.core.entity.CmdRequest;

public class Cl645ProtocolEncoder extends ChannelOutboundHandlerAdapter{

	private static Logger logger = LoggerFactory.getLogger(Cl645ProtocolEncoder.class);
	
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		try {
			
			logger.debug("enter into method encode() . encoding 645 protocol data. data = " + (msg != null ? msg.toString() : null));
			
			CmdRequest cmd = (CmdRequest)msg;
			ParseOpera opera = ParseFactory.build(ParseEnum.DLT645V2007);
			String content = cmd.getContent();
			String[] contents = content.split(",");
			logger.info("get encode params contents is " + Arrays.toString(contents));
			byte[] codeBuf = opera.encode(contents);
			logger.info("get encode byte data is " + Arrays.toString(codeBuf));
			
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

}
