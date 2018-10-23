package cn.com.tw.saas.serv.entity.room;

import java.math.BigDecimal;
import java.util.Date;

public class RoomAllowanceInfo extends Room{

	/**
	 * 上次执行时间
	 */
	private Date lastExcuteTime;
	
	/**
	 * 上次补助量
	 */
	private BigDecimal lastVolume;
	
	/**
	 * 下次执行时间
	 */
	private Date nextExcuteTime;

	public Date getLastExcuteTime() {
		return lastExcuteTime;
	}

	public void setLastExcuteTime(Date lastExcuteTime) {
		this.lastExcuteTime = lastExcuteTime;
	}

	public BigDecimal getLastVolume() {
		return lastVolume;
	}

	public void setLastVolume(BigDecimal lastVolume) {
		this.lastVolume = lastVolume;
	}

	public Date getNextExcuteTime() {
		return nextExcuteTime;
	}

	public void setNextExcuteTime(Date nextExcuteTime) {
		this.nextExcuteTime = nextExcuteTime;
	}
}
