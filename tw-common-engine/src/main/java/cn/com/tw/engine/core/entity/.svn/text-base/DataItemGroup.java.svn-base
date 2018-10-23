package cn.com.tw.engine.core.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 数据项和群主关系
 * @author admin
 *
 */
public class DataItemGroup implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 数据项标识
	 */
	private String dataItemNum;
	
	/**
	 * 数据项名称
	 */
	private String dataItemName;
	
	/**
	 * 采集频率，为cronexpresson表达式
	 */
	private String collectCron;
	
	/**
	 * 是否支持日采集
	 */
	private boolean isDayCollect = false;
	
	/**
	 * 是否支持月采集
	 */
	private boolean isMonthCollect = false;
	
	/**
	 * 是否为后续针
	 */
	private boolean isFollow = false;
	
	/**
	 * 打包间隔时间默认5分钟
	 */
	private int packIntervalTime = 5; 
	
	/**
	 * 仪表组
	 */
	private List<Meter> meters;
	
	public boolean getIsDayCollect() {
		return isDayCollect;
	}

	public void setIsDayCollect(boolean isDayCollect) {
		this.isDayCollect = isDayCollect;
	}

	public boolean getIsMonthCollect() {
		return isMonthCollect;
	}

	public void setIsMonthCollect(boolean isMonthCollect) {
		this.isMonthCollect = isMonthCollect;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj){
			return true;
		}
		
		if (obj instanceof DataItemGroup){
			obj = (DataItemGroup)obj;
			if (this.getDataItemNum().equals(((DataItemGroup) obj).getDataItemNum())){
				return true;
			}
		}
		
		return false;
	}

	public List<Meter> getMeters() {
		return meters;
	}

	public void setMeters(List<Meter> meters) {
		this.meters = meters;
	}
	
	public String getDataItemNum() {
		return dataItemNum;
	}

	public void setDataItemNum(String dataItemNum) {
		this.dataItemNum = dataItemNum;
	}

	public String getDataItemName() {
		return dataItemName;
	}

	public void setDataItemName(String dataItemName) {
		this.dataItemName = dataItemName;
	}

	public String getCollectCron() {
		return collectCron;
	}

	public void setCollectCron(String collectCron) {
		this.collectCron = collectCron;
	}

	@Override
	public String toString() {
		return "DataItemGroup [dataItemNum=" + dataItemNum + ", dataItemName="
				+ dataItemName + ", collectCron=" + collectCron
				+ ", isDayCollect=" + isDayCollect + ", isMonthCollect="
				+ isMonthCollect + ", isFollow=" + isFollow
				+ ", packIntervalTime=" + packIntervalTime + ", meters="
				+ meters + "]";
	}

	public boolean getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(boolean isFollow) {
		this.isFollow = isFollow;
	}

	public int getPackIntervalTime() {
		return packIntervalTime;
	}

	public void setPackIntervalTime(int packIntervalTime) {
		this.packIntervalTime = packIntervalTime;
	}


}
