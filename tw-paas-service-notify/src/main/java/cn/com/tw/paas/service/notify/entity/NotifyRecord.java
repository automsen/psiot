package cn.com.tw.paas.service.notify.entity;

import java.util.Date;

public class NotifyRecord {
	private String uuid;

    private Byte platType;

    private Integer notifyBusType;

    private Byte notifyType;

    private String notifyUrl;
    
    private String notifyContentType;

    private String notifyToNo;

    private String notifyBusNo;

    private String notifyToUserId;

    private String notifyToUserName;
    
    private String notifyContent;

    private String orgId;

    private String extField1;

    private String extField2;

    private String extField3;

    private String extField4;

    private String extField5;

    private Byte notifyLevel;

    private String status;

    private int notifyTimes;

    private int limitNotifyTimes = 5;

    private Date createTime;

    private Date lastNotifyTime;

    private int isFirst;
    
    public int getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(int isFirst) {
		this.isFirst = isFirst;
	}
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public Byte getPlatType() {
        return platType;
    }

    public void setPlatType(Byte platType) {
        this.platType = platType;
    }

    public Integer getNotifyBusType() {
		return notifyBusType;
	}

	public void setNotifyBusType(Integer notifyBusType) {
		this.notifyBusType = notifyBusType;
	}

	public Byte getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(Byte notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl == null ? null : notifyUrl.trim();
    }

    public String getNotifyToNo() {
		return notifyToNo;
	}

	public void setNotifyToNo(String notifyToNo) {
		this.notifyToNo = notifyToNo;
	}

	public String getNotifyBusNo() {
        return notifyBusNo;
    }

    public void setNotifyBusNo(String notifyBusNo) {
        this.notifyBusNo = notifyBusNo == null ? null : notifyBusNo.trim();
    }

    public String getNotifyToUserId() {
        return notifyToUserId;
    }

    public void setNotifyToUserId(String notifyToUserId) {
        this.notifyToUserId = notifyToUserId == null ? null : notifyToUserId.trim();
    }

    public String getNotifyToUserName() {
        return notifyToUserName;
    }

    public void setNotifyToUserName(String notifyToUserName) {
        this.notifyToUserName = notifyToUserName == null ? null : notifyToUserName.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getExtField1() {
        return extField1;
    }

    public void setExtField1(String extField1) {
        this.extField1 = extField1 == null ? null : extField1.trim();
    }

    public String getExtField2() {
        return extField2;
    }

    public void setExtField2(String extField2) {
        this.extField2 = extField2 == null ? null : extField2.trim();
    }

    public String getExtField3() {
        return extField3;
    }

    public void setExtField3(String extField3) {
        this.extField3 = extField3 == null ? null : extField3.trim();
    }

    public String getExtField4() {
        return extField4;
    }

    public void setExtField4(String extField4) {
        this.extField4 = extField4 == null ? null : extField4.trim();
    }

    public String getExtField5() {
        return extField5;
    }

    public void setExtField5(String extField5) {
        this.extField5 = extField5 == null ? null : extField5.trim();
    }

    public Byte getNotifyLevel() {
        return notifyLevel;
    }

    public void setNotifyLevel(Byte notifyLevel) {
        this.notifyLevel = notifyLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public int getNotifyTimes() {
		return notifyTimes;
	}

	public void setNotifyTimes(int notifyTimes) {
		this.notifyTimes = notifyTimes;
	}

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastNotifyTime() {
        return lastNotifyTime;
    }

    public void setLastNotifyTime(Date lastNotifyTime) {
        this.lastNotifyTime = lastNotifyTime;
    }

	public int getLimitNotifyTimes() {
		return limitNotifyTimes;
	}

	public void setLimitNotifyTimes(int limitNotifyTimes) {
		this.limitNotifyTimes = limitNotifyTimes;
	}

	@Override
	public String toString() {
		return "NotifyRecord [uuid=" + uuid + ", platType=" + platType
				+ ", notifyBusType=" + notifyBusType + ", notifyType="
				+ notifyType + ", notifyUrl=" + notifyUrl
				+ ", notifyContentType=" + notifyContentType + ", notifyToNo="
				+ notifyToNo + ", notifyBusNo=" + notifyBusNo
				+ ", notifyToUserId=" + notifyToUserId + ", notifyToUserName="
				+ notifyToUserName + ", notifyContent=" + notifyContent
				+ ", orgId=" + orgId + ", extField1=" + extField1
				+ ", extField2=" + extField2 + ", extField3=" + extField3
				+ ", extField4=" + extField4 + ", extField5=" + extField5
				+ ", notifyLevel=" + notifyLevel + ", status=" + status
				+ ", notifyTimes=" + notifyTimes + ", limitNotifyTimes="
				+ limitNotifyTimes + ", createTime=" + createTime
				+ ", lastNotifyTime=" + lastNotifyTime + ", isFirst=" + isFirst
				+ "]";
	}

	public String getNotifyContent() {
		return notifyContent;
	}

	public void setNotifyContent(String notifyContent) {
		this.notifyContent = notifyContent;
	}

	public String getNotifyContentType() {
		return notifyContentType;
	}

	public void setNotifyContentType(String notifyContentType) {
		this.notifyContentType = notifyContentType;
	}
}