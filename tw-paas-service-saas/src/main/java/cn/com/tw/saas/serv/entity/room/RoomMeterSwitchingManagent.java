package cn.com.tw.saas.serv.entity.room;

import java.math.BigDecimal;
import java.util.Date;

import cn.com.tw.common.utils.CommUtils;

/**
 * 电表通断管理
 * @author Administrator
 */
public class RoomMeterSwitchingManagent {

    /**
     * 房间id
     */
    private String roomId;

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 楼栋id
     */
    private String regionId;

    /**
     * 楼栋编号(层级)
     */
    private String regionNo;

    /**
     * 楼栋编号
     */
    private String regionNumber;

    /**
     * 楼栋完整名称
     */
    private String regionFullName;

    /**
     * 房间类型
     */
    private String roomUse;

    /**
     * 房间编号、同一楼栋下唯一
     */
    private String roomNumber;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 面积
     */
    private BigDecimal area;

    /**
     * 可入住人数
     */
    private Integer peopleLimit;

    /**
     * 已入住人数
     */
    private Integer peopleNumber;

    /**
     *  0，未使用
		1，正常使用中
		2，取消关联过程中
		3，历史数据
     */
    private Byte accountStatus;

    /**
     * 账户id
     */
    private String accountId;

    /**
     * 账户余额
     */
    private BigDecimal balance;

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
     * 租金规则id
     */
    private String rentalId;

    /**
     * 物业费规则id
     */
    private String propertyId;

    /**
     * 账户起始时间
     */
    private Date startTime;

    /**
     * 账户结束时间
     */
    private Date endTime;

    private Date createTime;

    private Date updateTime;
    /**
     * 客户编号
     */
    private String customerNo;
    
    /**
     * 客户名称
     */
    private String customerRealname;
    
    /**
     * 客户电话
     */
    private String customerMobile1;
    
    /**
     * 水的子账户余额
     */
    private BigDecimal subWaterBalance;
    
    /**
     * 电的子账户余额
     */
    private BigDecimal subElecBalance;
    
    /**
     * 合计余额
     */
    private BigDecimal totleBalance;
    
    /**
     * 扩展字段
     */
    private String elecMeter;
    
    private String elecInstallAddr;
    
    private String waterMeter;
    
    private String waterInstallAddr;
    
    
    private String meterType;

    private BigDecimal readValue;
    
    private String meterAddr;
    
    /**
     * 0，总回路
     */
    private Integer loopType;
    
    private String loopTypeStr;
    
    /**
     * 1，主回路
     */
    private Integer loopType1;
    
    private BigDecimal readValue1;
    
    /**
     * 2，副回路
     */
    private Integer loopType2;
    
    private String loopTypeStr2;
    
    private BigDecimal readValue2;
    /**
     * 3、第三回路
     */
    private Integer loopType3;
    
    private String loopTypeStr3;
    
    private BigDecimal readValue3;
    
    public BigDecimal getReadValue() {
		return readValue;
	}
	public void setReadValue(BigDecimal readValue) {
		this.readValue = readValue;
	}
	public String getMeterType() {
		return meterType;
	}
	public void setMeterType(String meterType) {
		this.meterType = meterType==null ? null : meterType.trim();
	}
	public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
    }
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }
    public String getRegionId() {
        return regionId;
    }
    public void setRegionId(String regionId) {
        this.regionId = regionId == null ? null : regionId.trim();
    }
    public String getRegionNo() {
        return regionNo;
    }
    public void setRegionNo(String regionNo) {
        this.regionNo = regionNo == null ? null : regionNo.trim();
    }
    public String getRegionNumber() {
        return regionNumber;
    }
    public void setRegionNumber(String regionNumber) {
        this.regionNumber = regionNumber == null ? null : regionNumber.trim();
    }
    public String getRegionFullName() {
        return regionFullName;
    }
    public void setRegionFullName(String regionFullName) {
        this.regionFullName = regionFullName == null ? null : regionFullName.trim();
    }
    public String getRoomUse() {
        return roomUse;
    }
    public void setRoomUse(String roomUse) {
        this.roomUse = roomUse == null ? null : roomUse.trim();
    }
    public String getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber == null ? null : roomNumber.trim();
    }
    public String getRoomName() {
        return roomName;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? null : roomName.trim();
    }
    public BigDecimal getArea() {
        return area;
    }
    public void setArea(BigDecimal area) {
        this.area = area;
    }
    public Integer getPeopleLimit() {
        return peopleLimit;
    }
    public void setPeopleLimit(Integer peopleLimit) {
        this.peopleLimit = peopleLimit;
    }
    public Integer getPeopleNumber() {
        return peopleNumber;
    }
    public void setPeopleNumber(Integer peopleNumber) {
        this.peopleNumber = peopleNumber;
    }
    public Byte getAccountStatus() {
        return accountStatus;
    }
    public void setAccountStatus(Byte accountStatus) {
        this.accountStatus = accountStatus;
    }
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
    public String getRentalId() {
        return rentalId;
    }
    public void setRentalId(String rentalId) {
        this.rentalId = rentalId == null ? null : rentalId.trim();
    }
    public String getPropertyId() {
        return propertyId;
    }
    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId == null ? null : propertyId.trim();
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
	public String getCustomerMobile1() {
		return customerMobile1;
	}
	public void setCustomerMobile1(String customerMobile1) {
		this.customerMobile1 = customerMobile1;
	}
	public BigDecimal getSubWaterBalance() {
		return subWaterBalance;
	}
	public void setSubWaterBalance(BigDecimal subWaterBalance) {
		this.subWaterBalance = subWaterBalance;
	}
	public BigDecimal getSubElecBalance() {
		return subElecBalance;
	}
	public void setSubElecBalance(BigDecimal subElecBalance) {
		this.subElecBalance = subElecBalance;
	}
	public BigDecimal getTotleBalance() {
		return totleBalance;
	}
	public void setTotleBalance(BigDecimal totleBalance) {
		this.totleBalance = totleBalance;
	}
    public String getElecMeter() {
		return elecMeter;
	}
	public void setElecMeter(String elecMeter) {
		this.elecMeter = elecMeter;
	}
	public String getElecInstallAddr() {
		return elecInstallAddr;
	}
	public void setElecInstallAddr(String elecInstallAddr) {
		this.elecInstallAddr = elecInstallAddr;
	}
	public String getWaterMeter() {
		return waterMeter;
	}
	public void setWaterMeter(String waterMeter) {
		this.waterMeter = waterMeter;
	}
	public String getWaterInstallAddr() {
		return waterInstallAddr;
	}
	public void setWaterInstallAddr(String waterInstallAddr) {
		this.waterInstallAddr = waterInstallAddr;
	}
	/**
     * 生成新的房间账户id
     * @return
     */
    public String generateAccountId(){
    	return CommUtils.getUuid();
    }
	public Integer getLoopType() {
		return loopType;
	}
	public void setLoopType(Integer loopType) {
		this.loopType = loopType;
	}
	public BigDecimal getReadValue1() {
		return readValue1;
	}
	public void setReadValue1(BigDecimal readValue1) {
		this.readValue1 = readValue1;
	}
	public Integer getLoopType1() {
		return loopType1;
	}
	public void setLoopType1(Integer loopType1) {
		this.loopType1 = loopType1;
	}
	public BigDecimal getReadValue2() {
		return readValue2;
	}
	public void setReadValue2(BigDecimal readValue2) {
		this.readValue2 = readValue2;
	}
	public Integer getLoopType2() {
		return loopType2;
	}
	public void setLoopType2(Integer loopType2) {
		this.loopType2 = loopType2;
	}
	public BigDecimal getReadValue3() {
		return readValue3;
	}
	public void setReadValue3(BigDecimal readValue3) {
		this.readValue3 = readValue3;
	}
	public Integer getLoopType3() {
		return loopType3;
	}
	public void setLoopType3(Integer loopType3) {
		this.loopType3 = loopType3;
	}
	public String getMeterAddr() {
		return meterAddr;
	}
	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}
	public String getLoopTypeStr() {
		return loopTypeStr;
	}
	public void setLoopTypeStr(String loopTypeStr) {
		this.loopTypeStr = loopTypeStr;
	}
	public String getLoopTypeStr2() {
		return loopTypeStr2;
	}
	public void setLoopTypeStr2(String loopTypeStr2) {
		this.loopTypeStr2 = loopTypeStr2;
	}
	public String getLoopTypeStr3() {
		return loopTypeStr3;
	}
	public void setLoopTypeStr3(String loopTypeStr3) {
		this.loopTypeStr3 = loopTypeStr3;
	}
}
