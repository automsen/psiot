package cn.com.tw.paas.monit.entity.db.org;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TerminalEquipParamDtu implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6355086382865012803L;

	private String equipNumber;

    private String equipName;

    private String orgId;

    private String appId;

    private String coordinate;

    private Integer childrenNum;

    private String installSite;

    private Date installTime;

    private String installTimeStr;

    private String workingMode;

    private String functionDigit;

    private String rfBaudRate;

    private Byte beattim;

    private Integer reportingInterval;

    private Date createTime;

    private Date updateTime;
    
    private Byte sendStatus;
    
    //扩展字段
    
    private String equipId;
    
    /**
     * 用户名
     */
    private String fUserName;
    
    /**
     * 电话号码
     */
    private String fPhone;
    
    /**
     * daas设备对象集合
     */
    private List<TerminalEquipChildren> dteList;
    
    /**
     * 居民信息
     */
    private OrgResident orgResident;

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

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate == null ? null : coordinate.trim();
    }

    public Integer getChildrenNum() {
        return childrenNum;
    }

    public void setChildrenNum(Integer childrenNum) {
        this.childrenNum = childrenNum;
    }

    public String getInstallSite() {
        return installSite;
    }

    public void setInstallSite(String installSite) {
        this.installSite = installSite == null ? null : installSite.trim();
    }

    public Date getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Date installTime) {
        this.installTime = installTime;
    }

    public String getWorkingMode() {
        return workingMode;
    }

    public void setWorkingMode(String workingMode) {
        this.workingMode = workingMode == null ? null : workingMode.trim();
    }

    public String getFunctionDigit() {
        return functionDigit;
    }

    public void setFunctionDigit(String functionDigit) {
        this.functionDigit = functionDigit == null ? null : functionDigit.trim();
    }

    public String getRfBaudRate() {
        return rfBaudRate;
    }

    public void setRfBaudRate(String rfBaudRate) {
        this.rfBaudRate = rfBaudRate == null ? null : rfBaudRate.trim();
    }

    public Byte getBeattim() {
        return beattim;
    }

    public void setBeattim(Byte beattim) {
        this.beattim = beattim;
    }

    public Integer getReportingInterval() {
        return reportingInterval;
    }

    public void setReportingInterval(Integer reportingInterval) {
        this.reportingInterval = reportingInterval;
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

	public String getInstallTimeStr() {
		return installTimeStr;
	}

	public void setInstallTimeStr(String installTimeStr) {
		this.installTimeStr = installTimeStr;
	}

	public String getfUserName() {
		return fUserName;
	}

	public void setfUserName(String fUserName) {
		this.fUserName = fUserName;
	}

	public String getfPhone() {
		return fPhone;
	}

	public void setfPhone(String fPhone) {
		this.fPhone = fPhone;
	}

	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	public Byte getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Byte sendStatus) {
		this.sendStatus = sendStatus;
	}

	public List<TerminalEquipChildren> getDteList() {
		return dteList;
	}

	public void setDteList(List<TerminalEquipChildren> dteList) {
		this.dteList = dteList;
	}

	public OrgResident getOrgResident() {
		return orgResident;
	}

	public void setOrgResident(OrgResident orgResident) {
		this.orgResident = orgResident;
	}
}