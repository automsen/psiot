package cn.com.tw.saas.serv.entity.audit;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 结算 t_saas_clearing
 * 
 * @author Administrator
 *
 */
public class Clearing {
	
	
	
	private String id;

	/**
	 * 房间id
	 */
	private String roomId;

	/**
	 * 楼栋完整名称
	 */
	private String regionFullName;

	/**
	 * 房间名称
	 */
	private String roomName;

	/**
	 * 房间编号、同一楼栋下唯一
	 */
	private String roomNumber;

	/**
	 * 房间账户余额
	 */
	private BigDecimal roomBalance;

	/**
	 * 机构id
	 */
	private String orgId;
	
	/**
	 * 收款人 姓名
	 */
	private String payeeName;
	
	/**
	 * 收款人电话
	 */
	private String payeePhone;

	/**
	 * 仪表编号
	 */
	private String meterAddr;

	/**
	 * 仪表类型
	 */
	private String meterType;

	/**
	 * 能源类型
	 */
	private String energyType;

	/**
	 * 仪表余额
	 */
	private BigDecimal meterBalance;

	/**
	 * 最后读数
	 */
	private BigDecimal readValue;

	/**
	 * 最后读取时间
	 */
	private Date readTime;

	/**
	 * 结算状态 0.初始状态 待结算 1.通过 2.不通过 3.取消
	 */
	private String clearingState;

	/**
	 * 结算版本
	 */
	private String clearingVersion;

	/**
	 * 提交人id
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
	 * 创建时间
	 */
	private Date createTime;
	
	
	/**
	 * 开户日期
	 */
	private Date  sTime;
	
	/**
	 * 开户日期String类型
	 */
	private String sTimeStr;
	
	/**
	 * 结算日期String类型
	 */
	private String eTimeStr;

	/**
	 * 结算的时候预览的曾经单价
	 */
	private String price;
	
	
	private  String  sTimeStr2;
	
	private  String  eTimeStr2;
	
	private  String  StartReadStr2;
	
	private  String  BalanceUpdateReadStr2;
	
	
	//是否导出英文excel
	private String isEnExcel;
	
	
	public String getIsEnExcel() {
		return isEnExcel;
	}

	public void setIsEnExcel(String isEnExcel) {
		this.isEnExcel = isEnExcel;
	}

	public String getStartReadStr2() {
		return StartReadStr2;
	}

	public void setStartReadStr2(String startReadStr2) {
		StartReadStr2 = startReadStr2;
	}

	public String getBalanceUpdateReadStr2() {
		return BalanceUpdateReadStr2;
	}

	public void setBalanceUpdateReadStr2(String balanceUpdateReadStr2) {
		BalanceUpdateReadStr2 = balanceUpdateReadStr2;
	}

	public String getsTimeStr2() {
		return sTimeStr2;
	}

	public void setsTimeStr2(String sTimeStr2) {
		this.sTimeStr2 = sTimeStr2;
	}

	public String geteTimeStr2() {
		return eTimeStr2;
	}

	public void seteTimeStr2(String eTimeStr2) {
		this.eTimeStr2 = eTimeStr2;
	}

	

	

	

	public String getsTimeStr() {
		return sTimeStr;
	}

	public void setsTimeStr(String sTimeStr) {
		this.sTimeStr = sTimeStr;
	}

	public String geteTimeStr() {
		return eTimeStr;
	}

	public void seteTimeStr(String eTimeStr) {
		this.eTimeStr = eTimeStr;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * 结算日期
	 */
	private Date  eTime;
	public Date getsTime() {
		return sTime;
	}

	public void setsTime(Date sTime) {
		this.sTime = sTime;
	}

	public Date geteTime() {
		return eTime;
	}

	public void seteTime(Date eTime) {
		this.eTime = eTime;
	}
	/**
     * 充值时间的String转换类型
     */
    private String createTimeStr;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	
	/**
	 * 结算金额
	 */
	private BigDecimal clearingMoney;

	/**
	 * 客户信息
	 */
	private String custInfo;

	/**
	 * 客户名称
	 */
	private String customerRealname;

	/**
	 * 客户电话
	 */
	private String customerMobile1;
	
	/**
	 * 客户编号
	 */
	private String customerNo;

	/**
	 * 计价ID
	 */
	private String priceId;

	/**
	 * 预警ID
	 */
	private String alarmId;

	/**
	 * 起始读数
	 */
	private BigDecimal startRead;

	/**
	 * 计价方式
	 */
	private String priceModeCode;

	/**
	 * 最后余额时间
	 */
	private Date balanceUpdateTime;

	/**
	 * 最后读数
	 */
	private BigDecimal balanceUpdateRead;
	
	  /**
     * 0，未使用
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
     * 房间类型
     */
    private String roomUse;
    
    /**
     * 电表号
     */
    private String elecMeterAddr;

    /**
     * 水表号
     */
    private String waterMeterAddr;
    
    /**
     * 机构名称
     */
    private String orgName;
	
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRegionFullName() {
		return regionFullName;
	}

	public void setRegionFullName(String regionFullName) {
		this.regionFullName = regionFullName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public BigDecimal getRoomBalance() {
		return roomBalance;
	}

	public void setRoomBalance(BigDecimal roomBalance) {
		this.roomBalance = roomBalance;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getMeterAddr() {
		return meterAddr;
	}

	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}

	public String getMeterType() {
		return meterType;
	}

	public void setMeterType(String meterType) {
		this.meterType = meterType;
	}

	public String getEnergyType() {
		return energyType;
	}

	public void setEnergyType(String energyType) {
		this.energyType = energyType;
	}

	public BigDecimal getMeterBalance() {
		return meterBalance;
	}

	public void setMeterBalance(BigDecimal meterBalance) {
		this.meterBalance = meterBalance;
	}

	public BigDecimal getReadValue() {
		return readValue;
	}

	public void setReadValue(BigDecimal readValue) {
		this.readValue = readValue;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public String getClearingState() {
		return clearingState;
	}

	public void setClearingState(String clearingState) {
		this.clearingState = clearingState;
	}

	public String getClearingVersion() {
		return clearingVersion;
	}

	public void setClearingVersion(String clearingVersion) {
		this.clearingVersion = clearingVersion;
	}

	public String getSubmitId() {
		return submitId;
	}

	public void setSubmitId(String submitId) {
		this.submitId = submitId;
	}

	public String getSubmitName() {
		return submitName;
	}

	public void setSubmitName(String submitName) {
		this.submitName = submitName;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
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

	public BigDecimal getClearingMoney() {
		return clearingMoney;
	}

	public void setClearingMoney(BigDecimal clearingMoney) {
		this.clearingMoney = clearingMoney;
	}

	public String getCustInfo() {
		return custInfo;
	}

	public void setCustInfo(String custInfo) {
		this.custInfo = custInfo;
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

	public String getPriceId() {
		return priceId;
	}

	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}

	public String getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(String alarmId) {
		this.alarmId = alarmId;
	}

	

	public String getPriceModeCode() {
		return priceModeCode;
	}

	public void setPriceModeCode(String priceModeCode) {
		this.priceModeCode = priceModeCode;
	}

	public Date getBalanceUpdateTime() {
		return balanceUpdateTime;
	}

	public void setBalanceUpdateTime(Date balanceUpdateTime) {
		this.balanceUpdateTime = balanceUpdateTime;
	}

	

	public BigDecimal getStartRead() {
		return startRead;
	}

	public void setStartRead(BigDecimal startRead) {
		this.startRead = startRead;
	}

	public BigDecimal getBalanceUpdateRead() {
		return balanceUpdateRead;
	}

	public void setBalanceUpdateRead(BigDecimal balanceUpdateRead) {
		this.balanceUpdateRead = balanceUpdateRead;
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
		this.accountId = accountId;
	}

	public String getRoomUse() {
		return roomUse;
	}

	public void setRoomUse(String roomUse) {
		this.roomUse = roomUse;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
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

	public String getPayeeName() {
		return payeeName;
	}
	
	public String getOrgName() {
        return orgName;
    }
	
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	
	public void setDayTime(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

	public String getPayeePhone() {
		return payeePhone;
	}

	public void setPayeePhone(String payeePhone) {
		this.payeePhone = payeePhone;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}


}