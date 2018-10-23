package cn.com.tw.saas.serv.entity.room;

import java.math.BigDecimal;
import java.util.Date;

import cn.com.tw.common.utils.CommUtils;

/**
 * 房间与房间账户
 * 数据库实体
 * t_saas_room
 */
public class Room {

	/**
	 * 备注 银联信息
	 */
	private String remarks;
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
     * 0，维护中
1，使用中
2，签约 审核中
3，废弃中
4.空置中
5.历史状态
6.结算审核中状态
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
     * 水的子账户余额的String类型
     */
    
	private  String   subWaterBalanceStr;
    
    /**
     * 电的子账户余额
     */
    private BigDecimal subElecBalance;
    
    
    /**
     * 电的子账户余额的String类型
     */
    private String subElecBalanceStr;
    
    /**
     * 合计余额
     */
    private BigDecimal totleBalance;
    
    
    /**
     *  合计余额更新时间
     */
    private Date totleBalanceUpdateTime;

    /**
     *  合计余额更新时读数
     */
    private BigDecimal totleBalanceUpdateRead;

    /**
     *  合计余额更新序号
     */
    private Integer totleBalanceUpdateNo;  
    
    
    //是否导出英文excel
    private String isEnExcel;
    
    
    public String getIsEnExcel() {
		return isEnExcel;
	}
	public void setIsEnExcel(String isEnExcel) {
		this.isEnExcel = isEnExcel;
	}
    
    
    /**
     * 房态管理扩展字段 开始
     */
    private String eMeterAddr;
    
    private String eAlarmId;
	
	private String eRuleName;
	
	private String ePriceId;
	
	private String ePriceName;
	
	private String ePriceModeCode;
	
	private String wMeterAddr;
	
	private String wAlarmId;
	
	private String wRuleName;
	
	private String wPriceId;
	
	private String wPriceName;
	
	private String wPriceModeCode;
	
	/**
	 * 1为是 0 为否
	 */
	private String isDelete;
	
	private Date lastRechargeTime;
	
	private Byte isUsed;
	
	/**
     * 房态管理扩展字段 结束
     */
    
	public String geteMeterAddr() {
		return eMeterAddr;
	}
	public void seteMeterAddr(String eMeterAddr) {
		this.eMeterAddr = eMeterAddr;
	}
	public String geteRuleName() {
		return eRuleName;
	}
	public void seteRuleName(String eRuleName) {
		this.eRuleName = eRuleName;
	}
	public String getePriceName() {
		return ePriceName;
	}
	public void setePriceName(String ePriceName) {
		this.ePriceName = ePriceName;
	}
	public String getePriceModeCode() {
		return ePriceModeCode;
	}
	public void setePriceModeCode(String ePriceModeCode) {
		this.ePriceModeCode = ePriceModeCode;
	}
	public String getwMeterAddr() {
		return wMeterAddr;
	}
	public void setwMeterAddr(String wMeterAddr) {
		this.wMeterAddr = wMeterAddr;
	}
	public String getwRuleName() {
		return wRuleName;
	}
	public void setwRuleName(String wRuleName) {
		this.wRuleName = wRuleName;
	}
	public String getwPriceName() {
		return wPriceName;
	}
	public void setwPriceName(String wPriceName) {
		this.wPriceName = wPriceName;
	}
	public String getwPriceModeCode() {
		return wPriceModeCode;
	}
	public void setwPriceModeCode(String wPriceModeCode) {
		this.wPriceModeCode = wPriceModeCode;
	}
	public Date getTotleBalanceUpdateTime() {
		return totleBalanceUpdateTime;
	}
	public void setTotleBalanceUpdateTime(Date totleBalanceUpdateTime) {
		this.totleBalanceUpdateTime = totleBalanceUpdateTime;
	}
	public BigDecimal getTotleBalanceUpdateRead() {
		return totleBalanceUpdateRead;
	}
	public void setTotleBalanceUpdateRead(BigDecimal totleBalanceUpdateRead) {
		this.totleBalanceUpdateRead = totleBalanceUpdateRead;
	}
	public Integer getTotleBalanceUpdateNo() {
		return totleBalanceUpdateNo;
	}
	public void setTotleBalanceUpdateNo(Integer totleBalanceUpdateNo) {
		this.totleBalanceUpdateNo = totleBalanceUpdateNo;
	}
	/**
     * 扩展字段
     */
    private String elecMeter;
    
    private String elecInstallAddr;
    
    private String waterMeter;
    
    private String waterInstallAddr;
    
    /**
     * 0为余额欠费
     * 1为余额报警
     */
    private String balanceType;
    
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
	public String getBalanceType() {
		return balanceType;
	}
	public void setBalanceType(String balanceType) {
		this.balanceType = balanceType;
	}
	public String getSubWaterBalanceStr() {
		return subWaterBalanceStr;
	}
	public void setSubWaterBalanceStr(String subWaterBalanceStr) {
		this.subWaterBalanceStr = subWaterBalanceStr;
	}
	public String getSubElecBalanceStr() {
		return subElecBalanceStr;
	}
	public void setSubElecBalanceStr(String subElecBalanceStr) {
		this.subElecBalanceStr = subElecBalanceStr;
	}
	public String geteAlarmId() {
		return eAlarmId;
	}
	public void seteAlarmId(String eAlarmId) {
		this.eAlarmId = eAlarmId;
	}
	public String getePriceId() {
		return ePriceId;
	}
	public void setePriceId(String ePriceId) {
		this.ePriceId = ePriceId;
	}
	public String getwAlarmId() {
		return wAlarmId;
	}
	public void setwAlarmId(String wAlarmId) {
		this.wAlarmId = wAlarmId;
	}
	public String getwPriceId() {
		return wPriceId;
	}
	public void setwPriceId(String wPriceId) {
		this.wPriceId = wPriceId;
	}
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	public Date getLastRechargeTime() {
		return lastRechargeTime;
	}
	public void setLastRechargeTime(Date lastRechargeTime) {
		this.lastRechargeTime = lastRechargeTime;
	}
	public Byte getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(Byte isUsed) {
		this.isUsed = isUsed;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
    
}