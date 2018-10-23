package cn.com.tw.paas.monit.entity.business.read;

import java.io.Serializable;
import java.util.Date;

/**
 * 历史读数扩展实体
 * 用于返回数据给接口
 * @author Administrator
 *
 */
public class ReadHistorySimple implements Serializable{
    /**
     * 仪表通讯地址
     */
    private String meterAddr;

    /**
     * 数据项编码（英文）
     */
    private String itemCode;

    /**
     * 数据项名称(中文)
     */
    private String itemName;

    /**
     * 读数
     */
    private String readValue;
    
    /**
     * 读取时间
     */
    private Date readTime;
	private static final long serialVersionUID = -4891679890557195532L;
	public String getMeterAddr() {
		return meterAddr;
	}
	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getReadValue() {
		return readValue;
	}
	public void setReadValue(String readValue) {
		this.readValue = readValue;
	}
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
}
