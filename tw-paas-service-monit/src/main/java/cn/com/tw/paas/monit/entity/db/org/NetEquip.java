package cn.com.tw.paas.monit.entity.db.org;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应数据库表t_org_net_equip
 * @author Administrator
 *
 */
public class NetEquip implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6999921296055083109L;

	/**
	 * 仪表id
	 */
    private String equipId;

    /**
     * 设备编号
     */
    private String equipNumber;

    /**
     * 设备名称
     */
    private String equipName;

    /**
     * 注册机构id
     */
    private String orgId;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 设备一级分类(字典code)
     */
    private String equipCategCode;

    /**
     * 设备二级分类(字典code)
     */
    private String equipTypeCode;

    /**
     * 型号id
     */
    private String modelId;

    /**
     * 软件版本号
     */
    private String softVersoin;
    
    /**
     * 采集频率
     */
    private String gatherHz;

    /**
     * 通信密码
     */
    private String commPwd;

    /**
     * 上行网络类型
     */
    private String netTypeCode;

    /**
     * 网络编号
     */
    private String netNumber;

    /**
     * 集抄设备下联设备分类
     */
    private String childEquipTypeCode;

    /**
     * 是否可用
     */
    private Byte isUsable;

    /**
     * 是否历史设备
     */
    private Byte isHistory;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
    
    /**
     * 扇区数
     */
    private int sectorsNumber;
    
    /**
     * 单个扇区可接入仪表数
     */
    private int sectorsLimit;

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId == null ? null : equipId.trim();
    }

    public String getEquipNumber() {
        return equipNumber;
    }

    public void setEquipNumber(String equipNumber) {
        this.equipNumber = equipNumber == null ? null : equipNumber.trim();
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName == null ? null : equipName.trim();
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

    public String getEquipCategCode() {
        return equipCategCode;
    }

    public void setEquipCategCode(String equipCategCode) {
        this.equipCategCode = equipCategCode == null ? null : equipCategCode.trim();
    }

    public String getEquipTypeCode() {
        return equipTypeCode;
    }

    public void setEquipTypeCode(String equipTypeCode) {
        this.equipTypeCode = equipTypeCode == null ? null : equipTypeCode.trim();
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId == null ? null : modelId.trim();
    }

    public String getSoftVersoin() {
        return softVersoin;
    }

    public void setSoftVersoin(String softVersoin) {
        this.softVersoin = softVersoin == null ? null : softVersoin.trim();
    }

    public String getCommPwd() {
        return commPwd;
    }

    public void setCommPwd(String commPwd) {
        this.commPwd = commPwd == null ? null : commPwd.trim();
    }

    public String getNetTypeCode() {
        return netTypeCode;
    }

    public void setNetTypeCode(String netTypeCode) {
        this.netTypeCode = netTypeCode == null ? null : netTypeCode.trim();
    }

    public String getNetNumber() {
        return netNumber;
    }

    public void setNetNumber(String netNumber) {
        this.netNumber = netNumber == null ? null : netNumber.trim();
    }

    public String getChildEquipTypeCode() {
        return childEquipTypeCode;
    }

    public void setChildEquipTypeCode(String childEquipTypeCode) {
        this.childEquipTypeCode = childEquipTypeCode == null ? null : childEquipTypeCode.trim();
    }

    public Byte getIsUsable() {
        return isUsable;
    }

    public void setIsUsable(Byte isUsable) {
        this.isUsable = isUsable;
    }

    public Byte getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(Byte isHistory) {
        this.isHistory = isHistory;
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

	public String getGatherHz() {
		return gatherHz;
	}

	public void setGatherHz(String gatherHz) {
		this.gatherHz = gatherHz;
	}

	public int getSectorsNumber() {
		return sectorsNumber;
	}

	public void setSectorsNumber(int sectorsNumber) {
		this.sectorsNumber = sectorsNumber;
	}

	public int getSectorsLimit() {
		return sectorsLimit;
	}

	public void setSectorsLimit(int sectorsLimit) {
		this.sectorsLimit = sectorsLimit;
	}
}