package cn.com.tw.saas.serv.entity.room;

import java.util.Date;

public class SaasRegion {
	
    private String id;

    /**
     * 机构ID
     */
    private String orgId;

    /**
     * 楼栋编号
     */
    private String regionNo;

    /**
     * 完整名称
     */
    private String regionFullName;

    /**
     * 楼栋名称
     */
    private String regionName;
     
    /**
     * 楼栋名编号
     */
    private String regionNumber;

    /**
     * 上级楼栋编号
     */
    private String pRegionNo;

    /**
     * 面积
     */
    private String square;

    /**
     * 是否顶级 1=是 0=否
     */
    private Byte isTop;

    /**
     * 层级
     */
    private Byte tier;

    /**
     * 是否叶子 1=是0=否
     */
    private Byte isLeaf;

    private Date createTime;

    private Date updateTime;

    /**
     * 是否可用 0=是1=否
     */
    private Byte isDelete;
    
//    private String regionCode;

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
    public String getRegionName() {
        return regionName;
    }
    public void setRegionName(String regionName) {
        this.regionName = regionName == null ? null : regionName.trim();
    }
    public String getPRegionNo() {
        return pRegionNo;
    }
    public void setPRegionNo(String pRegionNo) {
        this.pRegionNo = pRegionNo == null ? null : pRegionNo.trim();
    }
    public String getSquare() {
        return square;
    }
    public void setSquare(String square) {
        this.square = square == null ? null : square.trim();
    }
    public Byte getIsTop() {
        return isTop;
    }
    public void setIsTop(Byte isTop) {
        this.isTop = isTop;
    }
    public Byte getTier() {
        return tier;
    }
    public void setTier(Byte tier) {
        this.tier = tier;
    }
    public Byte getIsLeaf() {
        return isLeaf;
    }
    public void setIsLeaf(Byte isLeaf) {
        this.isLeaf = isLeaf;
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
    public Byte getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
	public String getRegionNumber() {
		return regionNumber;
	}
	public void setRegionNumber(String regionNumber) {
		this.regionNumber = regionNumber;
	}

//	public String getRegionCode() {
//		return regionCode;
//	}
//
//	public void setRegionCode(String regionCode) {
//		this.regionCode = regionCode;
//	}

}