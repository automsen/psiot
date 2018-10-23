package cn.com.tw.saas.serv.entity.room;

import java.util.Date;
@Deprecated
public class SaasRoom {
	
    private String id;

    /**
     * 机构主键
     */
    private String orgId;

    /**
     * 房间编号
     */
    private String roomNumber;

    /**
     * 房间名
     */
    private String roomName;

    /**
     * 楼栋主键
     */
    private String regionId;

    /**
     * 楼栋编号
     */
    private String regionNo;

    /**
     * 楼栋全名
     */
    private String regionFullName;

    /**
     * 房间用途
     */
    private String roomUse;

    /**
     * 面积
     */
    private String square;

    private Date createTime;

    private Date updateTime;
    
    /**
     * 扩展字段
     */
    private String elecMeter;
    
    private String elecInstallAddr;
    
    private String waterMeter;
    
    private String waterInstallAddr;
    

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }
    public String getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber == null ? null : roomNumber.trim();
    }
    public String getRoomName() {
        return roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? null : roomName.trim();
    }
    public String getRegionId() {
        return regionId;
    }
    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }
    public String getRegionNo() {
        return regionNo;
    }
    public void setRegionNo(String regionNo) {
        this.regionNo = regionNo == null ? null : regionNo.trim();
    }
    public String getRegionFullName() {
        return regionFullName;
    }
    public void setRegionFullName(String regionFullName) {
        this.regionFullName = regionFullName == null ? null : regionFullName.trim();
    }
    public String getRoomUse() {
        return roomUse;
    }
    public void setRoomUse(String roomUse) {
        this.roomUse = roomUse == null ? null : roomUse.trim();
    }
    public String getSquare() {
        return square;
    }
    public void setSquare(String square) {
        this.square = square == null ? null : square.trim();
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
	public String getElecMeter() {
		return elecMeter;
	}
	public void setElecMeter(String elecMeter) {
		this.elecMeter = elecMeter;
	}
	public String getElecInstallAddr() {
		return elecInstallAddr;
	}
	public void setElecInstallAddr(String elecInstallAddr) {
		this.elecInstallAddr = elecInstallAddr;
	}
	public String getWaterMeter() {
		return waterMeter;
	}
	public void setWaterMeter(String waterMeter) {
		this.waterMeter = waterMeter;
	}
	public String getWaterInstallAddr() {
		return waterInstallAddr;
	}
	public void setWaterInstallAddr(String waterInstallAddr) {
		this.waterInstallAddr = waterInstallAddr;
	}

}