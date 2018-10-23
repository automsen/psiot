package cn.com.tw.paas.monit.entity.db.orgEquipment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 设备实体  对应那个表t_org_equipment
 * @author Administrator
 *
 */
public class OrgEquipment implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4558581669964214403L;

	/**
	 * 仪表id
	 */
	private String equipId;

	/**
	 * 通信地址
	 */
	@NotNull
    private String commAddr;

    /**
	 * 设备名称
	 */
    private String equipName;

    /**
	 * 注册机构id
	 */
    private String orgId;
    
    /**
     * 应用ID
     */
    private String appId;

    /**
	 * 设备1级分类(字典code)
	 */
    private String equipCateg;

    /**
	 * 设备2级类型(字典code)
	 */
    private String equipType;
    
    /**
     * 集抄设备下联设备分类
     */
    private String chirdEquipType;

    /**
	 * 型号id
	 */
    private String modelId;

    /**
	 * 软件版本号
	 */
    private String softVersoin;

    /**
	 * 通信密码
	 */
    private String commPwd;

    /**
	 * 网络连接方式
	 */
    private String netType;

    /**
	 * 网络设备地址
	 */
    private String netEquipAddr;

    /**
	 * 电表子类
	 */
    private String elecMeterType;

    /**
	 * 额定电压
	 */
    private BigDecimal voltageRating;

    /**
	 * 额定电流
	 */
    private BigDecimal currentRating;

    /**
	 * 额定功率
	 */
    private BigDecimal powerRating;

    /**
	 * 最大电流
	 */
    private BigDecimal currentMax;

    /**
	 * 最大功率
	 */
    private BigDecimal powerMax;

    /**
	 * 电压变比(默认值为1)
	 */
    private BigDecimal voltageRatio;

    /**
	 * 电流变比(默认值为1)
	 */
    private BigDecimal currentRatio;

    /**
	 * 指令开
            指令关
            强制开
            强制关
            
	 */
    private String controlStatus;

    /**
	 * 是否可用
	 */
    private Byte isUsable;

    /**
	 * 是否历史仪表
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

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId == null ? null : equipId.trim();
    }

    public String getCommAddr() {
        return commAddr;
    }

    public void setCommAddr(String commAddr) {
        this.commAddr = commAddr == null ? null : commAddr.trim();
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

    public String getEquipCateg() {
        return equipCateg;
    }

    public void setEquipCateg(String equipCateg) {
        this.equipCateg = equipCateg == null ? null : equipCateg.trim();
    }

    public String getEquipType() {
        return equipType;
    }

    public void setEquipType(String equipType) {
        this.equipType = equipType == null ? null : equipType.trim();
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

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType == null ? null : netType.trim();
    }

    public String getNetEquipAddr() {
        return netEquipAddr;
    }

    public void setNetEquipAddr(String netEquipAddr) {
        this.netEquipAddr = netEquipAddr == null ? null : netEquipAddr.trim();
    }

    public String getElecMeterType() {
        return elecMeterType;
    }

    public void setElecMeterType(String elecMeterType) {
        this.elecMeterType = elecMeterType == null ? null : elecMeterType.trim();
    }

    public BigDecimal getVoltageRating() {
        return voltageRating;
    }

    public void setVoltageRating(BigDecimal voltageRating) {
        this.voltageRating = voltageRating;
    }

    public BigDecimal getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(BigDecimal currentRating) {
        this.currentRating = currentRating;
    }

    public BigDecimal getPowerRating() {
        return powerRating;
    }

    public void setPowerRating(BigDecimal powerRating) {
        this.powerRating = powerRating;
    }

    public BigDecimal getCurrentMax() {
        return currentMax;
    }

    public void setCurrentMax(BigDecimal currentMax) {
        this.currentMax = currentMax;
    }

    public BigDecimal getPowerMax() {
        return powerMax;
    }

    public void setPowerMax(BigDecimal powerMax) {
        this.powerMax = powerMax;
    }

    public BigDecimal getVoltageRatio() {
        return voltageRatio;
    }

    public void setVoltageRatio(BigDecimal voltageRatio) {
        this.voltageRatio = voltageRatio;
    }

    public BigDecimal getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(BigDecimal currentRatio) {
        this.currentRatio = currentRatio;
    }

    public String getControlStatus() {
        return controlStatus;
    }

    public void setControlStatus(String controlStatus) {
        this.controlStatus = controlStatus == null ? null : controlStatus.trim();
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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getChirdEquipType() {
		return chirdEquipType;
	}

	public void setChirdEquipType(String chirdEquipType) {
		this.chirdEquipType = chirdEquipType;
	}
}