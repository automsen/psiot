/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.entity.room;

import java.util.Date;

/**
 * 房间与房间账户(历史数据)
 * 数据库实体
 * t_saas_room_history
 */
public class RoomHis extends Room{
	private String orgId;
	

    private String id;

    /**
     * 账户起始时间
     */
    private Date startTime;

    /**
     * 账户结束时间
     */
    private Date endTime;
    
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

}