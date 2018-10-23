package cn.com.tw.saas.serv.entity.room;

import java.util.Date;

public class SaasRoomMeter {

    private String id;

    /**
     * 机构主键
     */
    private String orgId;

    /**
     * 房间是否存在 1=是 0=否
     */
    private Byte haveRoom;

    /**
     * 楼栋主键
     */
    private String regionId;

    /**
     * 楼栋编号
     */
    private String regionNo;

    /**
     * 房间主键
     */
    private String roomId;

    /**
     * 房间号
     */
    private String roomNumber;

    /**
     * 房间名
     */
    private String roomName;

    /**
     * 仪表是否存在 1=是 0=否
     */
    private Byte haveMeter;

    private String energyType;

    /**
     * 仪表类型
     */
    private String meterType;

    /**
     * 仪表编号
     */
    private String meterAddr;

    private Date createTime;

    private Date updateTime;
    
    /**
     * 扩展字段
     */
    private Byte haveWater;
    /**
     * 水表号
     */
    private String waterAddr;
    /**
     * 电表号
     */
    private String elecAddr;
    
//    private String regionFullName;
    /**
     * 房间账户状态
     */
    private String accountStatus;
    
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
    public Byte getHaveRoom() {
        return haveRoom;
    }
    public void setHaveRoom(Byte haveRoom) {
        this.haveRoom = haveRoom;
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
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
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
    public Byte getHaveMeter() {
        return haveMeter;
    }
    public void setHaveMeter(Byte haveMeter) {
        this.haveMeter = haveMeter;
    }
    public String getEnergyType() {
        return energyType;
    }
    public void setEnergyType(String energyType) {
        this.energyType = energyType == null ? null : energyType.trim();
    }
    public String getMeterType() {
        return meterType;
    }
    public void setMeterType(String meterType) {
        this.meterType = meterType == null ? null : meterType.trim();
    }
    public String getMeterAddr() {
        return meterAddr;
    }
    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr == null ? null : meterAddr.trim();
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
	public String getWaterAddr() {
		return waterAddr;
	}
	public void setWaterAddr(String waterAddr) {
		this.waterAddr = waterAddr;
	}
	public Byte getHaveWater() {
		return haveWater;
	}
	public void setHaveWater(Byte haveWater) {
		this.haveWater = haveWater;
	}
	public String getElecAddr() {
		return elecAddr;
	}
	public void setElecAddr(String elecAddr) {
		this.elecAddr = elecAddr;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

//	public String getRegionFullName() {
//		return regionFullName;
//	}
//
//	public void setRegionFullName(String regionFullName) {
//		this.regionFullName = regionFullName;
//	}
}