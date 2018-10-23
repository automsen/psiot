package cn.com.tw.paas.job.entity;

import java.io.Serializable;
import java.util.Date;

public class InsExe implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6006969606154741430L;

	private String id;

    private String cmdExeId;

    private String orgId;

    private String appId;

    private String businessNo;

    private String controlCode;

    private String dataMarker;

    private String insName;

    private String connAddr;

    private String param;

    private String returnValue;

    private Integer groupSort;

    private Byte status;

    private Integer useTime;
    
    private InsExe nextIns;

    private Date createTime;

    private Date updateTime;
    
    private Long startTime;
    
    /**
     *  指令下发次数
     */
    private Integer handleTimes;
    
    /**
     *  最大重试次数
     */
    private Integer limitHandleTimes;
    
    private String requestUrl;
    
    
    


    public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public InsExe getNextIns() {
		return nextIns;
	}

	public void setNextIns(InsExe nextIns) {
		this.nextIns = nextIns;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCmdExeId() {
        return cmdExeId;
    }

    public void setCmdExeId(String cmdExeId) {
        this.cmdExeId = cmdExeId == null ? null : cmdExeId.trim();
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

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo == null ? null : businessNo.trim();
    }

    public String getControlCode() {
        return controlCode;
    }

    public void setControlCode(String controlCode) {
        this.controlCode = controlCode == null ? null : controlCode.trim();
    }

    public String getDataMarker() {
        return dataMarker;
    }

    public void setDataMarker(String dataMarker) {
        this.dataMarker = dataMarker == null ? null : dataMarker.trim();
    }

    public String getInsName() {
        return insName;
    }

    public void setInsName(String insName) {
        this.insName = insName == null ? null : insName.trim();
    }

    public String getConnAddr() {
        return connAddr;
    }

    public void setConnAddr(String connAddr) {
        this.connAddr = connAddr == null ? null : connAddr.trim();
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param == null ? null : param.trim();
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue == null ? null : returnValue.trim();
    }

    public Integer getGroupSort() {
        return groupSort;
    }

    public void setGroupSort(Integer groupSort) {
        this.groupSort = groupSort;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getUseTime() {
        return useTime;
    }

    public void setUseTime(Integer useTime) {
        this.useTime = useTime;
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

	public Integer getHandleTimes() {
		return handleTimes;
	}

	public Integer getLimitHandleTimes() {
		return limitHandleTimes;
	}

	public void setHandleTimes(Integer handleTimes) {
		this.handleTimes = handleTimes;
	}

	public void setLimitHandleTimes(Integer limitHandleTimes) {
		this.limitHandleTimes = limitHandleTimes;
	}

	
    
    
}