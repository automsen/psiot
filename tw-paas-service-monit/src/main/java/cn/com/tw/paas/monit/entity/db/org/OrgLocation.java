package cn.com.tw.paas.monit.entity.db.org;

import java.io.Serializable;
import java.util.Date;

public class OrgLocation implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4044914577929724806L;

	private String locationId;

    private String locationName;

    private String areaId;

    private String manufacturerId;

    private String equipTypes;

    private Byte isUsable;

    private String remark;

    private Date createTime;

    private Date updateTime;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId == null ? null : locationId.trim();
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName == null ? null : locationName.trim();
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId == null ? null : areaId.trim();
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId == null ? null : manufacturerId.trim();
    }

    public String getEquipTypes() {
        return equipTypes;
    }

    public void setEquipTypes(String equipTypes) {
        this.equipTypes = equipTypes == null ? null : equipTypes.trim();
    }

    public Byte getIsUsable() {
        return isUsable;
    }

    public void setIsUsable(Byte isUsable) {
        this.isUsable = isUsable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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
}