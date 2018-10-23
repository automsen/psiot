package cn.com.tw.paas.monit.entity.db.org;

import java.io.Serializable;
import java.util.Date;

public class OrgResident implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -494088730008597539L;

	/**
	 * 主键
	 */
    private String residentId;

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 居民姓名
     */
    private String residentName;

    /**
     * 居民电话
     */
    private String residentPhone;

    /**
     * 所在门牌号ID
     */
    private String locationId;

    /**
     * 热泵厂家ID
     */
    private String manufacturer;

    /**
     * 热泵类型集合
     */
    private String equipTypes;

    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 修改时间
     */
    private Date updateTime;
    
    
    public String getResidentId() {
        return residentId;
    }

    public void setResidentId(String residentId) {
        this.residentId = residentId == null ? null : residentId.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName == null ? null : residentName.trim();
    }

    public String getResidentPhone() {
        return residentPhone;
    }

    public void setResidentPhone(String residentPhone) {
        this.residentPhone = residentPhone == null ? null : residentPhone.trim();
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer == null ? null : manufacturer.trim();
    }

    public String getEquipTypes() {
        return equipTypes;
    }

    public void setEquipTypes(String equipTypes) {
        this.equipTypes = equipTypes == null ? null : equipTypes.trim();
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

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
}