package cn.com.tw.engine.core.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 定义指定信息对象
 * @author admin
 *
 */
public class CmdRequest implements Serializable{
	
	private static final long serialVersionUID = 8562777871995782183L;

	/**
	 * 10表示采集指令优先级最低，20表示手动触发指令（可以由20,21,22...表示）
	 */
	private int cmdLvl;
	
	private String gwId;
	
	private String protocolType;
	
	private String meterAddr;
	
	private String meterType;
	
	private String content;
	
	private boolean isFollow = false;
	
	private Date date = null;
	
	private Date collecDate = null;
	
	private int sendNum = 0;

	public String getMeterAddr() {
		return meterAddr;
	}

	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}

	public String getMeterType() {
		return meterType;
	}

	public void setMeterType(String meterType) {
		this.meterType = meterType;
	}
	
	public int getCmdLvl() {
		return cmdLvl;
	}

	public void setCmdLvl(int cmdLvl) {
		this.cmdLvl = cmdLvl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public int getSendNum() {
		return sendNum;
	}

	public void setSendNum(int sendNum) {
		this.sendNum = sendNum;
	}

	public String getGwId() {
		return gwId;
	}

	public void setGwId(String gwId) {
		this.gwId = gwId;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof CmdRequest){
			
			if (this.cmdLvl == ((CmdRequest) obj).getCmdLvl() && this.content.equals(((CmdRequest) obj).getContent()) /*&& this.date.equals(((CmdRequest) obj).getDate())*/){
				return true;
			}
			
			return false;
			
		}
		
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return "CmdRequest [cmdLvl=" + cmdLvl + ", gwId=" + gwId
				+ ", protocolType=" + protocolType + ", meterAddr=" + meterAddr
				+ ", meterType=" + meterType + ", content=" + content
				+ ", isFollow=" + isFollow + ", date=" + date + ", collecDate="
				+ collecDate + ", sendNum=" + sendNum + "]";
	}

	public Date getCollecDate() {
		return collecDate;
	}

	public void setCollecDate(Date collecDate) {
		this.collecDate = collecDate;
	}

	public boolean getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(boolean isFollow) {
		this.isFollow = isFollow;
	}

}
