package cn.com.tw.saas.serv.entity.db.read;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 人工读数记录
 * 数据库实体
 * 对应t_read_manual
 */
public class ReadManual implements Serializable{

	private static final long serialVersionUID = -1956104356261166671L;

	/**
     * 主键
     */
    private String id;

    /**
     * 机构ID(外键关联机构信息表)
     */
    private String orgId;

    /**
     * 仪表通讯地址
     */
    private String meterAddr;

    /**
     * 仪表是否存在
     */
    private Byte haveMeter;

    /**
     * 数据项编码（英文）
     */
    private String itemCode;

    /**
     * 读数
     */
    private BigDecimal readValue;

    /**
     * 抄表人id
     */
    private String readStaffId;

    /**
     * 抄表人姓名
     */
    private String readStaff;

    /**
     * 照片路径
     */
    private String photoUrl;

    /**
     * 读取时间
     */
    private Date readTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 楼栋全名
     */
    private String regionFullName;
    /**
     * 房间名
     */
    private String roomName;
    /**
     * 房间编号
     */
    private String roomNumber;
    /**
     * 设备类型
     */
    private String meterType;

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
    public String getMeterAddr() {
        return meterAddr;
    }
    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr == null ? null : meterAddr.trim();
    }
    public Byte getHaveMeter() {
        return haveMeter;
    }
    public void setHaveMeter(Byte haveMeter) {
        this.haveMeter = haveMeter;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode == null ? null : itemCode.trim();
    }
    public BigDecimal getReadValue() {
        return readValue;
    }
    public void setReadValue(BigDecimal readValue) {
        this.readValue = readValue;
    }
    public String getReadStaffId() {
        return readStaffId;
    }
    public void setReadStaffId(String readStaffId) {
        this.readStaffId = readStaffId == null ? null : readStaffId.trim();
    }
    public String getReadStaff() {
        return readStaff;
    }
    public void setReadStaff(String readStaff) {
        this.readStaff = readStaff == null ? null : readStaff.trim();
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl == null ? null : photoUrl.trim();
    }
    public Date getReadTime() {
        return readTime;
    }
    public void setReadTime(Date readTime) {
        this.readTime = readTime;
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
	public String getRegionFullName() {
		return regionFullName;
	}
	public void setRegionFullName(String regionFullName) {
		this.regionFullName = regionFullName;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
	public String getMeterType() {
		return meterType;
	}
	public void setMeterType(String meterType) {
		this.meterType = meterType;
	}
}