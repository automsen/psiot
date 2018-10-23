package cn.com.tw.engine.core.entity.seria;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public abstract class Serialize {
	
	public static final Charset CHARSET = Charset.forName("UTF-8");
	
	private ByteBuf writeBuf = Unpooled.buffer();
	
	private ByteBuf readBuf = Unpooled.buffer();
	
	public abstract void write();
	
	public abstract void read();
	
	public void writeInt(int content){
		this.writeBuf.writeInt(content);
	}
	
	public void writeLong(long content){
		this.writeBuf.writeLong(content);
	}
	
	public void writeShort(short content){
		this.writeBuf.writeShort(content);
	}
	
	public void writeByte(byte content){
		this.writeBuf.writeByte(content);
	}
	
	public void writeBytes(byte[] content){
		this.writeBuf.writeBytes(content);
	}
	
	public void writeString(String content){
		
		if(null == content || "".equals(content)){
			writeShort((byte)0);
			return;
		}
		
		byte[] bytes = content.getBytes(CHARSET);
		writeShort((short) bytes.length);
		writeBytes(bytes);
	}
	
	public int readInt(){
		return this.readBuf.readInt();
	}
	
	public long readLong(){
		return this.readBuf.readLong();
	}
	
	public short readShort(){
		return this.readBuf.readShort();
	}
	
	public String readString(boolean isReverse){
		short cNum = readShort();
		if(cNum == 0){
			return null;
		}
		
		byte[] bytes = new byte[cNum];
		this.readBuf.readBytes(bytes);
		
		String retStr = new String(bytes);
		if(isReverse){
			return new StringBuilder(retStr).reverse().toString();
		}
		
		return retStr;
	}
	
	public String readString(short sNum, boolean isReverse){
		if(sNum == 0){
			return null;
		}
		
		byte[] bytes = new byte[sNum];
		this.readBuf.readBytes(bytes);

		String retStr = new String(bytes);
		if(isReverse){
			return new StringBuilder(retStr).reverse().toString();
		}
		
		return retStr;
	}
	
	public void byte2Obj(byte[] bytes){
		this.readBuf.writeBytes(bytes);
		read();
	}
	
	public Serialize readBytes(byte[] bytes){
		//ByteBuf byteBuf = readBuf.writeBytes(bytes);
		
		return null;
	}
	
	public byte[] obj2byte(){
		write();
		int index = writeBuf.writerIndex();
		if(index == 0){
			return new byte[0];
		}else{
			byte[] bytes = new byte[index];
			writeBuf.readBytes(bytes);
			return bytes;
		}
		
	}
	
	public void writeObject(Object object){
		
		if(object == null){
			writeByte((byte)0);
		}else{
			if (object instanceof Integer) {
				writeInt((Integer) object);
			}
			
			if (object instanceof Long) {
				writeLong((Long) object);
			}
			
			if (object instanceof Short) {
				writeShort((Short) object);
			}
			
			if (object instanceof Byte) {
				writeByte((Byte) object);
			}
			
			if (object instanceof String) {
				String value = (String) object;
				writeString(value);
			}
			throw new RuntimeException("不可序列化的类型:" + object.getClass());
		}
	}
	
	public void obj2Byte(){
		//ByteBufUtil.
		
	}

}
