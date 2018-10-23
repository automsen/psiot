package cn.com.tw.saas.serv.entity.audit;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 房间审核 t_saas_audit_room
 * @author Administrator
 *
 */
public class AuditRoom {
	/**
	 * 
	 */
    private String id;

    /**
	 * 机构ID
	 */
    private String orgId;

    /**
	 * 提交人ID
	 */
    private String submitId;

    /**
	 * 提交人
	 */
    private String submitName;

    /**
	 * 审核人id
	 */
    private String auditId;

    /**
	 * 审核人
	 */
    private String auditName;

    /**
	 * 房间余额
	 */
    private BigDecimal roomBalance;

    /**
	 * 电补贴方式
	 */
    private String elecAllowanceType;

    /**
	 * 计划用电规则id
	 */
    private String elecAllowanceRuleId;

    /**
	 * 剩余计划用电量
	 */
    private BigDecimal elecAllowance;

    /**
	 * 水补贴方式
	 */
    private String waterAllowanceType;

    /**
	 * 计划用水规则id
	 */
    private String waterAllowanceRuleId;

    /**
	 * 剩余计划用水量
	 */
    private BigDecimal waterAllowance;

    /**
	 * 账户起始时间
	 */
    private Date startTime;

    /**
	 * 账户结束时间
	 */
    private Date endTime;

    /**
	 * 审核类型 0.签约审核1.续约审核2.宿舍变更审核 3.使用中的宿舍变更审核
	 */
    private Byte auditType;

    /**
	 * 审核状态 0.待审核 1.通过 2.退回 3.取消
	 */
    private Byte auditStatus;

    /**
	 * 审核意见
	 */
    private String auditOpinion;

    /**
	 * 审核版本
	 */
    private String auditVersion;

    /**
	 * 房间ID
	 */
    private String roomId;

    /**
	 * 房间编号
	 */
    private String roomNumber;

    /**
	 * 楼栋全名
	 */
    private String regionFullName;

    /**
	 * 房间名
	 */
    private String roomName;
    
    /**
     * 房间类型
     */
    private String roomUse;

    /**
	 * 创建时间
	 */
    private Date createTime;

    /**
	 * 修改时间
	 */
    private Date updateTime;
    
    /**
     * 房间账户ID
     */
    private String accountId;
    
    
    private String subAccountId;
    
    /**
     * 电表编号
     */
    private String elecMeterAddr;
    
    /**
     * 水表编号
     */
    private String waterMeterAddr;
    
    /**
     * 客户信息
     */
    private String customerInfo;
    
    /**
     * 租金规则id
     */
    private String rentalId;

    /**
     * 物业费规则id
     */
    private String propertyId;
    
    /**
     * 客户电话
     */
    private String customerMobile1;
    
    /**
     * 客户编号
     */
    private String customerNo;
    
    /**
     * 客户名
     */
    private String customerRealname;
    /**
     * 客户ID
     */
    private String customerId;

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

    public String getSubmitId() {
        return submitId;
    }

    public void setSubmitId(String submitId) {
        this.submitId = submitId == null ? null : submitId.trim();
    }

    public String getSubmitName() {
        return submitName;
    }

    public void setSubmitName(String submitName) {
        this.submitName = submitName == null ? null : submitName.trim();
    }

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId == null ? null : auditId.trim();
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName == null ? null : auditName.trim();
    }

    public BigDecimal getRoomBalance() {
        return roomBalance;
    }

    public void setRoomBalance(BigDecimal roomBalance) {
        this.roomBalance = roomBalance;
    }

    public String getElecAllowanceType() {
        return elecAllowanceType;
    }

    public void setElecAllowanceType(String elecAllowanceType) {
        this.elecAllowanceType = elecAllowanceType == null ? null : elecAllowanceType.trim();
    }

    public String getElecAllowanceRuleId() {
        return elecAllowanceRuleId;
    }

    public void setElecAllowanceRuleId(String elecAllowanceRuleId) {
        this.elecAllowanceRuleId = elecAllowanceRuleId == null ? null : elecAllowanceRuleId.trim();
    }

    public BigDecimal getElecAllowance() {
        return elecAllowance;
    }

    public void setElecAllowance(BigDecimal elecAllowance) {
        this.elecAllowance = elecAllowance;
    }

    public String getWaterAllowanceType() {
        return waterAllowanceType;
    }

    public void setWaterAllowanceType(String waterAllowanceType) {
        this.waterAllowanceType = waterAllowanceType == null ? null : waterAllowanceType.trim();
    }

    public String getWaterAllowanceRuleId() {
        return waterAllowanceRuleId;
    }

    public void setWaterAllowanceRuleId(String waterAllowanceRuleId) {
        this.waterAllowanceRuleId = waterAllowanceRuleId == null ? null : waterAllowanceRuleId.trim();
    }

    public BigDecimal getWaterAllowance() {
        return waterAllowance;
    }

    public void setWaterAllowance(BigDecimal waterAllowance) {
        this.waterAllowance = waterAllowance;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Byte getAuditType() {
        return auditType;
    }

    public void setAuditType(Byte auditType) {
        this.auditType = auditType;
    }

    public Byte getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion == null ? null : auditOpinion.trim();
    }

    public String getAuditVersion() {
        return auditVersion;
    }

    public void setAuditVersion(String auditVersion) {
        this.auditVersion = auditVersion == null ? null : auditVersion.trim();
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

    public String getRegionFullName() {
        return regionFullName;
    }

    public void setRegionFullName(String regionFullName) {
        this.regionFullName = regionFullName == null ? null : regionFullName.trim();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? null : roomName.trim();
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

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getElecMeterAddr() {
		return elecMeterAddr;
	}

	public void setElecMeterAddr(String elecMeterAddr) {
		this.elecMeterAddr = elecMeterAddr;
	}

	public String getWaterMeterAddr() {
		return waterMeterAddr;
	}

	public void setWaterMeterAddr(String waterMeterAddr) {
		this.waterMeterAddr = waterMeterAddr;
	}

	public String getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(String customerInfo) {
		this.customerInfo = customerInfo;
	}

	public String getRentalId() {
		return rentalId;
	}

	public void setRentalId(String rentalId) {
		this.rentalId = rentalId;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getRoomUse() {
		return roomUse;
	}

	public void setRoomUse(String roomUse) {
		this.roomUse = roomUse;
	}

	public String getCustomerMobile1() {
		return customerMobile1;
	}

	public void setCustomerMobile1(String customerMobile1) {
		this.customerMobile1 = customerMobile1;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerRealname() {
		return customerRealname;
	}

	public void setCustomerRealname(String customerRealname) {
		this.customerRealname = customerRealname;
	}

	public String getSubAccountId() {
		return subAccountId;
	}

	public void setSubAccountId(String subAccountId) {
		this.subAccountId = subAccountId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}