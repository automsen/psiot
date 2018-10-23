package cn.com.tw.paas.monit.entity.business.read;

import java.util.Date;

import cn.com.tw.paas.monit.entity.db.read.ReadHistory;

/**
 * 历史读数扩展实体
 * 用于传递查询条件
 * @author Cheng Qi Peng
 *
 */
public class ReadHistoryExtend extends ReadHistory{

	private static final long serialVersionUID = -1078278677596009812L;

	private Date startTime;
	
	private String startTimeStr;
	
	private Date endTime;
	
	private String endTimeStr;
	
	private String longitude;
	
	private String latitude;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
